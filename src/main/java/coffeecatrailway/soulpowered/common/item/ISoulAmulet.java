package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public interface ISoulAmulet
{
    float getRange();

    float getSoulGatheringChance();

    @Nullable
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        CompoundNBT stackNbt = stack.getOrCreateTag();

        if (!stackNbt.contains("Range", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("Range", this.getRange());

        if (!stackNbt.contains("SoulGatheringChance", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("SoulGatheringChance", this.getSoulGatheringChance());
        return CuriosIntegration.getCapability(stack);
    }

    default void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_amulet.description"));

        CompoundNBT nbt = stack.getOrCreateTag();
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_amulet.range", nbt.getFloat("Range")));
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_amulet.soul_gathering_chance", (int) (nbt.getFloat("SoulGatheringChance") * 100)));
    }
}
