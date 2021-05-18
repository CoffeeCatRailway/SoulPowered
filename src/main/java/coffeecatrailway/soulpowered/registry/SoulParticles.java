package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.client.particle.SoulParticleData;
import coffeecatrailway.soulpowered.client.particle.SoulParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulParticles
{
    private static final Logger LOGGER = SoulMod.getLogger("Other-Registries");
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SoulMod.MOD_ID);

    public static final RegistryObject<ParticleType<SoulParticleData>> SOUL_PARTICLE = PARTICLE_TYPE.register("soul", SoulParticleType::new);

    public static void load(IEventBus bus)
    {
        PARTICLE_TYPE.register(bus);
        LOGGER.debug("Loaded");
    }
}
