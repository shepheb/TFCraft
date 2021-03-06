package TFC.Core;

import java.util.ArrayList;
import java.util.Random;

import TFC.WorldGen.TFCBiome;
import TFC.WorldGen.TFCWorldChunkManager;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.World;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class TFC_Climate 
{
	public static World worldObj;
	public static TFCWorldChunkManager manager;

	/**
	 * All Temeprature related code
	 */

	protected static float getZFactor(int zCoord)
	{
		float factor = 0;
		float z = zCoord;

		//Clamp Z
		if(z > getMaxZPos()) z = getMaxZPos();
		if(z < -getMaxZPos()) z = -getMaxZPos();

		//Get the factor
		if(z > 0)
			factor = (getMaxZPos()-z)/getMaxZPos();
		else
			factor = (-getMaxZPos()-z)/-getMaxZPos();

		return factor;
	}

	protected static float getTemp(int x, int z)
	{
		return getTemp(TFC_Time.currentDay, x, z);
	}

	protected static float getTemp(int day, int x, int z)
	{
		float zMod = getZFactor(z);
		float zTemp = (zMod * getMaxTemperature())-10;

		float rainMod = 1-manager.getRainfallLayerAt(x, z).floatdata1/16000;

		Random R = new Random(day * x + z);
		
		int _month = TFC_Time.getMonthFromDayOfYear(day);
		int _lastmonth = TFC_Time.getMonthFromDayOfYear(day-30);
		
		float mod = getMonthTempFactor(_month, x, z);
		float modLast = getMonthTempFactor(_lastmonth, x, z);
		int day2 = day - ((day/30)*30);
		int hour = (int) TFC_Time.getHour();

		if(hour < 0)
			hour = 23 + hour;
		float hourMod = 0;

		if(hour < 12)
			hourMod = ((float)hour/11) * 0.2F;
		else
			hourMod = 0.2F - (((float)(hour-12)/11) * 0.2F);

		float monthMod = 0;
		float temp = 0;

		float dailyTemp = WeatherManager.getInstance().getDailyTemp(day);

		if(modLast > mod)
		{
			monthMod = ((modLast-mod)/30)*day2;
			monthMod = (modLast - monthMod);

			temp += ((zTemp + dailyTemp));
			if(temp < 0)monthMod = 1 - monthMod;
			temp *= monthMod;
			temp += (hourMod*(zTemp + dailyTemp));
			temp += (8*rainMod)*zMod;
		}
		else
		{
			monthMod = ((modLast-mod)/30)*day2;
			monthMod = (modLast + monthMod);

			temp += ((zTemp + dailyTemp));
			if(temp < 0)monthMod = 1 - monthMod;
			temp *= monthMod;
			temp += (hourMod*(zTemp + dailyTemp));
			temp += (8*rainMod)*zMod;
		}

		return temp;
	}

	protected static float getBioTemp(int day, int x, int z)
	{
		float zMod = getZFactor(z);
		float zTemp = (zMod * getMaxTemperature())-10;

		float rain = manager.getRainfallLayerAt(x, z).floatdata1;
		float rainMod = 1-(rain/4000);

		Random R = new Random(day * x + z);
		
		int _month = TFC_Time.getMonthFromDayOfYear(day);
		int _lastmonth = TFC_Time.getMonthFromDayOfYear(day-30);
		
		float monthModifier = getMonthTempFactor(_month, x, z);
		float lastMonthModifier = getMonthTempFactor(_lastmonth, x, z);
		
		int dayOfMonth =  TFC_Time.getDayOfMonthFromDayOfYear(day);

		float hourMod = 0.2f;

		float monthMod = 0;
		float temp = 0;

		if(lastMonthModifier > monthModifier)
		{
			monthMod = ((lastMonthModifier-monthModifier)/30)*dayOfMonth;
			monthMod = (lastMonthModifier - monthMod);

			temp += ((zTemp));
			if(temp < 0)monthMod = 1 - monthMod;
			temp *= monthMod;
			temp += (hourMod*(zTemp));
			temp += (8*rainMod)*zMod;
		}
		else
		{
			monthMod = ((lastMonthModifier-monthModifier)/30)*dayOfMonth;
			monthMod = (lastMonthModifier + monthMod);

			temp += ((zTemp));
			if(temp < 0)monthMod = 1 - monthMod;
			temp *= monthMod;
			temp += (hourMod*(zTemp));
			temp += (8*rainMod)*zMod;
		}

		return temp;
	}

	protected static float getMonthTempFactor(int month, int x, int z)
	{
		float factor = getZFactor(z);

		float diff = 1-factor;

		switch(month)
		{
		case 11:
			return 1-((diff/6)*4);
		case 0:
			return 1-((diff/6)*3);
		case 1:
			return 1-((diff/6)*2);
		case 2:
			return 1-((diff/6)*1);
		case 3:
			return 1-((diff/6)); 
		case 4:
			return 1F;
		case 5:
			return 1-((diff/6)*1.2f);
		case 6:
			return 1-((diff/6)*2.4f);
		case 7:
			return 1-((diff/6)*3.6f);
		case 8:
			return 1-((diff/6)*4.8f);
		case 9:
			return factor;
		case 10:
			return 1-((diff/6)*5);
		default:
			return 1;
		}
	}

	protected static float getTempSpecificDay(int day, int x, int z)
	{
		return getTemp(day+1, x, z);
	}

	public static float getHeightAdjustedTemp(int x, int y, int z)
	{
		float temp = getTemp(x, z);

		temp += getTemp(x+1, z);
		temp += getTemp(x-1, z);
		temp += getTemp(x, z+1);
		temp += getTemp(x, z-1);

		temp/= 5;

		if(y > 180)
			temp -= temp * (y-180)/90;

		return temp;
	}

	public static float adjustHeightToTemp(int y, float temp)
	{
		if(y > 180)
			temp -= temp * (y-180)/90;

		return temp;
	}

	public static float getHeightAdjustedTempSpecificDay(int day, int x, int y, int z)
	{
		float temp = getTempSpecificDay(day, x, z);

		if(y > 180)
			temp -= temp * (y-180)/90;

		return temp;
	}

	public static float getHeightAdjustedBioTemp(int day, int x, int y, int z)
	{
		float temp = getBioTemp(day, x, z);

		if(y > 180)
			temp -= temp * (y-180)/90;

		return temp;
	}

	public static float getMaxTemperature()
	{
		return 42;
	}

	public static float getBioTemperatureHeight(int x, int y, int z)
	{
		float temp = 0;
		for(int i = 0; i < 12; i++)
		{
			float t = getHeightAdjustedBioTemp(i*30, x, y, z);
			if(t < 0)
				t = 0;

			temp += t;
		}
		return temp/12;
	}

	public static float getBioTemperature(int x, int z)
	{
		float temp = 0;
		for(int i = 0; i < 24; i++)
		{
			float t = getBioTemp(i*15, x, z);
			if(t < 0)
				t = 0;

			temp += t;
		}
		return temp/24;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Provides the basic grass color based on the biome temperature and rainfall
	 */
	public static int getGrassColor(World world, int x, int y, int z)
	{
		float temp = (getTemp(x, z)/getMaxTemperature());

		float rain = (TFC_Climate.getRainfall(x, y, z) / 8000);

		double var1 = (double)Helper.clamp_float(temp, 0.0F, 1.0F);
		double var3 = (double)Helper.clamp_float(rain, 0.0F, 1.0F);

		return ColorizerGrassTFC.getGrassColor(var1, var3);
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Provides the basic foliage color based on the biome temperature and rainfall
	 */
	public static int getFoliageColor(World world, int x, int y, int z)
	{
		if(TFC_Time.currentMonth < 9)
		{
			float temp = (getTemp(x, z)+35)/(getMaxTemperature()+35);
			//float evt = (1 - (((TFCWorldChunkManager)world.provider.worldChunkMgr).getEVTLayerAt(x, z).floatdata1 / 16))*0.5f;
			float rain = (TFC_Climate.getRainfall(x, y, z) / 8000);

			double var1 = (double)Helper.clamp_float(temp, 0.0F, 1.0F);
			double var3 = (double)Helper.clamp_float(rain, 0.0F, 1.0F);
			return ColorizerFoliageTFC.getFoliageColor(var1, var3);
		}
		else
			return ColorizerFoliageTFC.getFoliageDead();
	}

	/**
	 * All Rainfall related code
	 */

	public static float getRainfall(int x, int y, int z)
	{
		return manager.getRainfallLayerAt(x, z).floatdata1;
	}

	public static float getTerrainAdjustedRainfall(int x, int y, int z)
	{
		float rain = manager.getRainfallLayerAt(x, z).floatdata1;
		ArrayList biomes = new ArrayList<TFCBiome>();
		biomes.add(TFCBiome.river);
		biomes.add(TFCBiome.ocean);
		biomes.add(TFCBiome.swampland);

		float rainModWest = 1;
		float rainModNorth = 1;
		float rainModSouth = 1;
		float rainModEast = 1;

		BiomeGenBase biome = null;


		for(int i = 0; i < 8; i++)
		{
			biome = worldObj.getBiomeGenForCoords((x-512)+(64*i), z);

			if(biome.biomeID == TFCBiome.Mountains.biomeID)
				rainModWest = 1 - (i * 0.0625f);
			else if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModWest = 1 + (i * 0.125f);

		}
		for(int i = 0; i < 8; i++)
		{
			biome =  worldObj.getBiomeGenForCoords(x, (z+512)-(64*i));

			if(biome.biomeID == TFCBiome.Mountains.biomeID)
				rainModSouth = 1 - (i * 0.0625f);
			else if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModSouth = 1 + (i * 0.125f);

		}
		for(int i = 0; i < 2; i++)
		{
			biome = worldObj.getBiomeGenForCoords(x, (z-128)+(64*i));

			if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModNorth +=  0.35f;

		}
		for(int i = 0; i < 2; i++)
		{
			biome = worldObj.getBiomeGenForCoords((x+128)-(64*i), z);

			if(biome.biomeID == TFCBiome.ocean.biomeID)
				rainModEast += 0.35f;

		}

		float addMoisture = 1;
		for(int i = -2; i <= 2 && addMoisture == 1; i++)
		{
			for(int k = -2; k <= 2 && addMoisture == 1; k++)
			{
				biome = worldObj.getBiomeGenForCoords(x+(i * 8), z+(k * 8));
				if(biome.biomeID == TFCBiome.ocean.biomeID || biome.biomeID == TFCBiome.river.biomeID || biome.biomeID == TFCBiome.swampland.biomeID)
					addMoisture = 2f;
			}
		}


		return rain*((rainModEast + rainModWest + rainModNorth + rainModSouth + addMoisture)/5);
	}

	public static int getTreeLayer(int x, int y, int z, int index)
	{
		return manager.getTreeLayerAt(x, z, index).data1;
	}
	
	public static int[] getRockLayer(int x, int y, int z, int index)
	{
		return new int[] {manager.getRockLayerAt(x, z, index).data1, manager.getRockLayerAt(x, z, index).data2};
	}

	public static int getMaxZPos()
	{
		return 20000;
	}
}
