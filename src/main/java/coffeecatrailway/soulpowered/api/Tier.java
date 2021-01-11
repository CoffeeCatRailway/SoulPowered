package coffeecatrailway.soulpowered.api;

import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.properties.BlockStateProperties;

import java.util.function.ToIntFunction;

/**
 * @author CoffeeCatRailway
 * Created: 21/12/2020
 */
public enum Tier
{
    SIMPLE("simple", 2f, 1.5f, .5f, .5f, .5f,
            prop -> AbstractBlock.Properties.from(Blocks.OAK_PLANKS).setLightLevel(getLightValueLit(6)), Material.WOOD),
    NORMAL("normal", 1f, 1f, 1f, 1f, 1f,
            prop -> AbstractBlock.Properties.from(Blocks.STONE).setLightLevel(getLightValueLit(10)), Material.ROCK),
    SOULIUM("soulium", .75f, .75f, 1.5f, 1.5f, 1.5f,
            prop -> prop.setRequiresTool().hardnessAndResistance(3.5f).setLightLevel(getLightValueLit(13)).sound(SoundType.METAL), Material.IRON, MaterialColor.LIGHT_GRAY);

    String id;

    float processTime;

    float powerConsumeMultiplier;
    float powerGeneratedMultiplier;

    float energyCapacity;
    float energyTransfer;

    NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties;
    Material material;
    MaterialColor materialColor;

    Tier(String id, float processTime, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacity, float energyTransfer,
         NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties, Material material)
    {
        this(id, processTime, powerMultiplier, powerGeneratedMultiplier, energyCapacity, energyTransfer, properties, material, material.getColor());
    }

    Tier(String id, float processTime, float powerMultiplier, float powerGeneratedMultiplier, float energyCapacity, float energyTransfer,
         NonNullFunction<AbstractBlock.Properties, AbstractBlock.Properties> properties, Material material, MaterialColor materialColor)
    {
        this.id = id;

        this.processTime = processTime;

        this.powerConsumeMultiplier = powerMultiplier;
        this.powerGeneratedMultiplier = powerGeneratedMultiplier;

        this.energyCapacity = energyCapacity;
        this.energyTransfer = energyTransfer;

        this.properties = properties;
        this.material = material;
        this.materialColor = materialColor;
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
        return state -> state.hasProperty(BlockStateProperties.LIT) && state.get(BlockStateProperties.LIT) ? lightValue : 0;
    }
}
