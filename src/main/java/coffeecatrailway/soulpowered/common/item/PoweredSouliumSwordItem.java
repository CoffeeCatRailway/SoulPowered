package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import coffeecatrailway.soulpowered.api.utils.EnergyUtils;
import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 11/01/2021
 */
public class PoweredSouliumSwordItem extends SwordItem implements IEnergyItem
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Powered Soulium Sword");

    public static final int CAPACITY = 12_500;
    public static final int TRANSFER = 5000;

    private final Multimap<Attribute, AttributeModifier> unpoweredAttributeModifiers;

    public PoweredSouliumSwordItem(Properties properties)
    {
        super(SoulItemTier.POWERED_SOULIUM, 3, -2.4f, properties.maxStackSize(1).defaultMaxDamage(0));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage() / 2f, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4f, AttributeModifier.Operation.ADDITION));
        this.unpoweredAttributeModifiers = builder.build();
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        return super.getDestroySpeed(stack, state) / (this.hasEffect(stack) ? 1f : 2f);
    }

    public static void gainSouls(PlayerEntity player, LivingEntity hurt, World world, Runnable onGain)
    {
        SoulsCapability.ifPresent(player, playerHandler -> {
            int soulCount = 1;
            if (hurt instanceof PlayerEntity && SoulsCapability.isPresent(hurt))
                soulCount = world.rand.nextInt(Math.max(1, SoulsCapability.get(hurt).orElse(SoulsCapability.EMPTY).getSouls()) / 2) + 1;

            playerHandler.addSouls(soulCount, false);
            if (!world.isRemote)
            {
                SoulParticle.spawnParticles(world, player, hurt.getPositionVec().add(0d, 1d, 0d), soulCount + world.getRandom().nextInt(3) + 1, false);
                onGain.run();
                LOGGER.debug("Player {} killed/attacked living entity, souls given {}", player.getUniqueID().toString(), soulCount);
            }
        });
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        EnergyUtils.ifPresent(stack, energy -> energy.extractEnergy(SoulPoweredMod.SERVER_CONFIG.poweredSouliumSwordEnergyCost.get(), false));
        if (attacker instanceof PlayerEntity && this.hasEffect(stack) && MathHelper.nextDouble(attacker.world.getRandom(), 0d, 1d) < SoulPoweredMod.SERVER_CONFIG.poweredSouliumSwordSoulChance.get())
            gainSouls((PlayerEntity) attacker, target, attacker.world, () -> EnergyUtils.ifPresent(stack, energy -> energy.extractEnergy(SoulPoweredMod.SERVER_CONFIG.poweredSouliumSwordEnergyCost.get() * 2, false)));
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving)
    {
        if (state.getBlockHardness(world, pos) != 0f)
            EnergyUtils.ifPresent(stack, energy -> energy.extractEnergy(SoulPoweredMod.SERVER_CONFIG.poweredSouliumSwordEnergyCost.get() * 2, false));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
        if (stack.isEmpty() || slot != EquipmentSlotType.MAINHAND)
            return super.getAttributeModifiers(slot, stack);
        return this.hasEffect(stack) ? super.getAttributeModifiers(slot) : this.unpoweredAttributeModifiers;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        super.fillItemGroup(group, items);
        if (this.isInGroup(group))
            this.addItemVarients(items);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);
        IEnergyItem.super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return IEnergyItem.super.hasEnergy(stack, SoulPoweredMod.SERVER_CONFIG.poweredSouliumSwordEffectEnergyAmount.get());
    }

    @Override
    public int getMaxEnergy()
    {
        return CAPACITY;
    }

    @Override
    public int getMaxReceive()
    {
        return TRANSFER;
    }

    @Override
    public int getMaxExtract()
    {
        return TRANSFER;
    }
}
