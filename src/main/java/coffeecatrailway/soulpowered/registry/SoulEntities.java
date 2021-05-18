package coffeecatrailway.soulpowered.registry;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.common.entity.SoulShieldEntity;
import coffeecatrailway.soulpowered.data.gen.SoulLanguage;
import io.github.ocelot.sonar.common.item.SpawnEggItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author CoffeeCatRailway
 * Created: 5/12/2020
 */
public class SoulEntities
{
    private static final Logger LOGGER = SoulMod.getLogger("Entities");
    protected static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, SoulMod.MOD_ID);

    public static final RegistryObject<EntityType<SoulShieldEntity>> SOUL_SHIELD = register("soul_shield", SoulShieldEntity::new, EntityClassification.MISC, builder -> builder.sized(1f, 1f).setShouldReceiveVelocityUpdates(false));

    private static <E extends Entity> RegistryObject<EntityType<E>> register(String id, BiFunction<EntityType<E>, World, E> entityFactory, EntityClassification classification, Function<EntityType.Builder<E>, EntityType.Builder<E>> factory)
    {
        RegistryObject<EntityType<E>> object = ENTITIES.register(id, () -> factory.apply(EntityType.Builder.<E>of(entityFactory::apply, classification)).build(SoulMod.getLocation(id).toString()));
        SoulLanguage.ENTITIES.put(object, SoulLanguage.capitalize(id));
//        SoulItems.registerIdAsName(id + "_spawn_egg", prop -> new SpawnEggItemBase<>(object, 0xffffff, 0x7a3205, true, prop));
        return object;
    }

    public static void load(IEventBus bus)
    {
        ENTITIES.register(bus);
        LOGGER.debug("Loaded");
    }
}
