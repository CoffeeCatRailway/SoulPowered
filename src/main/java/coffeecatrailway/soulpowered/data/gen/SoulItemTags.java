package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import coffeecatrailway.soulpowered.registry.SoulItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 14/05/2021
 */
public class SoulItemTags extends ItemTagsProvider
{
    public static final ITag.INamedTag<Item> SOUL_BLOCKS = ItemTags.createOptional(SoulMod.getLocation("soul_blocks"));
    public static final ITag.INamedTag<Item> SOUL_GENERATOR_FUEL = ItemTags.createOptional(SoulMod.getLocation("soul_generator_fuel"));

    public static final ITag.INamedTag<Item> INGOTS_COPPER = ItemTags.createOptional(new ResourceLocation("forge", "ingots/copper"));

    public static final ITag.INamedTag<Item> CURIOS_NECKLACE = ItemTags.createOptional(new ResourceLocation("curios", "necklace"));
    public static final ITag.INamedTag<Item> CURIOS_CHARM = ItemTags.createOptional(new ResourceLocation("curios", "charm"));

    public SoulItemTags(DataGenerator generator, BlockTagsProvider provider, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generator, provider, SoulMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        this.tag(SOUL_BLOCKS).add(Items.SOUL_SAND, Items.SOUL_SOIL);
        this.tag(SOUL_GENERATOR_FUEL).add(SoulItems.SOUL_BOTTLE.get());

        this.tag(INGOTS_COPPER).add(SoulItems.COPPER_INGOT.get());
        this.tag(Tags.Items.INGOTS).add(SoulItems.SOULIUM_INGOT.get()).addTag(INGOTS_COPPER);

        this.tag(ItemTags.PIGLIN_LOVED).add(SoulItems.GOLD_SOUL_AMULET.get());
        this.tag(CURIOS_NECKLACE).add(SoulItems.GOLD_SOUL_AMULET.get(), SoulItems.COPPER_SOUL_AMULET.get(), SoulItems.IRON_SOUL_AMULET.get(), SoulItems.DIAMOND_SOUL_AMULET.get(), SoulItems.SOULIUM_SOUL_AMULET.get(), SoulItems.POWERED_SOULIUM_SOUL_AMULET.get(), SoulItems.NETHERITE_SOUL_AMULET.get());
        this.tag(CURIOS_CHARM).add(SoulItems.SOUL_SHIELD.get());

        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(SoulItems.SOULIUM_INGOT.get(), SoulItems.COPPER_INGOT.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).add(SoulBlocks.SOULIUM_BLOCK.get().asItem());
    }
}
