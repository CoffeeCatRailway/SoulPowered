package coffeecatrailway.soulpowered.api;

/**
 * @author CoffeeCatRailway
 * Created: 21/12/2020
 */
public enum Tier
{
    SIMPLE(2f, 1.5f, .5f, .5f, .5f),
    SOULIUM(1f, 1f, 1f, 1f, 1f);

    float processTime;

    float powerConsumeMultiplier;
    float powerGeneratedMultiplier;

    float energyCapacity;
    float energyTransfer;

    Tier(float processTime, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacity, float energyTransfer)
    {
        this.processTime = processTime;

        this.powerConsumeMultiplier = powerMultiplier;
        this.powerGeneratedMultiplier = powerGeneratedMultiplier;

        this.energyCapacity = energyCapacity;
        this.energyTransfer = energyTransfer;
    }

    public float getProcessTime()
    {
        return processTime;
    }

    public float getPowerConsumeMultiplier()
    {
        return powerConsumeMultiplier;
    }

    public float getPowerGeneratedMultiplier()
    {
        return powerGeneratedMultiplier;
    }

    public float getEnergyCapacity()
    {
        return energyCapacity;
    }

    public float getEnergyTransfer()
    {
        return energyTransfer;
    }
}
