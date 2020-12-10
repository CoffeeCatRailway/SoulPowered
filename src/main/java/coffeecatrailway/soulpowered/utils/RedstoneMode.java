package coffeecatrailway.soulpowered.utils;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 * Original: https://github.com/SilentChaos512/Silents-Mechanisms
 */
public enum RedstoneMode
{
    IGNORED, ON, OFF;

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
}
