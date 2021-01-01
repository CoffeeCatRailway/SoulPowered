package coffeecatrailway.soulpowered.common.block;

import coffeecatrailway.soulpowered.api.item.IEnergyItem;
import coffeecatrailway.soulpowered.common.tileentity.SoulGeneratorTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * @author CoffeeCatRailway
 * Created: 9/11/2020
 */
public class SoulGeneratorBlock extends AbstractMachineBlock implements IEnergyItem.PickableBlock
{
    public SoulGeneratorBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    protected void interactWith(World world, BlockPos pos, PlayerEntity player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof SoulGeneratorTileEntity)
            player.openContainer((SoulGeneratorTileEntity) tile);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new SoulGeneratorTileEntity();
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        if (state.get(LIT))
        {
            double d0 = (double) pos.getX() + .5d;
            double d1 = pos.getY();
            double d2 = (double) pos.getZ() + .5d;
            if (rand.nextDouble() < .1d)
                world.playSound(d0, d1, d2, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1f, 1f, false);

            Direction direction = state.get(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = rand.nextDouble() * .6d - .3d;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getXOffset() * .52d : d4;
            double d6 = rand.nextDouble() * 9d / 16d;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getZOffset() * .52d : d4;
            world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0d, 0d, 0d);
            world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d5, d1 + d6, d2 + d7, 0d, 0d, 0d);
        }
    }

    @Override
    public void onBlockPlaceBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        IEnergyItem.PickableBlock.super.onBlockPlaceBy(world, pos, state, placer, stack);
    }
}
