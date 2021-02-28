package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.api.RedstoneMode;
import coffeecatrailway.soulpowered.registry.OtherRegistries;
import coffeecatrailway.soulpowered.registry.SoulItems;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.providers.loot.RegistrateLootTableProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.Tags;

import java.util.HashMap;
import java.util.Map;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulData
{
    public static class Lang implements NonNullConsumer<RegistrateLangProvider>
    {
        public static final Map<String, String> EXTRA_LANGS = new HashMap<>();

        private static final String LANG_ENERGY = "misc.soulpowered.energy";
        private static final String LANG_ENERGY_PER_TICK = "misc.soulpowered.energy_per_tick";
        private static final String LANG_ENERGY_WITH_MAX = "misc.soulpowered.energy_with_max";
        //        private static final String LANG_FLUID_WITH_MAX = "misc.soulpowered.fluid_with_max";
        private static final String LANG_REDSTONE_MODE = "misc.soulpowered.redstone_mode";

        public static final String JEI_CATEGORY_COAL_GENERATOR = "gui.soulpowered.jei.category.coal_generator";
        public static final String JEI_CATEGORY_SOUL_GENERATOR = "gui.soulpowered.jei.category.soul_generator";
        public static final String JEI_ITEM_BURN_TIME = "gui.soulpowered.jei.item_burn_time";

        @Override
        public void accept(RegistrateLangProvider provider)
        {
            EXTRA_LANGS.forEach(provider::add);

            provider.add("commands.soulpowered.souls.get", "%1$s has %2$s soul(s)");
            provider.add("commands.soulpowered.souls.modify.add", "Gave %2$s soul(s) to %1$s");
            provider.add("commands.soulpowered.souls.modify.remove", "Removed %2$s soul(s) to %1$s");
            provider.add("commands.soulpowered.souls.modify.set", "%1$s now has %2$s soul(s)");

            provider.add("item.soulpowered.curio.range", TextFormatting.GOLD + "Range: " + TextFormatting.YELLOW + "%s Blocks");

            provider.add("item.soulpowered.soul_amulet.soul_gathering_chance", TextFormatting.GOLD + "Soul Gathering Chance: " + TextFormatting.YELLOW + "%s");
            provider.add("item.soulpowered.soul_shield.duration", TextFormatting.GOLD + "Duration: " + TextFormatting.YELLOW + "%s Seconds");
            provider.add("item.soulpowered.soul_shield.cooldown", TextFormatting.GOLD + "Cooldown: " + TextFormatting.YELLOW + "%s Seconds");

            provider.add(LANG_ENERGY, "%s SE");
            provider.add(LANG_ENERGY_PER_TICK, "%s SE/t");
            provider.add(LANG_ENERGY_WITH_MAX, "%s / %s SE");
//            provider.add(LANG_FLUID_WITH_MAX, "%s / %s mB");
            provider.add(LANG_REDSTONE_MODE, "Redstone Mode: %s");

            provider.add(JEI_CATEGORY_COAL_GENERATOR, "Coal Generator");
            provider.add(JEI_CATEGORY_SOUL_GENERATOR, "Soul Generator");
            provider.add(JEI_ITEM_BURN_TIME, "%s BT");
        }

        public static IFormattableTextComponent energy(int amount)
        {
            return new TranslationTextComponent(LANG_ENERGY, amount);
        }

        public static IFormattableTextComponent energyPerTick(int amount)
        {
            return new TranslationTextComponent(LANG_ENERGY_PER_TICK, amount);
        }

        public static IFormattableTextComponent energyWithMax(int amount, int max)
        {
            return new TranslationTextComponent(LANG_ENERGY_WITH_MAX, amount, max);
        }

        public static IFormattableTextComponent redstoneMode(RedstoneMode mode)
        {
            return new TranslationTextComponent(LANG_REDSTONE_MODE, mode.name());
        }

        public static ITextComponent itemBurnTime(int burnTime)
        {
            return new TranslationTextComponent(JEI_ITEM_BURN_TIME, burnTime);
        }
    }

    public static class TagItems implements NonNullConsumer<RegistrateTagsProvider<Item>>
    {
        public static final ITag.INamedTag<Item> SOUL_BLOCKS = ItemTags.createOptional(SoulPoweredMod.getLocation("soul_blocks"));
        public static final ITag.INamedTag<Item> SOUL_GENERATOR_FUEL = ItemTags.createOptional(SoulPoweredMod.getLocation("soul_generator_fuel"));

        public static final ITag.INamedTag<Item> INGOTS_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/copper"));
        public static final ITag.INamedTag<Item> STORAGE_BLOCKS_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));
        public static final ITag.INamedTag<Item> ORES_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "ores/copper"));

        public static final ITag.INamedTag<Item> CURIOS_NECKLACE = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));
        public static final ITag.INamedTag<Item> CURIOS_CHARM = ItemTags.createOptional(new ResourceLocation("curios", "charm"));

        @Override
        public void accept(RegistrateTagsProvider<Item> provider)
        {
            provider.getOrCreateBuilder(SOUL_BLOCKS).add(Items.SOUL_SAND, Items.SOUL_SOIL);

            provider.getOrCreateBuilder(Tags.Items.INGOTS).addTag(INGOTS_COPPER);
            provider.getOrCreateBuilder(Tags.Items.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_COPPER);
            provider.getOrCreateBuilder(Tags.Items.ORES).addTag(ORES_COPPER);
        }
    }

    public static class TagBlocks implements NonNullConsumer<RegistrateTagsProvider<Block>>
    {
        public static final ITag.INamedTag<Block> STORAGE_BLOCKS_COPPER = BlockTags.createOptional(new ResourceLocation("forge", "storage_blocks/copper"));
        public static final ITag.INamedTag<Block> ORES_COPPER = BlockTags.createOptional(new ResourceLocation("forge", "ores/copper"));

        public static final ITag.INamedTag<Block> HEAT_BLOCKS_BOTTOM = BlockTags.createOptional(SoulPoweredMod.getLocation("heat_blocks_bottom"));
        public static final ITag.INamedTag<Block> HEAT_BLOCKS = BlockTags.createOptional(SoulPoweredMod.getLocation("heat_blocks"));

        @Override
        public void accept(RegistrateTagsProvider<Block> provider)
        {
            provider.getOrCreateBuilder(Tags.Blocks.STORAGE_BLOCKS).addTag(STORAGE_BLOCKS_COPPER);
            provider.getOrCreateBuilder(Tags.Blocks.ORES).addTag(ORES_COPPER);

            provider.getOrCreateBuilder(HEAT_BLOCKS_BOTTOM).addTag(BlockTags.CAMPFIRES);
            provider.getOrCreateBuilder(HEAT_BLOCKS).addTag(BlockTags.FIRE).add(Blocks.WALL_TORCH, Blocks.SOUL_WALL_TORCH, Blocks.REDSTONE_WALL_TORCH, Blocks.MAGMA_BLOCK, Blocks.TORCH, Blocks.SOUL_TORCH, Blocks.REDSTONE_TORCH);
        }
    }

    public static class LootTables implements NonNullConsumer<RegistrateLootTableProvider>
    {
        @Override
        public void accept(RegistrateLootTableProvider provider)
        {
            provider.addLootAction(LootParameterSets.CHEST, builder -> builder.accept(OtherRegistries.CHESTS_SOUL_CASTLE, LootTable.builder()
                    .addLootPool(LootPool.builder()
                            .rolls(RandomValueRange.of(2f, 4f))
                            .addEntry(ItemLootEntry.builder(SoulItems.SOUL_BOTTLE.get())
                                    .weight(99)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1f, 3f))))
                            .addEntry(ItemLootEntry.builder(Blocks.BLACKSTONE)
                                    .weight(20)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(3f, 7f))))
                            .addEntry(ItemLootEntry.builder(Items.IRON_INGOT)
                                    .weight(20)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1f, 5f))))
                            .addEntry(ItemLootEntry.builder(Items.GOLD_INGOT)
                                    .weight(20)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1f, 3f))))
                            .addEntry(ItemLootEntry.builder(Items.DIAMOND)
                                    .weight(10)
                                    .acceptFunction(SetCount.builder(RandomValueRange.of(1f, 3f))))
                            .addEntry(ItemLootEntry.builder(Items.GOLDEN_SWORD)
                                    .weight(10)
                                    .acceptFunction(EnchantRandomly.func_215900_c()))
                            .addEntry(ItemLootEntry.builder(SoulItems.GOLD_SOUL_AMULET.get())
                                    .weight(5))
                            .addEntry(ItemLootEntry.builder(SoulItems.COPPER_SOUL_AMULET.get())
                                    .weight(5))
                            .addEntry(ItemLootEntry.builder(SoulItems.NETHERITE_SOUL_AMULET.get())
                                    .weight(1)))));
        }
    }
}
