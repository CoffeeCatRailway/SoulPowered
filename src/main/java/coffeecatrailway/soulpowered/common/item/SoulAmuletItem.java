package coffeecatrailway.soulpowered.common.item;

import net.minecraft.entity.LivingEntity;

/**
 * @author CoffeeCatRailway
 * Created: 8/11/2020
 */
public class SoulAmuletItem extends SoulCurioItem
{
    public SoulAmuletItem(Properties properties)
    {
        super(properties);
        this.setRange(2f);
        this.setSoulGathering(1);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity)
    {
    }
}
