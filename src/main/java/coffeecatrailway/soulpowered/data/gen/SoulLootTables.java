package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import coffeecatrailway.soulpowered.registry.SoulItems;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.ChestLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.functions.EnchantRandomly;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author CoffeeCatRailway
 * Created: 14/05/2021
 */
public class SoulLootTables extends ForgeLootTableProvider
{
    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> tables = ImmutableList.of(
            Pair.of(ChestProvider::new, LootParameterSets.CHEST),
            Pair.of(EntityProvider::new, LootParameterSets.ENTITY),
            Pair.of(BlockProvider::new, LootParameterSets.BLOCK)
    );

    public static final ResourceLocation CHESTS_SOUL_CASTLE = SoulMod.getLocation("chests/soul_castle");

    public SoulLootTables(DataGenerator gen)
    {
        super(gen);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables()
    {
        return this.tables;
    }

    private static class ChestProvider extends ChestLootTables
    {
        @Override
        public void accept(BiConsumer<ResourceLocation, LootTable.Builder> registry)
        {
            registry.accept(CHESTS_SOUL_CASTLE, LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(RandomValueRange.between(2f, 4f))
                            .add(ItemLootEntry.lootTableItem(SoulItems.SOUL_BOTTLE.get())
                                    .setWeight(99)
                                    .apply(SetCount.setCount(RandomValueRange.between(1f, 3f))))
                            .add(ItemLootEntry.lootTableItem(Blocks.BLACKSTONE)
                                    .setWeight(20)
                                    .apply(SetCount.setCount(RandomValueRange.between(3f, 7f))))
                            .add(ItemLootEntry.lootTableItem(Items.IRON_INGOT)
                                    .setWeight(20)
                                    .apply(SetCount.setCount(RandomValueRange.between(1f, 5f))))
                            .add(ItemLootEntry.lootTableItem(Items.GOLD_INGOT)
                                    .setWeight(20)
                                    .apply(SetCount.setCount(RandomValueRange.between(1f, 3f))))
                            .add(ItemLootEntry.lootTableItem(Items.DIAMOND)
                                    .setWeight(10)
                                    .apply(SetCount.setCount(RandomValueRange.between(1f, 3f))))
                            .add(ItemLootEntry.lootTableItem(Items.GOLDEN_SWORD)
                                    .setWeight(10)
                                    .apply(EnchantRandomly.randomApplicableEnchantment()))
                            .add(ItemLootEntry.lootTableItem(SoulItems.GOLD_SOUL_AMULET.get())
                                    .setWeight(5))
                            .add(ItemLootEntry.lootTableItem(SoulItems.COPPER_SOUL_AMULET.get())
                                    .setWeight(5))
                            .add(ItemLootEntry.lootTableItem(SoulItems.NETHERITE_SOUL_AMULET.get())
                                    .setWeight(1))));
        }
    }

    private static class EntityProvider extends EntityLootTables
    {
        @Override
        protected void addTables()
        {
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities()
        {
            return ForgeRegistries.ENTITIES.getValues().stream().filter(entityType -> entityType.getRegistryName() != null && SoulMod.MOD_ID.equals(entityType.getRegistryName().getNamespace())).collect(Collectors.toSet());
        }
    }

    private static class BlockProvider extends BlockLootTables
    {
        @Override
        protected void addTables()
        {
            this.dropSelf(SoulBlocks.SOULIUM_BLOCK.get());

            this.dropSelf(SoulBlocks.SIMPLE_MACHINE_FRAME.get());
            this.dropSelf(SoulBlocks.NORMAL_MACHINE_FRAME.get());
            this.dropSelf(SoulBlocks.SOULIUM_MACHINE_FRAME.get());

            this.dropSelf(SoulBlocks.SIMPLE_COAL_GENERATOR.get());
            this.dropSelf(SoulBlocks.NORMAL_COAL_GENERATOR.get());
            this.dropSelf(SoulBlocks.SOUL_GENERATOR.get());

            this.dropSelf(SoulBlocks.SIMPLE_SOUL_BOX.get());
            this.dropSelf(SoulBlocks.NORMAL_SOUL_BOX.get());
            this.dropSelf(SoulBlocks.SOULIUM_SOUL_BOX.get());

            this.dropSelf(SoulBlocks.SIMPLE_ALLOY_SMELTER.get());
            this.dropSelf(SoulBlocks.NORMAL_ALLOY_SMELTER.get());
            this.dropSelf(SoulBlocks.SOULIUM_ALLOY_SMELTER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ForgeRegistries.BLOCKS.getValues().stream().filter(entityType -> entityType.getRegistryName() != null && SoulMod.MOD_ID.equals(entityType.getRegistryName().getNamespace())).collect(Collectors.toSet());
        }
    }
}
