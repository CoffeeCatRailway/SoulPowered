package coffeecatrailway.soulpowered.network.handler;

import coffeecatrailway.soulpowered.common.inventory.container.AbstractEnergyStorageContainer;
import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import coffeecatrailway.soulpowered.network.SetRedstoneModeMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulServerPlayHandler implements ISoulServerPlayHandler
{
    @Override
    public void handleSetRedstoneModeMessage(SetRedstoneModeMessage message, NetworkEvent.Context ctx)
    {
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            if (player != null)
            {
                if (player.openContainer instanceof AbstractEnergyStorageContainer)
                {
                    AbstractMachineTileEntity tileEntity = ((AbstractEnergyStorageContainer<?>) player.openContainer).getTileEntity();
                    tileEntity.setRedstoneMode(message.getMode());
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
