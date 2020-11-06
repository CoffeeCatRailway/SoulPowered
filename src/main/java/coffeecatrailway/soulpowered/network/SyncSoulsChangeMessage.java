package coffeecatrailway.soulpowered.network;

import coffeecatrailway.soulpowered.network.handler.ISoulClientPlayHandler;
import io.github.ocelot.sonar.common.network.message.SonarMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SyncSoulsChangeMessage implements SonarMessage<ISoulClientPlayHandler>
{
    private int owner;
    private int amount;
    private boolean remove;

    public SyncSoulsChangeMessage()
    {
    }

    public SyncSoulsChangeMessage(int owner, int amount, boolean remove)
    {
        this.owner = owner;
        this.amount = amount;
        this.remove = remove;
    }

    @Override
    public void readPacketData(PacketBuffer buf)
    {
        this.owner = buf.readInt();
        this.amount = buf.readInt();
        this.remove = buf.readBoolean();
    }

    @Override
    public void writePacketData(PacketBuffer buf)
    {
        buf.writeInt(this.owner);
        buf.writeInt(this.amount);
        buf.writeBoolean(this.remove);
    }

    @Override
    public void processPacket(ISoulClientPlayHandler handler, NetworkEvent.Context ctx)
    {
        handler.handleSyncSoulsChangeMessage(this, ctx);
    }

    public int getOwner()
    {
        return owner;
    }

    public int getAmount()
    {
        return amount;
    }

    public boolean isRemove()
    {
        return remove;
    }
}
