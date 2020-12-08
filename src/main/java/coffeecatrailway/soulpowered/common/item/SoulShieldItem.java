package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.entity.SoulShieldEntity;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * @author CoffeeCatRailway
 * Created: 1/12/2020
 */
public class SoulShieldItem extends Item implements ISoulCurios
{
    private static Color DUST_COLOR = new Color(SoulPoweredMod.CLIENT_CONFIG.soulShieldParticleColor.get());

    private final float range;
    private final float durationInTicks;
    private final float cooldownInTicks;

    public SoulShieldItem(Properties properties, float range, float durationInSeconds, float cooldownInSeconds)
    {
        super(properties);
        this.range = range;
        this.durationInTicks = durationInSeconds * 20f;
        this.cooldownInTicks = cooldownInSeconds * 20f;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        CompoundNBT stackNbt = stack.getOrCreateTag();

        if (!stackNbt.contains("Active", Constants.NBT.TAG_BYTE))
            stackNbt.putBoolean("Active", false);

        if (!stackNbt.contains("DurationInTicks", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("DurationInTicks", this.durationInTicks);

        if (!stackNbt.contains("CooldownInTicks", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("CooldownInTicks", this.cooldownInTicks);

        if (!stackNbt.contains("Range", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("Range", this.range);
        return CuriosIntegration.getCapability(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> info, ITooltipFlag flag)
    {
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_shield.description"));

        CompoundNBT nbt = stack.getOrCreateTag();
        float duration = nbt.getFloat("DurationInTicks") / 20f;
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_shield.duration", duration));

        float cooldown = nbt.getFloat("CooldownInTicks") / 20f;
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_shield.cooldown", cooldown));

        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".curio.range", nbt.getFloat("Range")));
    }

    @Override
    public void curioTick(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
        if (livingEntity instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) livingEntity;
            CompoundNBT nbt = stack.getOrCreateTag();
            float durationInTicks = nbt.getFloat("DurationInTicks");
            float cooldownInTicks = nbt.getFloat("CooldownInTicks");

            if (this.isActive(stack) && !player.getCooldownTracker().hasCooldown(this) && !player.world.isRemote)
                    this.onCurioActivate(durationInTicks, cooldownInTicks, stack, player);

            if (this.isActive(stack))
                this.activate(stack, false);
        }
    }

    public void onCurioActivate(float durationInTicks, float cooldownInTicks, ItemStack stack, PlayerEntity player)
    {
        SoulsCapability.ifPresent(player, handler -> {
            if (handler.getSouls() >= SoulPoweredMod.SERVER_CONFIG.soulShieldSoulsUse.get() || player.isCreative())
            {
                World world = player.world;
                SoulShieldEntity shieldEntity = new SoulShieldEntity(world, player, stack, durationInTicks);
                ServerWorld serverWorld = (ServerWorld) world;
                if (serverWorld.addEntity(shieldEntity))
                {
                    Random rand = serverWorld.rand;
                    float xs = rand.nextFloat() * .5f - .25f;
                    float ys = rand.nextFloat() * .5f - .15f;
                    float zs = rand.nextFloat() * .5f - .25f;

                    float r = getDustColor().getRed() / 255f;
                    float g = getDustColor().getGreen() / 255f;
                    float b = getDustColor().getBlue() / 255f;
                    float a = getDustColor().getAlpha() / 255f;
                    serverWorld.spawnParticle(new RedstoneParticleData(r, g, b, a), shieldEntity.getPosX(), shieldEntity.getPosY() + .5f, shieldEntity.getPosZ(), 10, xs, ys, zs, 1);

                    handler.removeSouls(SoulPoweredMod.SERVER_CONFIG.soulShieldSoulsUse.get(), false);
                    if (!player.isCreative())
                        player.getCooldownTracker().setCooldown(this, (int) (cooldownInTicks + durationInTicks));
                }
            }
        });
    }

    public void activate(ItemStack stack, boolean active)
    {
        if (!stack.isEmpty())
            stack.getOrCreateTag().putBoolean("Active", active);
    }

    public boolean isActive(ItemStack stack)
    {
        CompoundNBT nbt = stack.getOrCreateTag();
        if (nbt.contains("Active", Constants.NBT.TAG_BYTE))
            return nbt.getBoolean("Active");
        return false;
    }

    public static Color getDustColor()
    {
        if (DUST_COLOR.getRGB() != SoulPoweredMod.CLIENT_CONFIG.soulShieldParticleColor.get())
            DUST_COLOR = new Color(SoulPoweredMod.CLIENT_CONFIG.soulShieldParticleColor.get());
        return DUST_COLOR;
    }
}
