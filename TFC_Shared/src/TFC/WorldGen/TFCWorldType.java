package TFC.WorldGen;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkProviderFlat;
import net.minecraft.src.ChunkProviderGenerate;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldChunkManagerHell;
import net.minecraft.src.WorldType;

public class TFCWorldType extends WorldType
{
	public static TFCWorldType DEFAULT;
	
	public TFCWorldType(int par1, String par2Str, int par3) 
	{
		super(par1, par2Str, par3);
	}
	@Override
	public WorldChunkManager getChunkManager(World world)
    {
        return new TFCWorldChunkManager(world);
    }

	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new TFCChunkProviderGenerate(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
    }

    @Override
    public int getMinimumSpawnHeight(World world)
    {
        return 145;
    }

    @Override
    public double getHorizon(World world)
    {
        return 144.0D;
    }

}
