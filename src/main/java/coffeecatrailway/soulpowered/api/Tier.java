package coffeecatrailway.soulpowered.api;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.NonNullFunction;

import java.util.function.ToIntFunction;

/**
 * @author CoffeeCatRailway
 * Created: 21/12/2020
 */
public enum Tier
{
    SIMPLE("simple", 2f, 1.5f, .5f, .5f, .5f,
            prop -> AbstractBlock.Properties.copy(Blocks.OAK_PLANKS).lightLevel(getLightValueLit(6)), Material.WOOD),
    NORMAL("normal", 1f, 1f, 1f, 1f, 1f,
            prop -> AbstractBlock.Properties.copy(Blocks.STONE).lightLevel(getLightValueLit(10)), Material.STONE),
    SOULIUM("soulium", .75f, .75f, 1.5f, 1.5f, 1.5f,
            prop -> prop.requiresCorrectToolForDrops().strength(3.5f).lightLevel(getLightValueLit(13)).sound(SoundType.METAL), Material.METAL, MaterialColor.COLOR_LIGHT_GRAY);

    String id;

    float processTimeMultiplier;

    float powerConsumeMultiplier;
    float powerGeneratedMultiplier;

    float energyCapacityMultiplier;
    float energyTransferMultiplier;

    NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties;
    Material material;
    MaterialColor materialColor;

    Tier(String id, float processTimeMultiplier, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacityMultiplier, float energyTransferMultiplier,
         NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties, Material material)
    {
        this(id, processTimeMultiplier, powerMultiplier, powerGeneratedMultiplier, energyCapacityMultiplier, energyTransferMultiplier, properties, material, material.getColor());
    }

    Tier(String id, float processTimeMultiplier, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacityMultiplier, float energyTransferMultiplier,
         NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties, Material material, MaterialColor materialColor)
    {
        this.id = id;

        this.processTimeMultiplier = processTimeMultiplier;

        this.powerConsumeMultiplier = powerMultiplier;
        this.powerGeneratedMultiplier = powerGeneratedMultiplier;

        this.energyCapacityMultiplier = energyCapacityMultiplier;
        this.energyTransferMultiplier = energyTransferMultiplier;

        this.properties = properties;
        this.material = material;
        this.materialColor = materialColor;
    }

    public String getId()
    {
        return this.id;
    }

    public int calculateProcessTime(float defaultTime)
    {
        return (int) (defaultTime * this.processTimeMultiplier);
    }

    public int calculatePowerConsumption(float defaultConsumedPower)
    {
        return (int) (defaultConsumedPower * this.powerConsumeMultiplier);
    }

    public int calculatePowerGenerated(float defaultGeneratedPower)
    {
        return (int) (defaultGeneratedPower * this.powerGeneratedMultiplier);
    }

    public int calculateEnergyCapacity(float defaultCapacity)
    {
        return (int) (defaultCapacity * this.energyCapacityMultiplier);
    }

    public int calculateEnergyTransfer(float defaultTransfer)
    {
        return (int) (defaultTransfer * this.energyTransferMultiplier);
    }

    public NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> getProperties()
    {
        return properties;
    }

    public Material getMaterial()
    {
        return material;
    }

    public MaterialColor getMaterialColor()
    {
        return materialColor;
    }

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue)
    {
        return state -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
