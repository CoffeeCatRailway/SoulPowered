package coffeecatrailway.soulpowered.common.capability;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.network.SoulMessageHandler;
import coffeecatrailway.soulpowered.network.SyncSoulsChangeMessage;
import coffeecatrailway.soulpowered.network.SyncSoulsTotalMessage;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.common.util.NonNullConsumer;
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

    public static final SoulsWrapper EMPTY = new SoulsCapability.SoulsWrapper();

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
            this.checkAmount(souls);
        }

        @Override
        public boolean addSouls(int amount, boolean force)
        {
            if (this.souls + amount <= 20 || force)
            {
                this.souls += amount;
                if (!this.owner.world.isRemote())
                    SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsChangeMessage(this.owner.getEntityId(), amount, false));
                this.checkAmount(this.souls);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeSouls(int amount, boolean force)
        {
            if (this.souls - amount >= 0 || force)
            {
                amount = Math.min(this.getSouls(), amount);
                this.souls -= amount;
                if (!this.owner.world.isRemote())
                    SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsChangeMessage(this.owner.getEntityId(), amount, true));
                this.checkAmount(this.souls);
                return true;
            }
            return false;
        }

        @Override
        public void checkAmount(int souls)
        {
            this.souls = Math.min(20, Math.max(0, souls));
            if (!this.owner.world.isRemote())
                SoulMessageHandler.PLAY.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.owner), new SyncSoulsTotalMessage(this.owner.getEntityId(), souls));
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

    public static LazyOptional<ISoulsHandler> get(Entity entity)
    {
        return entity.getCapability(SOULS_CAP, null);
    }

    public static boolean isPresent(Entity entity)
    {
        return get(entity).isPresent();
    }

    public static void ifPresent(Entity entity, NonNullConsumer<? super ISoulsHandler> consumer)
    {
        get(entity).ifPresent(consumer);
    }
}
