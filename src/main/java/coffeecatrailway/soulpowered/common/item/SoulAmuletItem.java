package coffeecatrailway.soulpowered.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public class SoulAmuletItem extends Item implements ISoulAmulet
{
    private final float range;
    private final float soulGatheringChance;
    private final ResourceLocation modelTexture;

    public SoulAmuletItem(Properties properties, IItemTier tier, float range, float soulGatheringChance, ResourceLocation modelTexture)
    {
        this(properties, tier.getMaxUses(), range, soulGatheringChance, modelTexture);
    }

    public SoulAmuletItem(Properties properties, int maxUses, float range, float soulGatheringChance, ResourceLocation modelTexture)
    {
        super(properties.defaultMaxDamage(maxUses));
        this.range = range;
        this.soulGatheringChance = soulGatheringChance;
        this.modelTexture = modelTexture;
    }

    @Override
    public void curioTick(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return ISoulAmulet.super.initCapabilities(stack, nbt);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
        ISoulAmulet.super.addInformation(stack, world, info, flag);
    }

    @Override
    public float getRange()
    {
        return this.range;
    }

    @Override
    public float getSoulGatheringChance()
    {
        return this.soulGatheringChance;
    }

    @Override
    public ResourceLocation getTexture()
    {
        return this.modelTexture;
    }
}
