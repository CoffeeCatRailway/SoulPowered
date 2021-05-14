package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.Tier;
import coffeecatrailway.soulpowered.common.block.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.Tags;
import org.apache.logging.log4j.Logger;

import static coffeecatrailway.soulpowered.SoulPoweredMod.REGISTRATE;

/**
 * @author CoffeeCatRailway
 * Created: 9/11/2020
 */
public class SoulBlocks
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Blocks");

    /*
     * World Gen
     */
    public static final RegistryEntry<Block> COPPER_ORE = REGISTRATE.object("copper_ore").block(Block::new).initialProperties(Material.ROCK, Material.ROCK.getColor())
            .defaultLang().defaultLoot().defaultBlockstate().properties(prop -> prop.setRequiresTool().hardnessAndResistance(3f))
            .tag(SoulData.TagBlocks.ORES_COPPER).item().tag(SoulData.TagItems.ORES_COPPER).build().register();

    /*
     * Building Blocks
     */
    public static final RegistryEntry<Block> SOULIUM_BLOCK = REGISTRATE.object("soulium_block").block(Block::new).initialProperties(Material.IRON, MaterialColor.LIGHT_GRAY)
            .properties(prop -> prop.setRequiresTool().hardnessAndResistance(5f, 6f).sound(SoundType.METAL)).defaultLang().defaultLoot().defaultBlockstate()
            .recipe((ctx, provider) -> provider.square(DataIngredient.items(SoulItems.SOULIUM_INGOT), ctx::getEntry, false))
            .tag(Tags.Blocks.STORAGE_BLOCKS, BlockTags.BEACON_BASE_BLOCKS).item().tag(Tags.Items.STORAGE_BLOCKS).build().register();

    public static final RegistryEntry<Block> COPPER_BLOCK = REGISTRATE.object("copper_block").block(Block::new).initialProperties(Material.IRON, MaterialColor.ORANGE_TERRACOTTA)
            .properties(prop -> prop.setRequiresTool().hardnessAndResistance(5f, 6f).sound(SoundType.METAL)).defaultLang().defaultLoot().defaultBlockstate()
            .recipe((ctx, provider) -> provider.square(DataIngredient.tag(SoulData.TagItems.INGOTS_COPPER), ctx::getEntry, false))
            .tag(SoulData.TagBlocks.STORAGE_BLOCKS_COPPER, BlockTags.BEACON_BASE_BLOCKS).item().tag(SoulData.TagItems.STORAGE_BLOCKS_COPPER).build().register();

    /*
     * Machine Frames
     */
    public static final RegistryEntry<MachineFrameBlock> SIMPLE_MACHINE_FRAME = registerMachineFrame(MachineFrameBlock::new, "simple_machine_frame", "Simple Machine Frame", Tier.SIMPLE,
            (ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', ItemTags.LOGS).key('s', ItemTags.PLANKS)
                    .patternLine("isi").patternLine("s s").patternLine("isi").addCriterion("has_log", RegistrateRecipeProvider.hasItem(ItemTags.LOGS))
                    .addCriterion("has_planks", RegistrateRecipeProvider.hasItem(ItemTags.PLANKS)).build(provider), true, SoundType.WOOD);

    public static final RegistryEntry<MachineFrameBlock> NORMAL_MACHINE_FRAME = registerMachineFrame("normal_machine_frame", "Machine Frame", Tier.NORMAL,
            (ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Tags.Items.INGOTS_IRON).key('s', Tags.Items.STONE)
                    .patternLine("isi").patternLine("s s").patternLine("isi").addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                    .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON)).build(provider), false, SoundType.STONE);

    public static final RegistryEntry<MachineFrameBlock> SOULIUM_MACHINE_FRAME = registerMachineFrame("soulium_machine_frame", "Soulium Machine Frame", Tier.SOULIUM,
            (ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOULIUM_INGOT.get()).key('s', SoulData.TagItems.SOUL_BLOCKS)
                    .patternLine("isi").patternLine("s s").patternLine("isi").addCriterion("has_soul_sand", RegistrateRecipeProvider.hasItem(SoulData.TagItems.SOUL_BLOCKS))
                    .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get())).build(provider), false, SoundType.METAL);

    /*
     * Machines
     * Generators
     */
    public static final RegistryEntry<CoalGeneratorBlock> SIMPLE_COAL_GENERATOR = registerCoalGenerator(CoalGeneratorBlock::new, "simple_coal_generator", "Simple Coal Generator", Tier.SIMPLE, (ctx, provider) ->
            ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('p', ItemTags.PLANKS).key('m', SIMPLE_MACHINE_FRAME.get())
                    .key('f', Blocks.FURNACE).key('b', SoulItems.SIMPLE_BATTERY.get()).patternLine("pfp").patternLine("pbp").patternLine("pmp")
                    .addCriterion("has_planks", RegistrateRecipeProvider.hasItem(ItemTags.PLANKS))
                    .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SIMPLE_MACHINE_FRAME.get()))
                    .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SIMPLE_BATTERY.get())).build(provider), SoundType.WOOD);

    public static final RegistryEntry<CoalGeneratorBlock> NORMAL_COAL_GENERATOR = registerCoalGenerator("normal_coal_generator", "Normal Coal Generator", Tier.NORMAL, (ctx, provider) -> {
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('s', Tags.Items.STONE).key('m', NORMAL_MACHINE_FRAME.get())
                .key('f', Blocks.FURNACE).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("fsf").patternLine("sbs").patternLine("sms")
                .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider);
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('s', Tags.Items.STONE).key('m', NORMAL_MACHINE_FRAME.get())
                .key('c', SIMPLE_COAL_GENERATOR.get()).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("csc").patternLine("sbs").patternLine("sms")
                .addCriterion("has_stone", RegistrateRecipeProvider.hasItem(Tags.Items.STONE))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                .addCriterion("has_simple_coal_generator", RegistrateRecipeProvider.hasItem(SIMPLE_COAL_GENERATOR.get()))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider, SoulPoweredMod.getLocation("normal_coal_generator_from_simple_coal_generator"));
    }, SoundType.STONE);

    public static final RegistryEntry<SoulGeneratorBlock> SOUL_GENERATOR = registerMachine(REGISTRATE.object("soul_generator").block(SoulGeneratorBlock::new)
            .blockstate(sidedFurnaceModel(false)).defaultLang().defaultLoot().initialProperties(Tier.SOULIUM.getMaterial(), Tier.SOULIUM.getMaterialColor())
            .properties(prop -> Tier.SOULIUM.getProperties().apply(prop))
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOULIUM_INGOT.get()).key('m', SOULIUM_MACHINE_FRAME.get())
                    .key('f', Blocks.FURNACE).key('b', SoulItems.SOULIUM_BATTERY.get()).patternLine(" f ").patternLine("ibi").patternLine("imi")
                    .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                    .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SOULIUM_MACHINE_FRAME.get()))
                    .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_BATTERY.get())).build(provider)).simpleItem(), "Soul Generator");

    // Soul Boxes
    public static final RegistryEntry<SoulBoxBlock> SIMPLE_SOUL_BOX = registerSoulBox("simple_soul_box", "Simple Soul Box", Tier.SIMPLE, (ctx, provider) ->
            ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('p', ItemTags.PLANKS).key('f', SIMPLE_MACHINE_FRAME.get())
                    .key('c', SoulData.TagItems.INGOTS_COPPER).key('b', SoulItems.SIMPLE_BATTERY.get()).patternLine("pcp").patternLine("bbb").patternLine("pfp")
                    .addCriterion("has_planks", RegistrateRecipeProvider.hasItem(ItemTags.PLANKS))
                    .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SIMPLE_MACHINE_FRAME.get()))
                    .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SIMPLE_BATTERY.get())).build(provider), SoundType.WOOD);

    public static final RegistryEntry<SoulBoxBlock> NORMAL_SOUL_BOX = registerSoulBox("normal_soul_box", "Soul Box", Tier.NORMAL, (ctx, provider) -> {
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Tags.Items.INGOTS_IRON).key('f', NORMAL_MACHINE_FRAME.get())
                .key('c', SoulData.TagItems.INGOTS_COPPER).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("ici").patternLine("bbb").patternLine("ifi")
                .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider);
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Tags.Items.INGOTS_IRON).key('f', NORMAL_MACHINE_FRAME.get())
                .key('o', SIMPLE_SOUL_BOX.get()).key('b', SoulItems.SIMPLE_BATTERY.get()).patternLine("ibi").patternLine("bob").patternLine("ifi")
                .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                .addCriterion("has_simple_soul_box", RegistrateRecipeProvider.hasItem(SIMPLE_SOUL_BOX.get()))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SIMPLE_BATTERY.get())).build(provider, SoulPoweredMod.getLocation("normal_soul_box_from_simple_soul_box"));
    }, SoundType.STONE);

    public static final RegistryEntry<SoulBoxBlock> SOULIUM_SOUL_BOX = registerSoulBox("soulium_soul_box", "Soulium Soul Box", Tier.SOULIUM, (ctx, provider) -> {
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('s', SoulItems.SOULIUM_INGOT.get()).key('f', SOULIUM_MACHINE_FRAME.get())
                .key('c', SoulData.TagItems.INGOTS_COPPER).key('b', SoulItems.SOULIUM_BATTERY.get()).patternLine("scs").patternLine("bbb").patternLine("sfs")
                .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SOULIUM_MACHINE_FRAME.get()))
                .addCriterion("has_copper_ingot", RegistrateRecipeProvider.hasItem(SoulData.TagItems.INGOTS_COPPER))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_BATTERY.get())).build(provider);
        ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('s', SoulItems.SOULIUM_INGOT.get()).key('f', SOULIUM_MACHINE_FRAME.get())
                .key('o', NORMAL_SOUL_BOX.get()).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("sbs").patternLine("bob").patternLine("sfs")
                .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SOULIUM_MACHINE_FRAME.get()))
                .addCriterion("has_normal_soul_box", RegistrateRecipeProvider.hasItem(NORMAL_SOUL_BOX.get()))
                .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider, SoulPoweredMod.getLocation("soulium_soul_box_from_normal_soul_box"));
    }, SoundType.METAL);

    // Alloy Smelters
    public static final RegistryEntry<AlloySmelterBlock> SIMPLE_ALLOY_SMELTER = registerAlloySmelter("simple_alloy_smelter", "Simple Alloy Smelter", Tier.SIMPLE,
            (ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', ItemTags.PLANKS).key('m', SIMPLE_MACHINE_FRAME.get())
                    .key('f', Blocks.FURNACE).key('b', SoulItems.SIMPLE_BATTERY.get()).patternLine("fff").patternLine("ibi").patternLine("imi")
                    .addCriterion("has_planks", RegistrateRecipeProvider.hasItem(ItemTags.PLANKS))
                    .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SIMPLE_MACHINE_FRAME.get()))
                    .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                    .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SIMPLE_BATTERY.get())).build(provider), SoundType.WOOD);

    public static final RegistryEntry<AlloySmelterBlock> NORMAL_ALLOY_SMELTER = registerAlloySmelter("normal_alloy_smelter", "Alloy Smelter", Tier.NORMAL,
            (ctx, provider) -> {
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Tags.Items.INGOTS_IRON).key('m', NORMAL_MACHINE_FRAME.get())
                        .key('f', Blocks.FURNACE).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("fff").patternLine("ibi").patternLine("imi")
                        .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                        .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                        .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                        .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Tags.Items.INGOTS_IRON).key('m', NORMAL_MACHINE_FRAME.get())
                        .key('a', SIMPLE_ALLOY_SMELTER.get()).key('b', SoulItems.SIMPLE_BATTERY.get()).patternLine("a a").patternLine("ibi").patternLine("imi")
                        .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Tags.Items.INGOTS_IRON))
                        .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(NORMAL_MACHINE_FRAME.get()))
                        .addCriterion("has_simple_alloy_smelter", RegistrateRecipeProvider.hasItem(SIMPLE_ALLOY_SMELTER.get()))
                        .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SIMPLE_BATTERY.get())).build(provider, SoulPoweredMod.getLocation("normal_alloy_smelter_from_simple_alloy_smelter"));
            }, SoundType.STONE);

    public static final RegistryEntry<AlloySmelterBlock> SOULIUM_ALLOY_SMELTER = registerAlloySmelter("soulium_alloy_smelter", "Soulium Alloy Smelter", Tier.SOULIUM,
            (ctx, provider) -> {
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOULIUM_INGOT.get()).key('m', SOULIUM_MACHINE_FRAME.get())
                        .key('f', Blocks.FURNACE).key('b', SoulItems.SOULIUM_BATTERY.get()).patternLine("fff").patternLine("ibi").patternLine("imi")
                        .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                        .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SOULIUM_MACHINE_FRAME.get()))
                        .addCriterion("has_furnace", RegistrateRecipeProvider.hasItem(Blocks.FURNACE))
                        .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_BATTERY.get())).build(provider);
                ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', SoulItems.SOULIUM_INGOT.get()).key('m', SOULIUM_MACHINE_FRAME.get())
                        .key('a', NORMAL_ALLOY_SMELTER.get()).key('b', SoulItems.NORMAL_BATTERY.get()).patternLine("a a").patternLine("ibi").patternLine("imi")
                        .addCriterion("has_soul_metal", RegistrateRecipeProvider.hasItem(SoulItems.SOULIUM_INGOT.get()))
                        .addCriterion("has_machine_frame", RegistrateRecipeProvider.hasItem(SOULIUM_MACHINE_FRAME.get()))
                        .addCriterion("has_simple_alloy_smelter", RegistrateRecipeProvider.hasItem(NORMAL_ALLOY_SMELTER.get()))
                        .addCriterion("has_battery", RegistrateRecipeProvider.hasItem(SoulItems.NORMAL_BATTERY.get())).build(provider, SoulPoweredMod.getLocation("soulium_alloy_smelter_from_normal_alloy_smelter"));
            }, SoundType.METAL);

    /*
     * Helper Methods
     */
    private static RegistryEntry<MachineFrameBlock> registerMachineFrame(String id, String name, Tier tier, NonNullBiConsumer<DataGenContext<Block, MachineFrameBlock>, RegistrateRecipeProvider> recipe, boolean hasEndTexure, SoundType soundType)
    {
        return registerMachineFrame(MachineFrameBlock::new, id, name, tier, recipe, hasEndTexure, soundType);
    }

    private static <B extends MachineFrameBlock> RegistryEntry<B> registerMachineFrame(NonNullFunction<AbstractBlock.Properties, B> factory, String id, String name, Tier tier, NonNullBiConsumer<DataGenContext<Block, B>, RegistrateRecipeProvider> recipe, boolean hasEndTexure, SoundType soundType)
    {
        return REGISTRATE.object(id).block(factory).initialProperties(tier.getMaterial(), tier.getMaterialColor()).lang(name).defaultLoot()
                .properties(prop -> tier.getProperties().apply(prop))
                .blockstate((ctx, provider) -> {
                    BlockModelBuilder builder = provider.models().withExistingParent(ctx.getName(), SoulPoweredMod.getLocation("block/machine_frame_template"));
                    if (hasEndTexure)
                        builder = builder.texture("end", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_end"))
                                .texture("side", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_side"));
                    else
                        builder = builder.texture("end", SoulPoweredMod.getLocation("block/" + ctx.getName()))
                                .texture("side", SoulPoweredMod.getLocation("block/" + ctx.getName()));

                    provider.axisBlock(ctx.getEntry(), builder, builder);
                }).recipe(recipe).simpleItem().register();
    }

    private static RegistryEntry<CoalGeneratorBlock> registerCoalGenerator(String id, String name, Tier tier, NonNullBiConsumer<DataGenContext<Block, CoalGeneratorBlock>, RegistrateRecipeProvider> recipe, SoundType soundType)
    {
        return registerMachine(REGISTRATE.object(id).block(prop -> new CoalGeneratorBlock(prop, tier)).initialProperties(tier.getMaterial(), tier.getMaterialColor()).lang(name).defaultLoot()
                .properties(prop -> tier.getProperties().apply(prop)).blockstate(sidedFurnaceModel(true)).recipe(recipe).simpleItem(), name);
    }

    private static RegistryEntry<SoulBoxBlock> registerSoulBox(String id, String name, Tier tier, NonNullBiConsumer<DataGenContext<Block, SoulBoxBlock>, RegistrateRecipeProvider> recipe, SoundType soundType)
    {
        return registerMachine(REGISTRATE.object(id).block(prop -> new SoulBoxBlock(prop, tier)).initialProperties(tier.getMaterial(), tier.getMaterialColor()).lang(name).defaultLoot()
                .properties(prop -> tier.getProperties().apply(prop)).blockstate((ctx, provider) -> {
                    ModelFile modelOff = provider.models().withExistingParent(ctx.getName(), SoulPoweredMod.getLocation("block/soul_box_template"))
                            .texture("side", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_side"))
                            .texture("top", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_top"));
                    modelOff.assertExistence();

                    ModelFile modelOn = provider.models().withExistingParent(ctx.getName() + "_on", SoulPoweredMod.getLocation("block/soul_box_template"))
                            .texture("side", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_side_on"))
                            .texture("top", SoulPoweredMod.getLocation("block/" + ctx.getName() + "_top"));
                    modelOn.assertExistence();

                    addModels(provider.getVariantBuilder(ctx.getEntry()), modelOn, modelOff);
                }).recipe(recipe).simpleItem(), name);
    }

    private static RegistryEntry<AlloySmelterBlock> registerAlloySmelter(String id, String name, Tier tier, NonNullBiConsumer<DataGenContext<Block, AlloySmelterBlock>, RegistrateRecipeProvider> recipe, SoundType soundType)
    {
        return registerMachine(REGISTRATE.object(id).block(prop -> new AlloySmelterBlock(prop, tier)).initialProperties(tier.getMaterial(), tier.getMaterialColor())
                .properties(prop -> tier.getProperties().apply(prop)).blockstate(sidedFurnaceModel(true)).lang(name).defaultLoot()
                .recipe(recipe).simpleItem(), name);
    }

    private static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> sidedFurnaceModel(boolean hasTopTexture)
    {
        return (ctx, provider) -> {
            ResourceLocation side = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_side");
            ResourceLocation top = hasTopTexture ? SoulPoweredMod.getLocation("block/" + ctx.getName() + "_top") : side;
            ResourceLocation front = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_front");
            ResourceLocation front_on = SoulPoweredMod.getLocation("block/" + ctx.getName() + "_front_on");

            ModelFile modelOff = provider.models().cube(ctx.getName(), top, top, front, side, side, side).texture("particle", side);
            ModelFile modelOn = provider.models().cube(ctx.getName() + "_on", top, top, front_on, side, side, side).texture("particle", side);

            modelOff.assertExistence();
            modelOn.assertExistence();

            addModels(provider.getVariantBuilder(ctx.getEntry()), modelOn, modelOff);
        };
    }

    private static void addModels(VariantBlockStateBuilder builder, ModelFile modelOn, ModelFile modelOff)
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
