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
        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator, TemplateManager manager, int chunkX, int chunkZ, Biome biome, T config)
        {
            ChunkPos chunkpos = new ChunkPos(chunkX, chunkZ);
            int x = chunkpos.getMinBlockX() + this.random.nextInt(16);
            int z = chunkpos.getMinBlockZ() + this.random.nextInt(16);
            int seaLevel = generator.getSeaLevel();
            int y = seaLevel + this.random.nextInt(generator.getGenDepth() - 2 - seaLevel);
            IBlockReader iblockreader = generator.getBaseColumn(x, z);

            for (BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z); y > seaLevel; --y)
            {
                BlockState blockstate = iblockreader.getBlockState(blockpos$mutable);
                blockpos$mutable.move(Direction.DOWN);
                BlockState blockstate1 = iblockreader.getBlockState(blockpos$mutable);
                if (blockstate.isAir() && (blockstate1.is(Blocks.SOUL_SAND) || blockstate1.isFaceSturdy(iblockreader, blockpos$mutable, Direction.UP)))
                    break;
            }

            if (y > seaLevel)
            {
                SoulCastlePieces.buildStructure(manager, new BlockPos(x, y, z), Rotation.values()[this.random.nextInt(Rotation.values().length)], this.pieces, this.random);
                this.calculateBoundingBox();
            }
        }

        @Override
        public void placeInChunk(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos)
        {
            super.placeInChunk(world, manager, generator, random, boundingBox, chunkPos);
            int minY = this.boundingBox.y0;

            for (int x = boundingBox.x0; x <= boundingBox.x1; x++)
            {
                for (int z = boundingBox.z0; z <= boundingBox.z1; z++)
                {
                    BlockPos blockpos = new BlockPos(x, minY, z);
                    if (!world.isEmptyBlock(blockpos) && this.boundingBox.isInside(blockpos))
                    {
                        boolean isAirBelow = false;
                        for (StructurePiece structurepiece : this.pieces)
                        {
                            if (structurepiece.getBoundingBox().isInside(blockpos))
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
                                if (!world.isEmptyBlock(pos) && !world.getBlockState(pos).getMaterial().isLiquid())
                                    break;
                                world.setBlock(pos, world.getBiome(pos).getGenerationSettings().getSurfaceBuilderConfig().getUnderMaterial(), Constants.BlockFlags.BLOCK_UPDATE);
                            }
                        }
                    }
                }
            }
        }
    }
}
