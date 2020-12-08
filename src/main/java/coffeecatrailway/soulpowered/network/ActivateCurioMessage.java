package coffeecatrailway.soulpowered.network;

import coffeecatrailway.soulpowered.network.handler.ISoulServerPlayHandler;
import io.github.ocelot.sonar.common.network.message.SonarMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 1/12/2020
 */
public class ActivateCurioMessage implements SonarMessage<ISoulServerPlayHandler>
{
    private int slotIndex;

    public ActivateCurioMessage()
    {
    }

    public ActivateCurioMessage(int slotIndex)
    {
        this.slotIndex = slotIndex;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        this.slotIndex = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeInt(this.slotIndex);
    }

    @Override
    public void processPacket(ISoulServerPlayHandler handler, NetworkEvent.Context ctx)
    {
        handler.handleActivateCurioMessage(this, ctx);
    }

    public int getSlotIndex()
    {
        return this.slotIndex;
    }
}
