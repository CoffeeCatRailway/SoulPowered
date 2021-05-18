package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.block.MachineFrameBlock;
import coffeecatrailway.soulpowered.common.item.BatteryItem;
import coffeecatrailway.soulpowered.common.item.SoulAmuletItem;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import coffeecatrailway.soulpowered.registry.SoulItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 17/03/2021
 */
@SuppressWarnings("unchecked")
public class SoulRecipeGen extends RecipeProvider
{
    public SoulRecipeGen(DataGenerator generator)
    {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer)
    {
        // Items
        this.battery(consumer, SoulItems.SIMPLE_BATTERY, () -> ItemTags.PLANKS, () -> Tags.Items.RODS_WOODEN);
        this.battery(consumer, SoulItems.NORMAL_BATTERY, () -> Tags.Items.INGOTS_IRON, () -> Tags.Items.STONE);
        ShapedRecipeBuilder.shaped(SoulItems.NORMAL_BATTERY.get()).define('s', Tags.Items.STONE).define('o', SoulItems.SIMPLE_BATTERY.get()).define('r', Tags.Items.DUSTS_REDSTONE)
                .pattern(" o ").pattern("srs").pattern(" o ")
                .unlockedBy("has_stone", has(Tags.Items.STONE)).unlockedBy("has_simple_battery", has(SoulItems.SIMPLE_BATTERY.get()))
                .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)).save(consumer, SoulMod.getLocation("normal_battery_from_simple_battery"));
        this.battery(consumer, SoulItems.SOULIUM_BATTERY, SoulItems.SOULIUM_INGOT, null);
        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_BATTERY.get()).define('s', SoulItems.SOULIUM_INGOT.get()).define('o', SoulItems.NORMAL_BATTERY.get()).define('b', SoulItems.SOUL_BOTTLE.get())
                .pattern(" o ").pattern("sbs").pattern(" o ")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_normal_battery", has(SoulItems.NORMAL_BATTERY.get()))
                .unlockedBy("has_soul_bottle", has(SoulItems.SOUL_BOTTLE.get())).save(consumer, SoulMod.getLocation("soulium_battery_from_normal_battery"));

        ShapelessRecipeBuilder.shapeless(SoulItems.SOULIUM_INGOT.get(), 9).requires(SoulBlocks.SOULIUM_BLOCK.get()).unlockedBy("has_soulium_block", has(SoulBlocks.SOULIUM_BLOCK.get())).save(consumer);

        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_SWORD.get()).pattern("i").pattern("i").pattern("s").define('i', SoulItems.SOULIUM_INGOT.get()).define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(consumer);
        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_AXE.get()).pattern("ii").pattern("is").pattern(" s").define('i', SoulItems.SOULIUM_INGOT.get()).define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(consumer);
        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_PICKAXE.get()).pattern("iii").pattern(" s ").pattern(" s ").define('i', SoulItems.SOULIUM_INGOT.get()).define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(consumer);
        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_SHOVEL.get()).pattern("i").pattern("s").pattern("s").define('i', SoulItems.SOULIUM_INGOT.get()).define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(consumer);
        ShapedRecipeBuilder.shaped(SoulItems.SOULIUM_HOE.get()).pattern("ii").pattern(" s").pattern(" s").define('i', SoulItems.SOULIUM_INGOT.get()).define('s', Tags.Items.RODS_WOODEN)
                .unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN)).save(consumer);

        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_SWORD.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_SWORD.get())
                .unlocks("has_sword", has(SoulItems.SOULIUM_SWORD.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulItems.POWERED_SOULIUM_SWORD.getId());
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_AXE.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_AXE.get())
                .unlocks("has_axe", has(SoulItems.SOULIUM_AXE.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulItems.POWERED_SOULIUM_AXE.getId());
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_PICKAXE.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_PICKAXE.get())
                .unlocks("has_pickaxe", has(SoulItems.SOULIUM_PICKAXE.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulItems.POWERED_SOULIUM_PICKAXE.getId());
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_SHOVEL.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_SHOVEL.get())
                .unlocks("has_shovel", has(SoulItems.SOULIUM_SHOVEL.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulItems.POWERED_SOULIUM_SHOVEL.getId());
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_HOE.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_HOE.get())
                .unlocks("has_hoe", has(SoulItems.SOULIUM_HOE.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulItems.POWERED_SOULIUM_HOE.getId());

        this.soulAmulet(consumer, SoulItems.GOLD_SOUL_AMULET, () -> Items.GOLD_INGOT, null);
        this.soulAmulet(consumer, SoulItems.COPPER_SOUL_AMULET, SoulItems.COPPER_INGOT::get, null);
        this.soulAmulet(consumer, SoulItems.IRON_SOUL_AMULET, () -> Items.IRON_INGOT, null);
        this.soulAmulet(consumer, SoulItems.DIAMOND_SOUL_AMULET, () -> Items.DIAMOND, null);
        this.soulAmulet(consumer, SoulItems.SOULIUM_SOUL_AMULET, SoulItems.SOULIUM_INGOT::get, null);
        this.soulAmulet(consumer, SoulItems.POWERED_SOULIUM_SOUL_AMULET, SoulItems.SOULIUM_INGOT::get, SoulItems.SOULIUM_BATTERY::get);
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.SOULIUM_SOUL_AMULET.get()), Ingredient.of(SoulItems.SOULIUM_BATTERY.get()), SoulItems.POWERED_SOULIUM_SOUL_AMULET.get())
                .unlocks("has_soulium_soul_amulet", has(SoulItems.SOULIUM_SOUL_AMULET.get())).unlocks("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer, SoulMod.getLocation(SoulItems.POWERED_SOULIUM_SOUL_AMULET.getId().getPath() + "_smithing"));
        this.soulAmulet(consumer, SoulItems.NETHERITE_SOUL_AMULET, () -> Items.NETHERITE_INGOT, null);
        SmithingRecipeBuilder.smithing(Ingredient.of(SoulItems.DIAMOND_SOUL_AMULET.get()), Ingredient.of(Tags.Items.INGOTS_NETHERITE), SoulItems.NETHERITE_SOUL_AMULET.get())
                .unlocks("has_diamond_soul_amulet", has(SoulItems.DIAMOND_SOUL_AMULET.get())).unlocks("has_netherite", has(Tags.Items.INGOTS_NETHERITE)).save(consumer, SoulMod.getLocation(SoulItems.NETHERITE_SOUL_AMULET.getId().getPath() + "_smithing"));

        ShapedRecipeBuilder.shaped(SoulItems.SOUL_SHIELD.get()).define('s', Items.SHIELD).define('b', SoulItems.SOUL_BOTTLE.get()).define('i', SoulItems.SOULIUM_INGOT.get()).pattern("s").pattern("b").pattern("i")
                .unlockedBy("has_shield", has(Items.SHIELD)).unlockedBy("has_soul_bottle", has(SoulItems.SOUL_BOTTLE.get())).unlockedBy("has_ingot", has(SoulItems.SOULIUM_INGOT.get())).save(consumer);

        // Blocks
        ShapedRecipeBuilder.shaped(SoulBlocks.SOULIUM_BLOCK.get()).define('i', SoulItems.SOULIUM_INGOT.get()).pattern("iii").pattern("iii").pattern("iii").unlockedBy("has_soulium_ingot", has(SoulItems.SOULIUM_INGOT.get())).save(consumer);

        this.machineFrame(consumer, SoulBlocks.SIMPLE_MACHINE_FRAME, ItemTags.PLANKS, () -> ItemTags.LOGS);
        this.machineFrame(consumer, SoulBlocks.NORMAL_MACHINE_FRAME, Tags.Items.STONE, () -> Tags.Items.INGOTS_IRON);
        this.machineFrame(consumer, SoulBlocks.SOULIUM_MACHINE_FRAME, SoulItemTags.SOUL_BLOCKS, SoulItems.SOULIUM_INGOT);

        ShapedRecipeBuilder.shaped(SoulBlocks.SIMPLE_COAL_GENERATOR.get()).define('p', ItemTags.PLANKS).define('m', SoulBlocks.SIMPLE_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.SIMPLE_BATTERY.get())
                .pattern("pfp").pattern("pbp").pattern("pmp")
                .unlockedBy("has_planks", has(ItemTags.PLANKS)).unlockedBy("has_machine_frame", has(SoulBlocks.SIMPLE_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE)).unlockedBy("has_battery", has(SoulItems.SIMPLE_BATTERY.get())).save(consumer);

        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_COAL_GENERATOR.get()).define('s', Tags.Items.STONE).define('m', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.NORMAL_BATTERY.get())
                .pattern("fsf").pattern("sbs").pattern("sms")
                .unlockedBy("has_stone", has(Tags.Items.STONE)).unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE)).unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer);
        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_COAL_GENERATOR.get()).define('s', Tags.Items.STONE).define('m', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('c', SoulBlocks.SIMPLE_COAL_GENERATOR.get()).define('b', SoulItems.NORMAL_BATTERY.get())
                .pattern("csc").pattern("sbs").pattern("sms")
                .unlockedBy("has_stone", has(Tags.Items.STONE)).unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_simple_coal_generator", has(SoulBlocks.SIMPLE_COAL_GENERATOR.get())).unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer, SoulMod.getLocation("normal_coal_generator_from_simple_coal_generator"));

        ShapedRecipeBuilder.shaped(SoulBlocks.SOUL_GENERATOR.get()).define('i', SoulItems.SOULIUM_INGOT.get()).define('m', SoulBlocks.SOULIUM_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.SOULIUM_BATTERY.get())
                .pattern(" f ").pattern("ibi").pattern("imi")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get())).unlockedBy("has_machine_frame", has(SoulBlocks.SOULIUM_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE)).unlockedBy("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer);

        ShapedRecipeBuilder.shaped(SoulBlocks.SIMPLE_SOUL_BOX.get()).define('p', ItemTags.PLANKS).define('f', SoulBlocks.SIMPLE_MACHINE_FRAME.get())
                .define('c', SoulItemTags.INGOTS_COPPER).define('b', SoulItems.SIMPLE_BATTERY.get()).pattern("pcp").pattern("bbb").pattern("pfp")
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SIMPLE_MACHINE_FRAME.get()))
                .unlockedBy("has_copper_ingot", has(SoulItemTags.INGOTS_COPPER))
                .unlockedBy("has_battery", has(SoulItems.SIMPLE_BATTERY.get())).save(consumer);

        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_SOUL_BOX.get()).define('i', Tags.Items.INGOTS_IRON).define('f', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('c', SoulItemTags.INGOTS_COPPER).define('b', SoulItems.NORMAL_BATTERY.get()).pattern("ici").pattern("bbb").pattern("ifi")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_copper_ingot", has(SoulItemTags.INGOTS_COPPER))
                .unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer);
        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_SOUL_BOX.get()).define('i', Tags.Items.INGOTS_IRON).define('f', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('o', SoulBlocks.SIMPLE_SOUL_BOX.get()).define('b', SoulItems.SIMPLE_BATTERY.get()).pattern("ibi").pattern("bob").pattern("ifi")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_simple_soul_box", has(SoulBlocks.SIMPLE_SOUL_BOX.get()))
                .unlockedBy("has_battery", has(SoulItems.SIMPLE_BATTERY.get())).save(consumer, SoulMod.getLocation("normal_soul_box_from_simple_soul_box"));

        ShapedRecipeBuilder.shaped(SoulBlocks.SOULIUM_SOUL_BOX.get()).define('s', SoulItems.SOULIUM_INGOT.get()).define('f', SoulBlocks.SOULIUM_MACHINE_FRAME.get())
                .define('c', SoulItemTags.INGOTS_COPPER).define('b', SoulItems.SOULIUM_BATTERY.get()).pattern("scs").pattern("bbb").pattern("sfs")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get()))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SOULIUM_MACHINE_FRAME.get()))
                .unlockedBy("has_copper_ingot", has(SoulItemTags.INGOTS_COPPER))
                .unlockedBy("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer);
        ShapedRecipeBuilder.shaped(SoulBlocks.SOULIUM_SOUL_BOX.get()).define('s', SoulItems.SOULIUM_INGOT.get()).define('f', SoulBlocks.SOULIUM_MACHINE_FRAME.get())
                .define('o', SoulBlocks.NORMAL_SOUL_BOX.get()).define('b', SoulItems.NORMAL_BATTERY.get()).pattern("sbs").pattern("bob").pattern("sfs")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get()))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SOULIUM_MACHINE_FRAME.get()))
                .unlockedBy("has_normal_soul_box", has(SoulBlocks.NORMAL_SOUL_BOX.get()))
                .unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer, SoulMod.getLocation("soulium_soul_box_from_normal_soul_box"));

        ShapedRecipeBuilder.shaped(SoulBlocks.SIMPLE_ALLOY_SMELTER.get()).define('i', ItemTags.PLANKS).define('m', SoulBlocks.SIMPLE_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.SIMPLE_BATTERY.get()).pattern("fff").pattern("ibi").pattern("imi")
                .unlockedBy("has_planks", has(ItemTags.PLANKS))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SIMPLE_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE))
                .unlockedBy("has_battery", has(SoulItems.SIMPLE_BATTERY.get())).save(consumer);

        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_ALLOY_SMELTER.get()).define('i', Tags.Items.INGOTS_IRON).define('m', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.NORMAL_BATTERY.get()).pattern("fff").pattern("ibi").pattern("imi")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE))
                .unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer);
        ShapedRecipeBuilder.shaped(SoulBlocks.NORMAL_ALLOY_SMELTER.get()).define('i', Tags.Items.INGOTS_IRON).define('m', SoulBlocks.NORMAL_MACHINE_FRAME.get())
                .define('a', SoulBlocks.SIMPLE_ALLOY_SMELTER.get()).define('b', SoulItems.SIMPLE_BATTERY.get()).pattern("a a").pattern("ibi").pattern("imi")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_machine_frame", has(SoulBlocks.NORMAL_MACHINE_FRAME.get()))
                .unlockedBy("has_simple_alloy_smelter", has(SoulBlocks.SIMPLE_ALLOY_SMELTER.get()))
                .unlockedBy("has_battery", has(SoulItems.SIMPLE_BATTERY.get())).save(consumer, SoulMod.getLocation("normal_alloy_smelter_from_simple_alloy_smelter"));

        ShapedRecipeBuilder.shaped(SoulBlocks.SOULIUM_ALLOY_SMELTER.get()).define('i', SoulItems.SOULIUM_INGOT.get()).define('m', SoulBlocks.SOULIUM_MACHINE_FRAME.get())
                .define('f', Blocks.FURNACE).define('b', SoulItems.SOULIUM_BATTERY.get()).pattern("fff").pattern("ibi").pattern("imi")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get()))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SOULIUM_MACHINE_FRAME.get()))
                .unlockedBy("has_furnace", has(Blocks.FURNACE))
                .unlockedBy("has_battery", has(SoulItems.SOULIUM_BATTERY.get())).save(consumer);
        ShapedRecipeBuilder.shaped(SoulBlocks.SOULIUM_ALLOY_SMELTER.get()).define('i', SoulItems.SOULIUM_INGOT.get()).define('m', SoulBlocks.SOULIUM_MACHINE_FRAME.get())
                .define('a', SoulBlocks.NORMAL_ALLOY_SMELTER.get()).define('b', SoulItems.NORMAL_BATTERY.get()).pattern("a a").pattern("ibi").pattern("imi")
                .unlockedBy("has_soul_metal", has(SoulItems.SOULIUM_INGOT.get()))
                .unlockedBy("has_machine_frame", has(SoulBlocks.SOULIUM_MACHINE_FRAME.get()))
                .unlockedBy("has_simple_alloy_smelter", has(SoulBlocks.NORMAL_ALLOY_SMELTER.get()))
                .unlockedBy("has_battery", has(SoulItems.NORMAL_BATTERY.get())).save(consumer, SoulMod.getLocation("soulium_alloy_smelter_from_normal_alloy_smelter"));
    }

    private void battery(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends BatteryItem> result, Supplier<?> material, @Nullable Supplier<?> cornerMaterial)
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(result.get()).define('c', SoulItemTags.INGOTS_COPPER).define('r', Tags.Items.DUSTS_REDSTONE);

        if (material.get() instanceof IItemProvider)
            builder = builder.define('m', ((IItemProvider) material.get()).asItem());
        else if (material.get() instanceof ITag<?>)
            builder = builder.define('m', (ITag<Item>) material.get());

        if (cornerMaterial != null)
        {
            if (cornerMaterial.get() instanceof IItemProvider)
                builder = builder.define('o', ((IItemProvider) cornerMaterial.get()).asItem());
            else if (cornerMaterial.get() instanceof ITag<?>)
                builder = builder.define('o', (ITag<Item>) cornerMaterial.get());

            builder = builder.pattern("oco").pattern("mrm").pattern("oco");

            if (cornerMaterial.get() instanceof IItemProvider)
                builder = builder.unlockedBy("has_corner_material", has(((IItemProvider) cornerMaterial.get()).asItem()));
            else if (cornerMaterial.get() instanceof ITag<?>)
                builder = builder.unlockedBy("has_corner_material", has((ITag<Item>) cornerMaterial.get()));
        } else
            builder = builder.pattern("mcm").pattern("mrm").pattern("mcm");

        if (material.get() instanceof IItemProvider)
            builder = builder.unlockedBy("has_material", has(((IItemProvider) material.get()).asItem()));
        else if (material.get() instanceof ITag<?>)
            builder = builder.unlockedBy("has_material", has((ITag<Item>) material.get()));
        builder.unlockedBy("has_copper_ingot", has(SoulItemTags.INGOTS_COPPER)).unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)).save(consumer);
    }

    private void machineFrame(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends MachineFrameBlock> result, ITag<Item> material, Supplier<?> cornerMaterial)
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(result.get()).define('s', material);
        if (cornerMaterial.get() instanceof IItemProvider)
            builder.define('i', ((IItemProvider) cornerMaterial.get()).asItem()).pattern("isi").pattern("s s").pattern("isi")
                    .unlockedBy("has_material", has(material)).unlockedBy("has_corner_material", has(((IItemProvider) cornerMaterial.get()).asItem())).save(consumer);
        else if (cornerMaterial.get() instanceof ITag<?>)
            builder.define('i', (ITag<Item>) cornerMaterial.get()).pattern("isi").pattern("s s").pattern("isi")
                    .unlockedBy("has_material", has(material)).unlockedBy("has_corner_material", has((ITag<Item>) cornerMaterial.get())).save(consumer);
    }

    private void soulAmulet(Consumer<IFinishedRecipe> consumer, RegistryObject<? extends SoulAmuletItem> result, Supplier<IItemProvider> ingot, @Nullable Supplier<IItemProvider> top)
    {
        boolean flag = top != null;
        ShapedRecipeBuilder recipeBuilder = ShapedRecipeBuilder.shaped(result.get()).define('i', ingot.get()).define('s', SoulItems.SOUL_BOTTLE.get());

        if (flag)
            recipeBuilder.define('t', top.get()).pattern(" t ");
        else
            recipeBuilder.pattern(" i ");

        recipeBuilder.pattern("i i").pattern(" s ")
                .unlockedBy("has_soul_bottle", has(SoulItems.SOUL_BOTTLE.get()))
                .unlockedBy("has_ingot", has(ingot.get()));
        if (flag)
            recipeBuilder.unlockedBy("has_top", has(top.get()));
        recipeBuilder.save(consumer);
    }
}
