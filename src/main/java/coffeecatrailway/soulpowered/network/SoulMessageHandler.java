package coffeecatrailway.soulpowered.network;

import coffeecatrailway.soulpowered.SoulMod;
import coffeecatrailway.soulpowered.network.handler.SoulClientPlayHandler;
import coffeecatrailway.soulpowered.network.handler.SoulServerPlayHandler;
import io.github.ocelot.sonar.common.network.SonarNetworkManager;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulMessageHandler
{
    public static final String VERSION = "1.0";
    public static final SimpleChannel PLAY = NetworkRegistry.newSimpleChannel(SoulMod.getLocation("play"), () -> VERSION, VERSION::equals, VERSION::equals);

    private static final SonarNetworkManager PLAY_NETWORK_MANAGER = new SonarNetworkManager(PLAY, () -> SoulClientPlayHandler::new, () -> SoulServerPlayHandler::new);

    public static void init()
    {
        PLAY_NETWORK_MANAGER.register(SyncSoulsTotalMessage.class, SyncSoulsTotalMessage::new, NetworkDirection.PLAY_TO_CLIENT);
        PLAY_NETWORK_MANAGER.register(SyncSoulsChangeMessage.class, SyncSoulsChangeMessage::new, NetworkDirection.PLAY_TO_CLIENT);
        PLAY_NETWORK_MANAGER.register(SetRedstoneModeMessage.class, SetRedstoneModeMessage::new, NetworkDirection.PLAY_TO_SERVER);
        PLAY_NETWORK_MANAGER.register(ActivateCurioMessage.class, ActivateCurioMessage::new, NetworkDirection.PLAY_TO_SERVER);
    }
}
