package coffeecatrailway.soulpowered.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.particles.ParticleType;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulParticleType extends ParticleType<SoulParticleData>
{
    public SoulParticleType()
    {
        super(false, SoulParticleData.DESERIALIZER);
    }

    @Override
    public Codec<SoulParticleData> func_230522_e_()
    {
        return SoulParticleData.CODEC;
    }
}
