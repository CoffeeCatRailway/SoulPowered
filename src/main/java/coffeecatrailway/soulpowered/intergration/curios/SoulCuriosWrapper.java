package coffeecatrailway.soulpowered.intergration.curios;

import coffeecatrailway.soulpowered.common.item.ISoulCurios;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.type.capability.ICurio;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public class SoulCuriosWrapper implements ICurio
{
    private final ItemStack stack;

    public SoulCuriosWrapper(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity)
    {
        ((ISoulCurios) stack.getItem()).curioTick(identifier, index, livingEntity);
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity livingEntity)
    {
        return ((ISoulCurios) stack.getItem()).canEquip(identifier, livingEntity);
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity)
    {
        return ((ISoulCurios) stack.getItem()).canUnequip(identifier, livingEntity);
    }

    @Override
    public void playRightClickEquipSound(LivingEntity livingEntity)
    {
        ((ISoulCurios) stack.getItem()).playRightClickEquipSound(livingEntity);
    }

    @Override
    public boolean canRightClickEquip()
    {
        return ((ISoulCurios) stack.getItem()).canRightClickEquip();
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity)
    {
        return ((ISoulCurios) stack.getItem()).canRender(identifier, index, livingEntity);
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        ((ISoulCurios) stack.getItem()).render(identifier, index, matrixStack, renderTypeBuffer, light, livingEntity, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
    }
}
