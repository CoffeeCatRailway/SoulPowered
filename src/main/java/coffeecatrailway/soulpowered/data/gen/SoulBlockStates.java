package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.block.AbstractMachineBlock;
import coffeecatrailway.soulpowered.common.block.MachineFrameBlock;
import coffeecatrailway.soulpowered.common.block.SoulBoxBlock;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.io.IOException;

/**
 * @author CoffeeCatRailway
 * Created: 7/04/2021
 */
public class SoulBlockStates extends BlockStateProvider
{
    public SoulBlockStates(DataGenerator gen, ExistingFileHelper existingFileHelper)
    {
        super(gen, SoulMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        this.simpleBlock(SoulBlocks.SOULIUM_BLOCK.get());
        this.simpleBlockItem(SoulBlocks.SOULIUM_BLOCK.get(), this.cubeAll(SoulBlocks.SOULIUM_BLOCK.get()));

        this.machineFrame(SoulBlocks.SIMPLE_MACHINE_FRAME, true);
        this.machineFrame(SoulBlocks.NORMAL_MACHINE_FRAME, false);
        this.machineFrame(SoulBlocks.SOULIUM_MACHINE_FRAME, false);

        this.sidedFurnaceModel(SoulBlocks.SIMPLE_COAL_GENERATOR, true);
        this.sidedFurnaceModel(SoulBlocks.NORMAL_COAL_GENERATOR, true);
        this.sidedFurnaceModel(SoulBlocks.SOUL_GENERATOR, false);

        this.soulBox(SoulBlocks.SIMPLE_SOUL_BOX);
        this.soulBox(SoulBlocks.NORMAL_SOUL_BOX);
        this.soulBox(SoulBlocks.SOULIUM_SOUL_BOX);

        this.sidedFurnaceModel(SoulBlocks.SIMPLE_ALLOY_SMELTER, true);
        this.sidedFurnaceModel(SoulBlocks.NORMAL_ALLOY_SMELTER, true);
        this.sidedFurnaceModel(SoulBlocks.SOULIUM_ALLOY_SMELTER, true);
    }

    private void machineFrame(RegistryObject<MachineFrameBlock> block, boolean hasEndTexure)
    {
        BlockModelBuilder builder = this.models().withExistingParent(block.getId().getPath(), SoulMod.getLocation("block/machine_frame_template"));
        if (hasEndTexure)
            builder = builder.texture("end", SoulMod.getLocation("block/" + block.getId().getPath() + "_end"))
                    .texture("side", SoulMod.getLocation("block/" + block.getId().getPath() + "_side"));
        else
            builder = builder.texture("end", SoulMod.getLocation("block/" + block.getId().getPath()))
                    .texture("side", SoulMod.getLocation("block/" + block.getId().getPath()));

        this.axisBlock(block.get(), builder, builder);
        this.simpleBlockItem(block.get(), builder);
    }

    private void soulBox(RegistryObject<SoulBoxBlock> block)
    {
        ModelFile modelOff = this.models().withExistingParent(block.getId().getPath(), SoulMod.getLocation("block/soul_box_template"))
                .texture("side", SoulMod.getLocation("block/" + block.getId().getPath() + "_side"))
                .texture("top", SoulMod.getLocation("block/" + block.getId().getPath() + "_top"));
        modelOff.assertExistence();

        ModelFile modelOn = this.models().withExistingParent(block.getId().getPath() + "_on", SoulMod.getLocation("block/soul_box_template"))
                .texture("side", SoulMod.getLocation("block/" + block.getId().getPath() + "_side_on"))
                .texture("top", SoulMod.getLocation("block/" + block.getId().getPath() + "_top"));
        modelOn.assertExistence();

        addModels(this.getVariantBuilder(block.get()), modelOn, modelOff);
        this.simpleBlockItem(block.get(), modelOff);
    }

    private <T extends AbstractMachineBlock> void sidedFurnaceModel(RegistryObject<T> block, boolean hasTopTexture)
    {
        ResourceLocation side = SoulMod.getLocation("block/" + block.getId().getPath() + "_side");
        ResourceLocation top = hasTopTexture ? SoulMod.getLocation("block/" + block.getId().getPath() + "_top") : side;
        ResourceLocation front = SoulMod.getLocation("block/" + block.getId().getPath() + "_front");
        ResourceLocation front_on = SoulMod.getLocation("block/" + block.getId().getPath() + "_front_on");

        ModelFile modelOff = this.models().cube(block.getId().getPath(), top, top, front, side, side, side).texture("particle", side);
        ModelFile modelOn = this.models().cube(block.getId().getPath() + "_on", top, top, front_on, side, side, side).texture("particle", side);

        modelOff.assertExistence();
        modelOn.assertExistence();

        addModels(this.getVariantBuilder(block.get()), modelOn, modelOff);
        this.simpleBlockItem(block.get(), modelOff);
    }

    private void addModels(VariantBlockStateBuilder builder, ModelFile modelOn, ModelFile modelOff)
    {
        // North
        builder.partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, false)
                .modelForState().modelFile(modelOff).addModel()
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, true)
                .modelForState().modelFile(modelOn).addModel()

                // South
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, false)
                .modelForState().modelFile(modelOff).rotationY(180).addModel()
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, true)
                .modelForState().modelFile(modelOn).rotationY(180).addModel()

                // East
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, false)
                .modelForState().modelFile(modelOff).rotationY(90).addModel()
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, true)
                .modelForState().modelFile(modelOn).rotationY(90).addModel()

                // West
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, false)
                .modelForState().modelFile(modelOff).rotationY(270).addModel()
                .partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, true)
                .modelForState().modelFile(modelOn).rotationY(270).addModel();
    }
}
