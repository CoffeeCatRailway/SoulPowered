package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.block.MachineFrameBlock;
import coffeecatrailway.soulpowered.common.block.SoulBoxBlock;
import coffeecatrailway.soulpowered.common.block.SoulGeneratorBlock;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.logging.log4j.Logger;

import java.util.function.ToIntFunction;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 9/11/2020
 */
public class SoulBlocks
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Blocks");

    public static final RegistryEntry<Block> SOUL_METAL_BLOCK = REGISTRATE.object("soul_metal_block").block(Block::new).initialProperties(Material.IRON, MaterialColor.LIGHT_GRAY)
            .properties(prop -> prop.setRequiresTool().hardnessAndResistance(5f, 6f).sound(SoundType.METAL)).defaultLang().defaultLoot().defaultBlockstate()
            .recipe((ctx, provider) -> provider.square(DataIngredient.items(SoulItems.SOUL_METAL_INGOT), ctx::getEntry, false)).tag(BlockTags.BEACON_BASE_BLOCKS).simpleItem().register();

    public static final RegistryEntry<MachineFrameBlock> MACHINE_FRAME = REGISTRATE.object("machine_frame").block(MachineFrameBlock::new).initialProperties(Material.IRON, MaterialColor.LIGHT_GRAY)
            .defaultLang().defaultLoot().properties(prop -> prop.setRequiresTool().hardnessAndResistance(2.5f).sound(SoundType.METAL))
            .blockstate((ctx, provider) -> provider.simpleBlock(ctx.getEntry(), provider.models().getExistingFile(SoulPoweredMod.getLocation("block/" + ctx.getName()))))
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOUL_METAL_INGOT.get()).key('s', SoulData.TagItems.SOUL_BLOCKS)
                    .patternLine("isi").patternLine("s s").patternLine("isi").addCriterion("has_soul_sand", RegistrateRecipeProvider.hasItem(SoulData.TagItems.SOUL_BLOCKS))
                    .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOUL_METAL_INGOT.get())).build(provider)).simpleItem().register();

    // Machines
    public static final RegistryEntry<SoulGeneratorBlock> SOUL_GENERATOR = registerMachine(REGISTRATE.object("soul_generator").block(SoulGeneratorBlock::new)
            .blockstate((ctx, provider) -> {
                ResourceLocation side = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_side");
                ResourceLocation front = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_front");
                ResourceLocation front_on = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_front_on");

                ModelFile model_off = provider.models().cube(ctx.getName(), side, side, front, side, side, side).texture("particle", side);
                ModelFile model_on = provider.models().cube(ctx.getName() + "_on", side, side, front_on, side, side, side).texture("particle", side);

                model_off.assertExistence();
                model_on.assertExistence();

                provider.getVariantBuilder(ctx.getEntry()) // North
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, false)
                        .modelForState().modelFile(model_off).addModel()
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.NORTH).with(AbstractFurnaceBlock.LIT, true)
                        .modelForState().modelFile(model_on).addModel()

                        // South
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, false)
                        .modelForState().modelFile(model_off).rotationY(180).addModel()
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.SOUTH).with(AbstractFurnaceBlock.LIT, true)
                        .modelForState().modelFile(model_on).rotationY(180).addModel()

                        // East
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, false)
                        .modelForState().modelFile(model_off).rotationY(90).addModel()
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.EAST).with(AbstractFurnaceBlock.LIT, true)
                        .modelForState().modelFile(model_on).rotationY(90).addModel()

                        // West
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, false)
                        .modelForState().modelFile(model_off).rotationY(270).addModel()
                        .partialState().with(AbstractFurnaceBlock.FACING, Direction.WEST).with(AbstractFurnaceBlock.LIT, true)
                        .modelForState().modelFile(model_on).rotationY(270).addModel();
            }).defaultLang().defaultLoot().initialProperties(Material.IRON, MaterialColor.LIGHT_GRAY)
            .properties(prop -> prop.setRequiresTool().hardnessAndResistance(3.5f).setLightLevel(getLightValueLit(13)).sound(SoundType.METAL))
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOUL_METAL_INGOT.get()).key('m', MACHINE_FRAME.get())
                    .key('f', Blocks.FURNACE).patternLine("ifi").patternLine("imi")
                    .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOUL_METAL_INGOT.get()))
                    .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(MACHINE_FRAME.get()))
                    .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE)).build(provider)).simpleItem(), "Soul Generator");

    public static final RegistryEntry<SoulBoxBlock> SOUL_BOX = registerMachine(REGISTRATE.object("soul_box").block(SoulBoxBlock::new).initialProperties(Material.IRON, MaterialColor.LIGHT_GRAY)
            .defaultLang().defaultLoot().properties(prop -> prop.setRequiresTool().hardnessAndResistance(6f, 20f).sound(SoundType.METAL))
            .blockstate((ctx, provider) -> provider.getVariantBuilder(ctx.getEntry())
                    .partialState().with(SoulBoxBlock.ON, false).modelForState().modelFile(provider.models().getExistingFile(SoulPoweredMod.getLocation("block/soul_box"))).addModel()
                    .partialState().with(SoulBoxBlock.ON, true).modelForState().modelFile(provider.models().getExistingFile(SoulPoweredMod.getLocation("block/soul_box_on"))).addModel())
            .simpleItem(), "Soul Box");

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
    {
        return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
    }

    private static <T extends Block> RegistryEntry<T> registerMachine(BlockBuilder<T, Registrate> builder, String name)
    {
        SoulData.Lang.EXTRA_LANGS.put("container." + SoulPoweredMod.MOD_ID + "." + builder.getName(), name);
        return builder.lang(name).register();
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
