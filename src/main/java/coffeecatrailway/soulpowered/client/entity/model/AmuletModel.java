package coffeecatrailway.soulpowered.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class AmuletModel extends EntityModel<LivingEntity>
{
    private final ModelRenderer frame;
    private final ModelRenderer gem;
    private final ModelRenderer chain;

    public AmuletModel()
    {
        textureWidth = 32;
        textureHeight = 16;

        frame = new ModelRenderer(this);
        frame.setRotationPoint(0f, 7.9f, 0f);
        frame.setTextureOffset(0, 0).addBox(-1.5f, -3f, -3f, 3f, 1f, 1f, 0f, false);
        frame.setTextureOffset(0, 6).addBox(-1.5f, -7f, -3f, 3f, 1f, 1f, 0f, false);
        frame.setTextureOffset(0, 2).addBox(-2.5f, -6f, -3f, 1f, 3f, 1f, 0f, false);
        frame.setTextureOffset(4, 2).addBox(1.5f, -6f, -3f, 1f, 3f, 1f, 0f, false);

        gem = new ModelRenderer(this);
        gem.setRotationPoint(0f, 7.9f, 0f);
        gem.setTextureOffset(8, 2).addBox(-1.5f, -6f, -3.25f, 3f, 3f, 1f, 0f, false);

        chain = new ModelRenderer(this);
        chain.setRotationPoint(0f, 7.9f, 0f);
        chain.setTextureOffset(0, 8).addBox(-4f, -8f, -2f, 8f, 3f, 4f, 0f, false);
    }

    @Override
    public void setRotationAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
    {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
    {
        frame.render(matrixStack, buffer, packedLight, packedOverlay);
        gem.render(matrixStack, buffer, packedLight, packedOverlay);
        chain.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}