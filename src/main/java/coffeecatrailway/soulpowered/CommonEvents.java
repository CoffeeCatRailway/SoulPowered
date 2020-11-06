package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
@Mod.EventBusSubscriber(modid = SoulPoweredMod.MOD_ID)
public class CommonEvents
{
    @SubscribeEvent
    public static void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof LivingEntity)
            event.addCapability(SoulsCapability.ID, new SoulsCapability.Provider((LivingEntity) event.getObject()));
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity)
            player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler ->
                    SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(player.getEntityId(), handler.getSouls())));
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event)
    {
        if (!event.isWasDeath())
            return;
        PlayerEntity original = event.getOriginal();
        PlayerEntity player = event.getPlayer();
        original.getCapability(SoulsCapability.SOULS_CAP).ifPresent(originalHandler -> player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> handler.setSouls(originalHandler.getSouls())));
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event)
    {
        Entity target = event.getTarget();
        PlayerEntity player = event.getPlayer();

        if (player instanceof ServerPlayerEntity)
            target.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> SoulMessageHandler.PLAY.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncSoulsTotalMessage(target.getEntityId(), handler.getSouls())));
    }
}
