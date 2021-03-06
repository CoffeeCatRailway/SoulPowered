package coffeecatrailway.soulpowered.api.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;

import java.util.Set;

/**
 * @author CoffeeCatRailway
 * Created: 13/01/2021
 */
public abstract class PoweredModifierToolItem extends PoweredToolItem
{
    public PoweredModifierToolItem(float attackDamage, float attackSpeed, IItemTier tier, Set<Block> effectiveBlocks, Properties properties, ToolType toolType)
    {
        super(attackDamage, attackSpeed, tier, effectiveBlocks, properties, toolType);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        if (this.isFoil(context.getItemInHand()))
        {
            Direction face = context.getClickedFace();
            Set<BlockPos> positions = Sets.newHashSet();
            BlockPos.betweenClosedStream(this.getOffsetToFaceDirection(face, -1, -1, context.getClickedPos()), this.getOffsetToFaceDirection(face, 1, 1, context.getClickedPos())).forEach(pos ->
                    positions.add(pos.immutable()));

            positions.forEach(blockpos -> this.modifyAtPosition(context, blockpos));
            return ActionResultType.sidedSuccess(context.getLevel().isClientSide());
        }
        return this.hasEnergy(context.getItemInHand()) ? this.modifyAtPosition(context, context.getClickedPos()) : ActionResultType.PASS;
    }

    protected abstract ActionResultType modifyAtPosition(ItemUseContext context, BlockPos pos);

    private BlockPos getOffsetToFaceDirection(Direction face, int x, int y, BlockPos pos)
    {
        switch (face)
        {
            case DOWN:
            case UP:
                return pos.offset(new BlockPos(x, 0, y));
            default:
            case NORTH:
            case SOUTH:
                return pos.offset(new BlockPos(x, y, 0));
            case WEST:
            case EAST:
                return pos.offset(new BlockPos(0, y, x));
        }
    }
}
