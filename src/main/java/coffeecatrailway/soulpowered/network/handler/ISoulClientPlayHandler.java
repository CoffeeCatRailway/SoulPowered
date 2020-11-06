package coffeecatrailway.soulpowered.network.handler;

import coffeecatrailway.soulpowered.network.SyncSoulsChangeMessage;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public interface ISoulClientPlayHandler
{
    void handleSyncSoulsTotalMessage(SyncSoulsTotalMessage message, NetworkEvent.Context ctx);

    void handleSyncSoulsChangeMessage(SyncSoulsChangeMessage message, NetworkEvent.Context ctx);
}
