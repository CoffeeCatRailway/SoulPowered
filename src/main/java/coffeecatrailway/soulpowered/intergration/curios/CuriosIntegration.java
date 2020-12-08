package coffeecatrailway.soulpowered.intergration.curios;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.item.ISoulAmulet;
import coffeecatrailway.soulpowered.common.item.ISoulCurios;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public class CuriosIntegration
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Curios-Integration");

    public static final Supplier<Optional<ISlotType>> NECKLACE = () -> CuriosApi.getSlotHelper().getSlotType("necklace");
    public static final Supplier<Optional<ISlotType>> CHARM = () -> CuriosApi.getSlotHelper().getSlotType("charm");

    public static void onInterModComms(InterModEnqueueEvent event)
    {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("charm").build());
        LOGGER.debug("Curios slots registered");
    }

    public static ItemStack getCurioStack(PlayerEntity player, String slotName, int slot)
    {
        AtomicReference<ItemStack> artifact = new AtomicReference<>(ItemStack.EMPTY);
        if (CuriosApi.getSlotHelper().getSlotType(slotName).isPresent())
        {
            LazyOptional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(player);
            optional.ifPresent(handler -> handler.getStacksHandler(slotName).ifPresent(charms -> {
                if (charms.getStacks().getStackInSlot(slot).getItem() instanceof ISoulCurios)
                    artifact.set(charms.getStacks().getStackInSlot(slot));
            }));
        }
        return artifact.get();
    }

    public static boolean hasCurio(PlayerEntity player)
    {
        if (!NECKLACE.get().isPresent() && !CHARM.get().isPresent())
            return false;
        int i = 0;
        for (int slot = 0; slot < NECKLACE.get().get().getSize(); slot++)
        {
            ItemStack charm = getCurioStack(player, "necklace", slot);
            if (charm.getItem() instanceof ISoulCurios)
                i++;
        }
        for (int slot = 0; slot < CHARM.get().get().getSize(); slot++)
        {
            ItemStack charm = getCurioStack(player, "charm", slot);
            if (charm.getItem() instanceof ISoulCurios)
                i++;
        }
        return i != 0;
    }

    public static ICapabilityProvider getCapability(ItemStack stack)
    {
        return new ICapabilityProvider()
        {
            private final LazyOptional<ICurio> optional = LazyOptional.of(() -> new SoulCuriosWrapper(stack));

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
            {
                return CuriosCapability.ITEM.orEmpty(cap, optional);
            }
        };
    }
}
