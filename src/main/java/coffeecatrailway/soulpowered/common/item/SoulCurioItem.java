package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
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
 * Created: 8/11/2020
 */
public abstract class SoulCurioItem extends Item implements ISoulCurios
{
    private float range = 0f;
    private boolean showRange;
    private int soulGathering = 0;
    private boolean showSoulGathering;

    public SoulCurioItem(Properties properties)
    {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        CompoundNBT stackNbt = stack.getOrCreateTag();
        if (this.hasRange())
            if (!stackNbt.contains("Range", Constants.NBT.TAG_ANY_NUMERIC))
                stackNbt.putFloat("Range", this.range);
        if (this.hasSoulGathering())
            if (!stackNbt.contains("SoulGathering", Constants.NBT.TAG_ANY_NUMERIC))
                stackNbt.putFloat("SoulGathering", this.soulGathering);
        return CuriosIntegration.getCapability(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
        info.add(new TranslationTextComponent(this.getTranslationKey() + ".description"));

        if (this.showRange && this.hasRange())
            info.add(new TranslationTextComponent("item.soul_curio.description.range", this.range));
        if (this.showSoulGathering && this.hasSoulGathering())
            info.add(new TranslationTextComponent("item.soul_curio.description.soul_gathering", this.soulGathering));
    }

    public boolean hasRange()
    {
        return this.range != 0f;
    }

    protected void setRange(float range)
    {
        this.setRange(range, range != 0f);
    }

    protected void setRange(float range, boolean showRange)
    {
        this.range = range;
        this.showRange = showRange;
    }

    public boolean hasSoulGathering()
    {
        return this.soulGathering != 0;
    }

    protected void setSoulGathering(int soulGathering)
    {
        this.setSoulGathering(soulGathering, soulGathering != 0);
    }

    protected void setSoulGathering(int soulGathering, boolean showSoulGathering)
    {
        this.soulGathering = soulGathering;
        this.showSoulGathering = showSoulGathering;
    }
}
