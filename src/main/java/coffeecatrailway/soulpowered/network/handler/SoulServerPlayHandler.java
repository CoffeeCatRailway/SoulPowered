package coffeecatrailway.soulpowered.network.handler;

import coffeecatrailway.soulpowered.common.inventory.container.AbstractEnergyStorageContainer;
import coffeecatrailway.soulpowered.common.item.SoulShieldItem;
import coffeecatrailway.soulpowered.common.tileentity.AbstractMachineTileEntity;
import coffeecatrailway.soulpowered.intergration.curios.CuriosIntegration;
import coffeecatrailway.soulpowered.network.ActivateCurioMessage;
import coffeecatrailway.soulpowered.network.SetRedstoneModeMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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
                if (player.containerMenu instanceof AbstractEnergyStorageContainer)
                {
                    AbstractMachineTileEntity tileEntity = ((AbstractEnergyStorageContainer<?>) player.containerMenu).getBlockEntity();
                    tileEntity.setRedstoneMode(message.getMode());
                }
            }
        });
        ctx.setPacketHandled(true);
    }

    @Override
    public void handleActivateCurioMessage(ActivateCurioMessage message, NetworkEvent.Context ctx)
    {
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            if (player != null)
            {
                ItemStack curio = CuriosIntegration.getCurioStack(player, "charm", message.getSlotIndex());
                if (!curio.isEmpty() && curio.getItem() instanceof SoulShieldItem)
                    ((SoulShieldItem) curio.getItem()).activate(curio, true);
            }
        });
        ctx.setPacketHandled(true);
    }
}
