package coffeecatrailway.soulpowered.common.item;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.api.item.ISoulCurios;
import coffeecatrailway.soulpowered.client.entity.model.AmuletModel;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
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
 * Created: 8/11/2020
 */
public class SoulAmuletItem extends Item implements ISoulCurios
{
    private static final AmuletModel MODEL = new AmuletModel();

    private final float range;
    private final float soulGatheringChance;
    private final ResourceLocation modelTexture;

    public SoulAmuletItem(Properties properties, IItemTier tier, float range, float soulGatheringChance, ResourceLocation modelTexture)
    {
        this(properties, tier.getUses(), range, soulGatheringChance, modelTexture);
    }

    public SoulAmuletItem(Properties properties, int maxUses, float range, float soulGatheringChance, ResourceLocation modelTexture)
    {
        super(properties.durability(maxUses));
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
        CompoundNBT stackNbt = stack.getOrCreateTag();

        if (!stackNbt.contains("Range", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("Range", this.range);

        if (!stackNbt.contains("SoulGatheringChance", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("SoulGatheringChance", this.soulGatheringChance);
        return CuriosIntegration.getCapability(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> info, ITooltipFlag flag)
    {
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + "." + stack.getItem().getRegistryName().getPath() + ".description"));

        CompoundNBT nbt = stack.getOrCreateTag();
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".curio.range", nbt.getFloat("Range")));
        info.add(new TranslationTextComponent("item." + SoulPoweredMod.MOD_ID + ".soul_amulet.soul_gathering_chance", (int) (nbt.getFloat("SoulGatheringChance") * 100)));
    }

    @Override
    public boolean canRender(ItemStack stack, String identifier, int index, LivingEntity livingEntity)
    {
        return true;
    }

    @Override
    public void render(ItemStack stack, String identifier, int index, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float netHeadYaw, float headPitch)
    {
        ICurio.RenderHelper.translateIfSneaking(matrixStack, livingEntity);
        ICurio.RenderHelper.rotateIfSneaking(matrixStack, livingEntity);

        IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, MODEL.renderType(this.modelTexture), false, stack.hasFoil());
        matrixStack.scale(1.01f, 1.01f, 1.01f);
        MODEL.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        MODEL.setupAnim(livingEntity, limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch);
    }
}
