package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.client.particle.SoulParticle;
import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.common.item.SoulCurioItem;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import coffeecatrailway.soulpowered.registry.SoulItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.concurrent.atomic.AtomicInteger;

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

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity player = event.player;
        player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> {
            if (handler.getSouls() == 20 && !player.world.isRemote())
                    SoulParticle.spawnParticles(player.world, player, player.getPositionVec().add(0d, 2d, 0d), 1, true);
        });
    }

    @SubscribeEvent
    public static void onEntityDie(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            World world = player.world;

            if (CuriosIntegration.hasCurio(player, "necklace").isEmpty())
                return;

            player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(playerHandler -> {
                LivingEntity entity = event.getEntityLiving();
                AtomicInteger soulCount = new AtomicInteger(1);
                if (entity instanceof PlayerEntity)
                    entity.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> soulCount.set(player.world.rand.nextInt(handler.getSouls() / 2) + 1));
                else if (entity != null)
                    soulCount.set(1);

                for (int slot = 0; slot < CuriosApi.getSlotHelper().getSlotType("necklace").get().getSize(); slot++)
                {
                    ItemStack charm = CuriosIntegration.getCurioStack(player, "necklace", slot);
                    if (charm.getItem() instanceof SoulCurioItem)
                        soulCount.set(soulCount.get() * charm.getOrCreateTag().getInt("SoulGathering"));
                }

                playerHandler.addSouls(soulCount.get(), false);
                if (!world.isRemote)
                    SoulParticle.spawnParticles(world, player, entity.getPositionVec().add(0d, 1d, 0d), soulCount.get() + player.world.getRandom().nextInt(3) + 1, false);
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        boolean creativeFlag = !player.isCreative();

        if (!(stack.getItem() instanceof GlassBottleItem) || (CuriosIntegration.hasCurio(player, "necklace").isEmpty() && creativeFlag))
            return;

        player.getCapability(SoulsCapability.SOULS_CAP).ifPresent(handler -> {
            if (handler.getSouls() > 1)
            {
                if (creativeFlag && handler.removeSouls(2, false))
                    stack.shrink(1);
                player.addItemStackToInventory(new ItemStack(SoulItems.SOUL_BOTTLE.get()));
            }
        });
    }
}
