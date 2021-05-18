package coffeecatrailway.soulpowered.api.item;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public interface ISoulCurios
{
    void curioTick(ItemStack stack, String identifier, int index, LivingEntity livingEntity);

    default boolean canEquip(ItemStack stack, String identifier, LivingEntity livingEntity)
    {
        int i = 0;
        if (livingEntity instanceof PlayerEntity)
        {
            if (CuriosIntegration.NECKLACE.get().isPresent())
                for (int j = 0; j < CuriosIntegration.NECKLACE.get().get().getSize(); j++)
                    if (CuriosIntegration.getCurioStack((PlayerEntity) livingEntity, "necklace", j).isEmpty())
                        i++;
            if (CuriosIntegration.CHARM.get().isPresent())
                for (int j = 0; j < CuriosIntegration.CHARM.get().get().getSize(); j++)
                    if (CuriosIntegration.getCurioStack((PlayerEntity) livingEntity, "charm", j).isEmpty())
                        i++;
        }
        return i != 0;
    }

    default boolean canUnequip(ItemStack stack, String identifier, LivingEntity livingEntity)
    {
        if (livingEntity instanceof PlayerEntity)
            return this instanceof Item && !((PlayerEntity) livingEntity).getCooldowns().isOnCooldown((Item) this);
        return false;
    }

    default void playRightClickEquipSound(ItemStack stack, LivingEntity livingEntity)
    {
        livingEntity.level.playSound(null, new BlockPos(livingEntity.position()), SoundEvents.ARMOR_EQUIP_GOLD, SoundCategory.NEUTRAL, 1f, 1f);
    }

    default boolean canRightClickEquip(ItemStack stack)
    {
        return SoulMod.SERVER_CONFIG.canRightClickEquipCurio.get();
    }

    default boolean canRender(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
        return false;
    }

    default void render(ItemStack stack, String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch)
    {
    }
}
