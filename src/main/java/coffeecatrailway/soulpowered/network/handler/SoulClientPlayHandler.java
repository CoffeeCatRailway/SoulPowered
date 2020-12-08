package coffeecatrailway.soulpowered.network.handler;

import coffeecatrailway.soulpowered.common.capability.SoulsCapability;
import coffeecatrailway.soulpowered.network.SyncSoulsChangeMessage;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulClientPlayHandler implements ISoulClientPlayHandler
{
    @Override
    public void handleSyncSoulsTotalMessage(SyncSoulsTotalMessage message, NetworkEvent.Context ctx)
    {
        ctx.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(message.getOwner());
            if (entity != null)
                SoulsCapability.ifPresent(entity, handler -> handler.setSouls(message.getSouls()));
        });
        ctx.setPacketHandled(true);
    }

    @Override
    public void handleSyncSoulsChangeMessage(SyncSoulsChangeMessage message, NetworkEvent.Context ctx)
    {
        ctx.enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(message.getOwner());
            if (entity != null)
                SoulsCapability.ifPresent(entity, handler -> {
                    if (message.isRemove())
                        handler.removeSouls(message.getAmount(), false);
                    else
                        handler.addSouls(message.getAmount(), false);
                });
        });
        ctx.setPacketHandled(true);
    }
}
