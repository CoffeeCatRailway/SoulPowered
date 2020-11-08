package coffeecatrailway.soulpowered.common.capability;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsChangeMessage;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulsCapability
{
    public static final ResourceLocation ID = SoulPoweredMod.getLocation("souls");

    @CapabilityInject(ISoulsHandler.class)
    public static final Capability<ISoulsHandler> SOULS_CAP = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ISoulsHandler.class, new Capability.IStorage<ISoulsHandler>()
        {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ISoulsHandler> capability, ISoulsHandler instance, Direction side)
            {
                CompoundNBT nbt = new CompoundNBT();
                nbt.putInt("souls", instance.getSouls());
                return nbt;
            }

            @Override
            public void readNBT(Capability<ISoulsHandler> capability, ISoulsHandler instance, Direction side, INBT nbt)
            {
                instance.setSouls(((CompoundNBT) nbt).getInt("souls"));
            }
        }, SoulsWrapper::new);
    }

    public static class SoulsWrapper implements ISoulsHandler
    {
        int souls;
        LivingEntity owner;

        public SoulsWrapper()
        {
            this(null);
        }

        public SoulsWrapper(final LivingEntity owner)
        {
            this.souls = 0;
            this.owner = owner;
        }

        @Override
        public int getSouls()
        {
            return this.souls;
        }

        @Override
        public void setSouls(int souls)
        {
            this.souls = souls;
            if (!this.owner.world.isRemote())
                SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsTotalMessage(this.owner.getEntityId(), souls));
        }

        @Override
        public void addSouls(int amount)
        {
            if (amount >= 0)
            {
                this.souls += amount;
                if (!this.owner.world.isRemote())
                    SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsChangeMessage(this.owner.getEntityId(), amount, false));
            } else
                this.removeSouls(amount);
        }

        @Override
        public void removeSouls(int amount)
        {
            if (amount <= 0)
            {
                amount = Math.min(this.getSouls(), amount);
                this.souls -= amount;
                if (!this.owner.world.isRemote())
                    SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsChangeMessage(this.owner.getEntityId(), amount, true));
            } else
                this.addSouls(amount);
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static class Provider implements ICapabilitySerializable<INBT>
    {
        final LazyOptional<ISoulsHandler> optional;
        final ISoulsHandler handler;

        public Provider(final LivingEntity owner)
        {
            this.handler = new SoulsWrapper(owner);
            this.optional = LazyOptional.of(() -> this.handler);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
        {
            return SoulsCapability.SOULS_CAP.orEmpty(cap, this.optional);
        }

        @Override
        public INBT serializeNBT()
        {
            return SoulsCapability.SOULS_CAP.writeNBT(this.handler, null);
        }

        @Override
        public void deserializeNBT(INBT nbt)
        {
            SoulsCapability.SOULS_CAP.readNBT(this.handler, null, nbt);
        }
    }
}
