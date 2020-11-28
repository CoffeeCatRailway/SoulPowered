package coffeecatrailway.soulpowered.common.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public class SoulCastleStructure<T extends NoFeatureConfig> extends Structure<T>
{
    public SoulCastleStructure(Codec<T> codec)
    {
        super(codec);
    }

    @Override
    public IStartFactory<T> getStartFactory()
    {
        return SoulCastleStructure.Start::new;
    }

    public static class Start<T extends NoFeatureConfig> extends StructureStart<T>
    {
        public Start(Structure<T> structure, int chunkX, int chunkZ, MutableBoundingBox boundingBox, int references, long seed)
        {
            super(structure, chunkX, chunkZ, boundingBox, references, seed);
        }

        @Override
        public void func_230364_a_(DynamicRegistries registries, ChunkGenerator generator, TemplateManager manager, int chunkX, int chunkZ, Biome biome, T config)
        {
            ChunkPos chunkpos = new ChunkPos(chunkX, chunkZ);
            int x = chunkpos.getXStart() + this.rand.nextInt(16);
            int z = chunkpos.getZStart() + this.rand.nextInt(16);
            int seaLevel = generator.getSeaLevel();
            int y = seaLevel + this.rand.nextInt(generator.getMaxBuildHeight() - 2 - seaLevel);
            IBlockReader iblockreader = generator.func_230348_a_(x, z);

            for (BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z); y > seaLevel; --y)
            {
                BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
                blockpos$mutable.move(Direction.DOWN);
                BlockState blockstate1 = iblockreader.getBlockState(blockpos$mutable);
                if (blockstate.isAir() && (blockstate1.isIn(Blocks.SOUL_SAND) || blockstate1.isSolidSide(iblockreader, blockpos$mutable, Direction.UP)))
                    break;
            }

            if (y > seaLevel)
            {
                SoulCastlePieces.buildStructure(manager, new BlockPos(x, y, z), Rotation.values()[this.rand.nextInt(Rotation.values().length)], this.components, this.rand);
                this.recalculateStructureSize();
            }
        }

        @Override
        public void func_230366_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos)
        {
            super.func_230366_a_(world, manager, generator, random, boundingBox, chunkPos);
            int minY = this.bounds.minY;

            for (int x = boundingBox.minX; x <= boundingBox.maxX; x++)
            {
                for (int z = boundingBox.minZ; z <= boundingBox.maxZ; z++)
                {
                    BlockPos blockpos = new BlockPos(x, minY, z);
                    if (!world.isAirBlock(blockpos) && this.bounds.isVecInside(blockpos))
                    {
                        boolean isAirBelow = false;
                        for (StructurePiece structurepiece : this.components)
                        {
                            if (structurepiece.getBoundingBox().isVecInside(blockpos))
                            {
                                isAirBelow = true;
                                break;
                            }
                        }

                        if (isAirBelow)
                        {
                            for (int lowY = minY - 1; lowY > 1; lowY--)
                            {
                                BlockPos pos = new BlockPos(x, lowY, z);
                                if (!world.isAirBlock(pos) && !world.getBlockState(pos).getMaterial().isLiquid())
                                    break;
                                world.setBlockState(pos, world.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getUnder(), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                        }
                    }
                }
            }
        }
    }
}
