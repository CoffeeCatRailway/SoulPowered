package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.ISlotType;

import java.util.Optional;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public interface ISoulCurios
{
    void curioTick(String identifier, int index, LivingEntity livingEntity);

    default boolean canEquip(String identifier, LivingEntity livingEntity)
    {
        if (livingEntity instanceof PlayerEntity)
        {
            Optional<ISlotType> necklace = CuriosApi.getSlotHelper().getSlotType("necklace");
            if (necklace.isPresent())
                for (int i = 0; i < necklace.get().getSize(); i++)
                    if (CuriosIntegration.getCurioStack((PlayerEntity) livingEntity, "necklace", i).isEmpty())
                        return true;
        }
        return false;
    }

    default boolean canUnequip(String identifier, LivingEntity livingEntity)
    {
        if (livingEntity instanceof PlayerEntity)
            return this instanceof Item && !((PlayerEntity) livingEntity).getCooldownTracker().hasCooldown((Item) this);
        return false;
    }

    default void playRightClickEquipSound(LivingEntity livingEntity)
    {
        livingEntity.world.playSound(null, new BlockPos(livingEntity.getPositionVec()), SoundEvents.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.NEUTRAL, 1f, 1f);
    }

    default boolean canRightClickEquip()
    {
        return SoulPoweredMod.SERVER_CONFIG.canRightClickEquipCurio.get();
    }

    default boolean canRender(String identifier, int index, LivingEntity livingEntity)
    {
        return false;
    }

    default void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch)
    {
    }
}
