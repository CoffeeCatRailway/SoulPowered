package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.client.entity.model.AmuletModel;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author CoffeeCatRailway
 * Created: 25/11/2020
 */
public interface ISoulAmulet extends ISoulCurios
{
    AmuletModel MODEL = new AmuletModel();

    float getRange();

    float getSoulGatheringChance();

    ResourceLocation getTexture();

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
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + "." + stack.getItem().getRegistryName().getPath() + ".description"));

        CompoundNBT nbt = stack.getOrCreateTag();
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".curio.range", nbt.getFloat("Range")));
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_amulet.soul_gathering_chance", (int) (nbt.getFloat("SoulGatheringChance") * 100)));
    }

    @Override
    default boolean canRender(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
        return true;
    }

    @Override
    default void render(ItemStack stack, String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch)
    {
        ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

        IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(renderTypeBuffer, MODEL.getRenderType(getTexture()), false, stack.hasEffect());
        matrixStack.scale(1.01f, 1.01f, 1.01f);
        MODEL.render(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        MODEL.setRotationAngles(livingEntity, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
    }
}
