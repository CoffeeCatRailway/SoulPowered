package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.client.particle.SoulParticleData;
import coffeecatrailway.soulpowered.client.particle.SoulParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class OtherRegistries
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Other-Registries");
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SoulPoweredMod.MOD_ID);

    public static final RegistryObject<ParticleType<SoulParticleData>> SOUL_PARTICLE = PARTICLE_TYPE.register("soul", SoulParticleType::new);

    public static final ResourceLocation CHESTS_SOUL_CASTLE = SoulPoweredMod.getLocation("chests/soul_castle");

    public static void load(IEventBus bus)
    {
        PARTICLE_TYPE.register(bus);
        LOGGER.debug("Loaded");
    }
}
