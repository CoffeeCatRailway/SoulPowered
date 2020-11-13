package coffeecatrailway.soulpowered.network;

import coffeecatrailway.soulpowered.network.handler.ISoulServerPlayHandler;
import coffeecatrailway.soulpowered.utils.RedstoneMode;
import io.github.ocelot.sonar.common.network.message.SonarMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.utils.EnumUtils;

/**
 * @author CoffeeCatRailway
 * Created: 13/11/2020
 */
public class SetRedstoneModeMessage implements SonarMessage<ISoulServerPlayHandler>
{
    private RedstoneMode mode;

    public SetRedstoneModeMessage()
    {
    }

    public SetRedstoneModeMessage(RedstoneMode mode)
    {
        this.mode = mode;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        this.mode = EnumUtils.byOrdinal(buf.readByte(), RedstoneMode.IGNORED);
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeByte(this.mode.ordinal());
    }

    @Override
    public void processPacket(ISoulServerPlayHandler handler, NetworkEvent.Context ctx)
    {
        handler.handleSetRedstoneModeMessage(this, ctx);
    }

    public RedstoneMode getMode()
    {
        return mode;
    }
}
