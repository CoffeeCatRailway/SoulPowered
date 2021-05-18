package coffeecatrailway.soulpowered.data.gen;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.registry.SoulBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 14/05/2021
 */
public class SoulBlockTags extends BlockTagsProvider
{
    public SoulBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper)
    {
        super(generator, SoulMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags()
    {
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(SoulBlocks.SOULIUM_BLOCK.get());
        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(SoulBlocks.SOULIUM_BLOCK.get());
    }
}
