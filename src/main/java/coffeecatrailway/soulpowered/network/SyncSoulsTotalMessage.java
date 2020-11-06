package coffeecatrailway.soulpowered.network;

import coffeecatrailway.soulpowered.network.handler.ISoulClientPlayHandler;
import io.github.ocelot.sonar.common.network.message.SonarMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SyncSoulsTotalMessage implements SonarMessage<ISoulClientPlayHandler>
{
    private int owner;
    private int souls;

    public SyncSoulsTotalMessage()
    {
    }

    public SyncSoulsTotalMessage(int owner, int souls)
    {
        this.owner = owner;
        this.souls = souls;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        this.owner = buf.readInt();
        this.souls = buf.readInt();
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeInt(this.owner);
        buf.writeInt(this.souls);
    }

    @Override
    public void processPacket(ISoulClientPlayHandler handler, NetworkEvent.Context ctx)
    {
        handler.handleSyncSoulsTotalMessage(this, ctx);
    }

    public int getOwner()
    {
        return owner;
    }

    public int getSouls()
    {
        return souls;
    }
}
