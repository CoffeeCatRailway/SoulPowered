package coffeecatrailway.soulpowered.common.entity;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import coffeecatrailway.soulpowered.common.item.SoulShieldItem;
import coffeecatrailway.soulpowered.registry.SoulEntities;
import coffeecatrailway.soulpowered.registry.SoulItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * @author CoffeeCatRailway
 * Created: 5/12/2020
 */
public class SoulShieldEntity extends Entity
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("SoulShieldEntity");
    private static final Vector3d VEC_TWO = new Vector3d(2d, 2d, 2d);

    private static final DataParameter<ItemStack> SHIELD_STACK = EntityDataManager.createKey(SoulShieldEntity.class, DataSerializers.ITEMSTACK);

    @OnlyIn(Dist.CLIENT)
    public float deadAngle = 0f;
    @OnlyIn(Dist.CLIENT)
    public float yOffset = 0f;

    private final Supplier<SoulShieldItem> soulShieldSupplier = SoulItems.SOUL_SHIELD;

    public SoulShieldEntity(EntityType<?> type, World world)
    {
        super(type, world);
    }

    public SoulShieldEntity(World world, PlayerEntity player, ItemStack stack, float durationInTicks)
    {
        super(SoulEntities.SOUL_SHIELD.get(), world);
        this.setPosition(player.getPositionVec().x, player.getPositionVec().y, player.getPositionVec().z);
        this.setShieldStack(stack.copy());
        this.setDuration(durationInTicks);
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!this.world.isRemote)
            if (this.ticksExisted >= this.getDuration())
                this.remove();


        ItemStack shieldStack = this.getShieldStack();
        if (!shieldStack.isEmpty())
        {
            double shieldBounceOffset = SoulPoweredMod.SERVER_CONFIG.soulShieldBounceOffset.get();
            double range = shieldStack.getOrCreateTag().getFloat("Range") + shieldBounceOffset;

            this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().grow(range - shieldBounceOffset))
                    .stream().filter(entity -> entity instanceof ProjectileEntity).forEach(projectile -> {
                if (projectile.getPositionVec().squareDistanceTo(this.getPositionVec()) <= range * range)
                {
                    Vector3d n = projectile.getPositionVec().subtract(this.getPositionVec()).normalize();
                    Vector3d d = projectile.getMotion().normalize();
                    double dot = d.dotProduct(n);
                    projectile.setMotion(d.subtract(VEC_TWO.mul(dot, dot, dot).mul(n)).mul(d.length(), d.length(), d.length()));
                }
            });
        }
    }

    @Override
    protected void registerData()
    {
        this.getDataManager().register(SHIELD_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void readAdditional(CompoundNBT nbt)
    {
        if (nbt.contains("ShieldStack", Constants.NBT.TAG_COMPOUND))
            this.setShieldStack(ItemStack.read(nbt.getCompound("ShieldStack")));

        ItemStack shieldStack = this.getShieldStack();
        CompoundNBT stackNbt = shieldStack.getOrCreateTag();
        if (stackNbt.contains("DurationInTicks", Constants.NBT.TAG_ANY_NUMERIC))
            this.setDuration(stackNbt.getFloat("DurationInTicks"));
    }

    @Override
    protected void writeAdditional(CompoundNBT nbt)
    {
        ItemStack shieldStack = this.getShieldStack();
        CompoundNBT stackNbt = shieldStack.getOrCreateTag();
        if (!stackNbt.contains("DurationInTicks", Constants.NBT.TAG_ANY_NUMERIC))
            stackNbt.putFloat("DurationInTicks", this.getDuration());

        if (!nbt.contains("ShieldStack", Constants.NBT.TAG_COMPOUND))
            nbt.put("ShieldStack", shieldStack.write(new CompoundNBT()));
    }

    public ItemStack getShieldStack()
    {
        ItemStack shieldStack = this.getDataManager().get(SHIELD_STACK);
        if (shieldStack.getItem() != this.soulShieldSupplier.get())
        {
            if (this.world != null)
                LOGGER.warn("SoulShieldEntity {} doesn't have a shield stack?!", this.getUniqueID());
            return this.setShieldStack(new ItemStack(this.soulShieldSupplier.get()));
        } else
            return shieldStack;
    }

    public ItemStack setShieldStack(ItemStack stack)
    {
        ItemStack newStack = stack.copy();
        this.getDataManager().set(SHIELD_STACK, newStack);
        return newStack;
    }

    public float getDuration()
    {
        ItemStack shieldStack = this.getDataManager().get(SHIELD_STACK);
        CompoundNBT stackNbt = shieldStack.getOrCreateTag();
        if (stackNbt.contains("DurationInTicks", Constants.NBT.TAG_ANY_NUMERIC))
            return stackNbt.getFloat("DurationInTicks");
        return 0f;
    }

    public void setDuration(float duration)
    {
        ItemStack shieldStack = this.getDataManager().get(SHIELD_STACK);
        shieldStack.getOrCreateTag().putFloat("DurationInTicks", duration);
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
