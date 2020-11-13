package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulData;
import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.block.SoulGeneratorBlock;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
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

    public static final RegistryEntry<SoulGeneratorBlock> SOUL_GENERATOR = REGISTRATE.object("soul_generator").block(SoulGeneratorBlock::new).addLayer(() -> RenderType::getCutoutMipped)
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
            .properties(prop -> prop.setRequiresTool().hardnessAndResistance(3.5f).setLightLevel(getLightValueLit(13)))
            .recipe((ctx, provider) -> ShapedRecipeBuilder.shapedRecipe(ctx.getEntry()).key('i', Items.IRON_INGOT).key('s', SoulData.TagItems.SOUL_BLOCKS)
                    .key('b', SoulItems.SOUL_BOTTLE.get()).patternLine("isi").patternLine("sbs").patternLine("isi")
                    .addCriterion("has_soul_sand", RegistrateRecipeProvider.hasItem(SoulData.TagItems.SOUL_BLOCKS))
                    .addCriterion("has_iron", RegistrateRecipeProvider.hasItem(Items.IRON_INGOT)).build(provider)).simpleItem().register();

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
    {
        return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
    }

    public static void load()
    {
        LOGGER.debug("Loaded");
    }
}
