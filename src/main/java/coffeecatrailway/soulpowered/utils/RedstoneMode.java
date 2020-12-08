package coffeecatrailway.soulpowered.utils;

import net.minecraft.util.ResourceLocation;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 * Original: https://github.com/SilentChaos512/Silents-Mechanisms
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

    public static RedstoneMode byOrdinal(int ordinal, RedstoneMode other)
    {
        for (RedstoneMode mode : values())
            if (mode.ordinal() == ordinal)
                return mode;
        return other;
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
