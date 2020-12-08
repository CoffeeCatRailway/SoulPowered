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
        ((ISoulCurios) this.stack.getItem()).curioTick(this.stack, identifier, index, livingEntity);
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity livingEntity)
    {
        return ((ISoulCurios) this.stack.getItem()).canEquip(this.stack, identifier, livingEntity);
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity)
    {
        return ((ISoulCurios) this.stack.getItem()).canUnequip(this.stack, identifier, livingEntity);
    }

    @Override
    public void playRightClickEquipSound(LivingEntity livingEntity)
    {
        ((ISoulCurios) this.stack.getItem()).playRightClickEquipSound(this.stack, livingEntity);
    }

    @Override
    public boolean canRightClickEquip()
    {
        return ((ISoulCurios) this.stack.getItem()).canRightClickEquip(this.stack);
    }

    @Override
    public boolean canRender(String identifier, int index, LivingEntity livingEntity)
    {
        return ((ISoulCurios) this.stack.getItem()).canRender(this.stack, identifier, index, livingEntity);
    }

    @Override
    public void render(String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
    {
        if (this.canRender(identifier, index, livingEntity))
            ((ISoulCurios) this.stack.getItem()).render(this.stack, identifier, index, matrixStack, renderTypeBuffer, light, livingEntity, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
    }
}
