package TFC.Food;

import java.util.List;

import TFC.Core.TFC_ItemHeat;
import TFC.Core.TFC_Settings;
import TFC.Core.Player.TFC_PlayerClient;
import TFC.Core.Player.TFC_PlayerServer;
import TFC.Enums.EnumSize;
import TFC.Enums.EnumWeight;
import TFC.Items.ISize;
import TFC.Items.ItemTerra;

import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.World;

public class ItemTerraFood extends ItemFood implements ISize
{
	String texture;
	public int foodID;
	private EnumSize size = EnumSize.SMALL;
	private EnumWeight weight = EnumWeight.LIGHT;
	public int Tier = 0;

	public ItemTerraFood(int id, int healAmt) 
	{
		super(id, healAmt, true);
	}
	public ItemTerraFood(int id, int healAmt, String tex) 
	{
		this(id, healAmt);
		texture = tex;
	}

	public ItemTerraFood(int id, int healAmt, float saturation, boolean wolfFood, String tex, int foodid)
	{
		super(id, healAmt, saturation, wolfFood);
		texture = tex;
		foodID = foodid;
	}
	
	public ItemTerraFood(int id, int healAmt, float saturation, boolean wolfFood, String tex, int foodid, int tier)
	{
		super(id, healAmt, saturation, wolfFood);
		texture = tex;
		foodID = foodid;
		Tier = tier;
	}

	public ItemTerraFood setFoodID(int id)
	{
		foodID = id;
		return this;
	}
	
	public ItemTerraFood setFoodTier(int tier)
	{
		Tier = tier;
		return this;
	}

	@Override
	public String getTextureFile()
	{
		return texture;
	}

	public ItemTerraFood setTexturePath(String t)
	{
		texture = t;
		return this;
	}

	public static void addFoodTempInformation(ItemStack is, List arraylist)
    {
		if (is.hasTagCompound())
		{
			NBTTagCompound stackTagCompound = is.getTagCompound();

			if(stackTagCompound.hasKey("temperature"))
			{
				float temp = stackTagCompound.getFloat("temperature");
				float meltTemp = 0;

				meltTemp = TFC_ItemHeat.getMeltingPoint(is);

				if(meltTemp != -1)
				{
					arraylist.add(TFC_ItemHeat.getHeatColorFood(temp, meltTemp));
				}
			}
		}
    }
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) 
	{
		ItemTerra.addSizeInformation(this, arraylist);

		this.addFoodTempInformation(is, arraylist);
		
		int filling = this.getHealAmount() / 10;
		if(filling > 0)
		{
			String stars = "";
			int whitestars = 5-filling;
			int blackstars = filling;

			for(int i = 0; i < blackstars; i++)
			{
				stars += "\u272e";
			}
			for(int i = 0; i < whitestars; i++)
			{
				stars += "\u2729";
			}

			arraylist.add("Filling: " + stars);
		}
	}

	@Override
	public void onUpdate(ItemStack is, World world, Entity entity, int i, boolean isSelected) 
	{
		if (!world.isRemote && is.hasTagCompound())
		{
			NBTTagCompound stackTagCompound = is.getTagCompound();

			if(stackTagCompound.hasKey("temperature"))
			{
				TFC_ItemHeat.HandleItemHeat(is, (int)entity.posX, (int)entity.posY, (int)entity.posZ);
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			TFC_PlayerServer playerserver = TFC_PlayerServer.getFromEntityPlayer(player);
			if ((playerserver.getFoodStatsTFC().needFood()))
			{
				player.setItemInUse(is, this.getMaxItemUseDuration(is));
			}
		}
		else
		{
			TFC_PlayerClient playerclient = TFC_PlayerClient.getFromEntityPlayer(player);
			if ((playerclient.getFoodStatsTFC().needFood()))
			{
				player.setItemInUse(is, this.getMaxItemUseDuration(is));
			}
		}

		return is;
	}

	@Override
	public ItemStack onFoodEaten(ItemStack is, World world, EntityPlayer player)
	{
		if(!world.isRemote)
		{
			--is.stackSize;
			TFC_PlayerServer playerServer = (TFC_PlayerServer) ((EntityPlayerMP)player).getServerPlayerBase("TFC Player Server");
			playerServer.getFoodStatsTFC().addStats(this);
			player.getFoodStats().addStats(this);
			world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			this.func_77849_c(is, world, player);
		}
		return is;
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

	@Override
	public int getItemStackLimit()
	{
		if(canStack())
			return this.getSize().stackSize * getWeight().multiplier <= 64 ? this.getSize().stackSize * getWeight().multiplier : 64;
			else
				return 1;
	}
	
	public boolean isHot(ItemStack is)
	{
		if(TFC_ItemHeat.GetTemperature(is) > TFC_ItemHeat.getMeltingPoint(is) *0.8)
			return true;
		else return false;
	}

	public Item setSize(EnumSize s)
	{
		size = s;
		return this;
	}

	public Item setWeight(EnumWeight w)
	{
		weight = w;
		return this;
	}

	@Override
	public EnumSize getSize() {
		// TODO Auto-generated method stub
		return size;
	}
	@Override
	public EnumWeight getWeight() {
		// TODO Auto-generated method stub
		return weight;
	}
	@Override
	public boolean canStack() {
		// TODO Auto-generated method stub
		return true;
	}
}
