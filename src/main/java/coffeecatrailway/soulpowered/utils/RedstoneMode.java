package coffeecatrailway.soulpowered.utils;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public enum RedstoneMode
{
    IGNORED(new ResourceLocation("textures/item/barrier.png")),
    ON(new ResourceLocation("textures/block/redstone_torch.png")),
    OFF(new ResourceLocation("textures/block/redstone_torch_off.png"));

    private final ResourceLocation texture;

    RedstoneMode(ResourceLocation texture)
    {
        this.texture = texture;
    }

    @Nullable
    public static RedstoneMode byName(String name)
    {
        for (RedstoneMode mode : values())
            if (mode.name().equalsIgnoreCase(name))
                return mode;
        return null;
    }

    public boolean shouldRun(boolean isPowered)
    {
        switch (this)
        {
            case ON:
                return isPowered;
            case OFF:
                return !isPowered;
            default:
                return true;
        }
    }

    public ResourceLocation getTexture()
    {
        return texture;
    }
}