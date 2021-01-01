package coffeecatrailway.soulpowered.api;

/**
 * @author CoffeeCatRailway
 * Created: 21/12/2020
 */
public enum Tier
{
    SIMPLE("simple", 2f, 1.5f, .5f, .5f, .5f),
    NORMAL("normal", 1f, 1f, 1f, 1f, 1f),
    SOULIUM("soulium", .75f, .75f, 1.5f, 1.5f, 1.5f);

    String id;

    float processTime;

    float powerConsumeMultiplier;
    float powerGeneratedMultiplier;

    float energyCapacity;
    float energyTransfer;

    Tier(String id, float processTime, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacity, float energyTransfer)
    {
        this.id = id;

        this.processTime = processTime;

        this.powerConsumeMultiplier = powerMultiplier;
        this.powerGeneratedMultiplier = powerGeneratedMultiplier;

        this.energyCapacity = energyCapacity;
        this.energyTransfer = energyTransfer;
    }

    public String getId()
    {
        return this.id;
    }

    public float getProcessTime()
    {
        return this.processTime;
    }

    public float getPowerConsumeMultiplier()
    {
        return this.powerConsumeMultiplier;
    }

    public float getPowerGeneratedMultiplier()
    {
        return this.powerGeneratedMultiplier;
    }

    public float getEnergyCapacity()
    {
        return this.energyCapacity;
    }

    public float getEnergyTransfer()
    {
        return this.energyTransfer;
    }
}
