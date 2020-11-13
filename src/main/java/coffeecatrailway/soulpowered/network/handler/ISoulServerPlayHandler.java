package coffeecatrailway.soulpowered.network.handler;

import coffeecatrailway.soulpowered.network.SetRedstoneModeMessage;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public interface ISoulServerPlayHandler
{
    void handleSetRedstoneModeMessage(SetRedstoneModeMessage message, NetworkEvent.Context ctx);
}
