package coffeecatrailway.soulpowered.common.world.gen.structure;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.registry.OtherRegistries;
import coffeecatrailway.soulpowered.registry.SoulWorldGen;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.util.Constants;

import java.util.List;
import java.util.Random;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public class SoulCastlePieces
{
    private static final ResourceLocation INTACT = SoulPoweredMod.getLocation("soul_castle/intact");
    private static final ResourceLocation PARTLY_DESTROYED = SoulPoweredMod.getLocation("soul_castle/partly_destroyed");
    private static final ResourceLocation DESTROYED = SoulPoweredMod.getLocation("soul_castle/destroyed");

    private static final ResourceLocation[] PIECES = new ResourceLocation[]{INTACT, PARTLY_DESTROYED, DESTROYED};

    private static final BlockPos CENTER = new BlockPos(5, 3, 5);

    public static void buildStructure(TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, Random random)
    {
        pieces.add(new SoulCastlePieces.Piece(manager, PIECES[random.nextInt(PIECES.length)], pos, rotation));
    }

    public static class Piece extends TemplateStructurePiece
    {
        private final ResourceLocation pieceLocation;
        private final Rotation rotation;

        public Piece(TemplateManager manager, ResourceLocation pieceLocation, BlockPos pos, Rotation rotation)
        {
            super(SoulWorldGen.StructurePieces.SOUL_CASTLE, 0);
            this.pieceLocation = pieceLocation;
            this.rotation = rotation;
            this.templatePosition = pos;
            this.loadTemplate(manager);
        }

        public Piece(TemplateManager manager, CompoundNBT nbt)
        {
            super(SoulWorldGen.StructurePieces.SOUL_CASTLE, nbt);
            this.pieceLocation = new ResourceLocation(nbt.getString("Template"));
            this.rotation = Rotation.valueOf(nbt.getString("Rotation"));
            this.loadTemplate(manager);
        }

        private void loadTemplate(TemplateManager manager)
        {
            Template template = manager.get(this.pieceLocation);
            PlacementSettings settings = new PlacementSettings().setRotation(this.rotation).setMirror(Mirror.NONE).setRotationPivot(CENTER).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_AND_AIR);
            this.setup(template, this.templatePosition, settings);
        }

        @Override
        protected void addAdditionalSaveData(CompoundNBT nbt)
        {
            super.addAdditionalSaveData(nbt);
            nbt.putString("Template", this.pieceLocation.toString());
            nbt.putString("Rotation", this.rotation.name());
        }

        @Override
        protected void handleDataMarker(String name, BlockPos pos, IServerWorld world, Random random, MutableBoundingBox boundingBox)
        {
            if ("Chest".equals(name))
            {
                world.setBlock(pos, Blocks.CHEST.defaultBlockState(), Constants.BlockFlags.DEFAULT);
                TileEntity tileentity = world.getBlockEntity(pos);
                if (tileentity instanceof ChestTileEntity)
                    ((ChestTileEntity) tileentity).setLootTable(OtherRegistries.CHESTS_SOUL_CASTLE, random.nextLong());
            }
        }

        @Override
        public boolean postProcess(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox boundingBox, ChunkPos chunkpos, BlockPos pos)
        {
            boundingBox.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            return super.postProcess(world, manager, generator, random, boundingBox, chunkpos, pos);
        }
    }
}
