package coffeecatrailway.soulpowered.client.entity;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.entity.SoulShieldEntity;
import coffeecatrailway.soulpowered.common.item.SoulShieldItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author CoffeeCatRailway
 * Created: 5/12/2020
 */
@OnlyIn(Dist.CLIENT)
public class SoulShieldRenderer extends EntityRenderer<SoulShieldEntity>
{
    private static final ResourceLocation SHIELD_TEXTURE = SoulMod.getLocation("textures/misc/shield.png");

    private static final Set<SoulShieldEntity> TO_RENDER = new HashSet<>();
    private static List<PosUv> POS_UVS = new ArrayList<>();

    public SoulShieldRenderer(EntityRendererManager renderManager)
    {
        super(renderManager);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void addPosUvs(List<PosUv> posUvs, float range)
    {
        float oneDivThree = 1f / 3f;
        float oneDivThreeT2 = oneDivThree * 2f;
        float oneDivThreeT3 = oneDivThree * 3f;
        float angle = 360f / 6f;

        for (int i = 0; i < 2; i++)
        {
            float halfMul = i != 0 ? -1f : 1f;
            float halfAdd = i != 0 ? 1.3f : 0f;

            Vector2f topPos = new Vector2f(0f, range - 3.975f);
            Vector2f topPosNext = topPos;

            Vector2f topMidPos = new Vector2f(0f, range - 1.55f);
            Vector2f topMidPosNext = topMidPos;

            Vector2f bottomMidPos = new Vector2f(0f, range - .5f);
            Vector2f bottomMidPosNext = bottomMidPos;

            Vector2f bottomPos = new Vector2f(0f, range);
            Vector2f bottomPosNext = bottomPos;

            for (int j = 0; j < 6; j++)
            {
                topPosNext = rotatePoint(topPosNext, Vector2f.ZERO, angle);
                topMidPosNext = rotatePoint(topMidPosNext, Vector2f.ZERO, angle);
                bottomMidPosNext = rotatePoint(bottomMidPosNext, Vector2f.ZERO, angle);
                bottomPosNext = rotatePoint(bottomPosNext, Vector2f.ZERO, angle);

                posUvs.add(new PosUv(topPosNext.x, range * halfMul + halfAdd, topPosNext.y, 0f, 0f));
                posUvs.add(new PosUv(topPos.x, range * halfMul + halfAdd, topPos.y, oneDivThree, 0f));
                posUvs.add(new PosUv(topMidPos.x, (range - .75f) * halfMul + halfAdd, topMidPos.y, oneDivThree, oneDivThree));
                posUvs.add(new PosUv(topMidPosNext.x, (range - .75f) * halfMul + halfAdd, topMidPosNext.y, 0f, oneDivThree));

                posUvs.add(new PosUv(topMidPosNext.x, (range - .75f) * halfMul + halfAdd, topMidPosNext.y, oneDivThree, oneDivThree));
                posUvs.add(new PosUv(topMidPos.x, (range - .75f) * halfMul + halfAdd, topMidPos.y, oneDivThreeT2, oneDivThree));
                posUvs.add(new PosUv(bottomMidPos.x, (range - 1.85f) * halfMul + halfAdd, bottomMidPos.y, oneDivThreeT2, oneDivThreeT2));
                posUvs.add(new PosUv(bottomMidPosNext.x, (range - 1.85f) * halfMul + halfAdd, bottomMidPosNext.y, oneDivThree, oneDivThreeT2));

                posUvs.add(new PosUv(bottomMidPosNext.x, (range - 1.85f) * halfMul + halfAdd, bottomMidPosNext.y, oneDivThreeT2, oneDivThreeT2));
                posUvs.add(new PosUv(bottomMidPos.x, (range - 1.85f) * halfMul + halfAdd, bottomMidPos.y, oneDivThreeT3, oneDivThreeT2));
                posUvs.add(new PosUv(bottomPos.x, (range - 3.35f) * halfMul + halfAdd, bottomPos.y, oneDivThreeT3, oneDivThreeT3));
                posUvs.add(new PosUv(bottomPosNext.x, (range - 3.35f) * halfMul + halfAdd, bottomPosNext.y, oneDivThreeT2, oneDivThreeT3));

                topPos = rotatePoint(topPos, Vector2f.ZERO, angle);
                topMidPos = rotatePoint(topMidPos, Vector2f.ZERO, angle);
                bottomMidPos = rotatePoint(bottomMidPos, Vector2f.ZERO, angle);
                bottomPos = rotatePoint(bottomPos, Vector2f.ZERO, angle);
            }
        }
    }

    public IVertexBuilder getVertexBuilder(SoulShieldEntity shieldEntity, IRenderTypeBuffer typeBuffer)
    {
        return typeBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(shieldEntity)));
    }

    public List<PosUv> getPosUvs(float range)
    {
        if (POS_UVS.isEmpty())
            this.addPosUvs(POS_UVS, range);
        return POS_UVS;
    }

    @SubscribeEvent
    public void onReload(TagsUpdatedEvent event)
    {
        if (!POS_UVS.isEmpty())
            POS_UVS = new ArrayList<>();
    }

    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event)
    {
        World world = Minecraft.getInstance().level;
        MatrixStack matrix = event.getMatrixStack();
        IRenderTypeBuffer.Impl typeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
        if (world == null)
            return;

        IVertexBuilder buffer = null;
        Vector3d view = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition();

        matrix.pushPose();
        matrix.translate(-view.x(), -view.y(), -view.z());
        for (SoulShieldEntity shieldEntity : TO_RENDER)
        {
            ItemStack stack = shieldEntity.getShieldStack();
            int packedLight = WorldRenderer.getLightColor(world, new BlockPos(shieldEntity.position()));
            if (!stack.isEmpty())
            {
                if (buffer == null)
                    buffer = this.getVertexBuilder(shieldEntity, typeBuffer);

                matrix.pushPose();
                matrix.translate(shieldEntity.xOld + (shieldEntity.getX() - shieldEntity.xOld) * event.getPartialTicks(), shieldEntity.yOld + (shieldEntity.getY() - shieldEntity.yOld) * event.getPartialTicks(), shieldEntity.zOld + (shieldEntity.getZ() - shieldEntity.zOld) * event.getPartialTicks());

                this.render(stack, packedLight, buffer, matrix);

                matrix.popPose();
            }
        }
        matrix.popPose();
        RenderSystem.disableCull();
        typeBuffer.endBatch();
        RenderSystem.enableCull();
        TO_RENDER.clear();
    }

    public void render(ItemStack stack, int packedLight, IVertexBuilder buffer, MatrixStack matrix)
    {
        Matrix4f matrixLast = matrix.last().pose();
        float range = stack.getOrCreateTag().getFloat("Range");

        List<PosUv> posUvs = this.getPosUvs(range);
        if (!posUvs.isEmpty())
        {
            for (int i = 0; i < posUvs.size(); i += 4)
            {
                Color color = SoulShieldItem.getDustColor();
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int a = color.getAlpha();

                buffer.vertex(matrixLast, posUvs.get(i).pos.x(), posUvs.get(i).pos.y(), posUvs.get(i).pos.z()).color(r, g, b, a).uv(posUvs.get(i).u, posUvs.get(i).v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0f, 1f, 0f).endVertex(); // TODO: Fix normals
                buffer.vertex(matrixLast, posUvs.get(i + 1).pos.x(), posUvs.get(i + 1).pos.y(), posUvs.get(i + 1).pos.z()).color(r, g, b, a).uv(posUvs.get(i + 1).u, posUvs.get(i + 1).v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
                buffer.vertex(matrixLast, posUvs.get(i + 2).pos.x(), posUvs.get(i + 2).pos.y(), posUvs.get(i + 2).pos.z()).color(r, g, b, a).uv(posUvs.get(i + 2).u, posUvs.get(i + 2).v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
                buffer.vertex(matrixLast, posUvs.get(i + 3).pos.x(), posUvs.get(i + 3).pos.y(), posUvs.get(i + 3).pos.z()).color(r, g, b, a).uv(posUvs.get(i + 3).u, posUvs.get(i + 3).v)
                        .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(0f, 1f, 0f).endVertex();
            }
        }
    }

    @Override
    public void render(SoulShieldEntity shieldEntity, float entityYaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int packedLight)
    {
        super.render(shieldEntity, entityYaw, partialTicks, matrix, typeBuffer, packedLight);
        ItemStack stack = shieldEntity.getShieldStack();
        if (!stack.isEmpty())
        {
            matrix.pushPose();

            matrix.scale(.75f, .75f, .75f);
            matrix.mulPose(Vector3f.YP.rotationDegrees(180f - shieldEntity.yRot));

            boolean hasEnded = this.isEnding(shieldEntity);
            if (hasEnded)
                shieldEntity.deadAngle = MathHelper.lerp(0.05f, shieldEntity.deadAngle, -90f);
            matrix.mulPose(Vector3f.XP.rotationDegrees(shieldEntity.deadAngle));

            if (!hasEnded)
                shieldEntity.yOffset = (float) Math.sin((shieldEntity.tickCount + partialTicks) / 4.5f) / 48f;
            else
                shieldEntity.yOffset = MathHelper.lerp(0.025f, shieldEntity.yOffset, -.25f);
            matrix.translate(0f, .75f + shieldEntity.yOffset, 0f);

            Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, packedLight, OverlayTexture.NO_OVERLAY, matrix, typeBuffer);

            matrix.popPose();
        }
        TO_RENDER.add(shieldEntity);
    }

    @Override
    public boolean shouldRender(SoulShieldEntity shieldEntity, ClippingHelper camera, double camX, double camY, double camZ)
    {
        if (super.shouldRender(shieldEntity, camera, camX, camY, camZ))
            return true;
        AxisAlignedBB box = shieldEntity.getBoundingBox();
        float range = shieldEntity.getShieldStack().getOrCreateTag().getFloat("Range");
        return camera.isVisible(box.expandTowards(range, range, range));
    }

    private boolean isEnding(SoulShieldEntity shieldEntity)
    {
        return shieldEntity.tickCount >= shieldEntity.getDuration() - SoulMod.CLIENT_CONFIG.soulShieldEndDuration.get().floatValue();
    }

    public static Vector2f rotatePoint(Vector2f point, Vector2f origin, float angle)
    {
        double radians = Math.toRadians(angle);
        float sin = (float) Math.sin(radians);
        float cos = (float) Math.cos(radians);

        Vector2f newPoint = new Vector2f(point.x - origin.x, point.y - origin.y);
        return new Vector2f(newPoint.x * cos + newPoint.y * sin + origin.x, -newPoint.x * sin + newPoint.y * cos + origin.y);
    }

    @Override
    public ResourceLocation getTextureLocation(SoulShieldEntity entity)
    {
        return SHIELD_TEXTURE;
    }
}
