package coffeecatrailway.soulpowered.common.capability;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public interface ISoulsHandler
{
    int getSouls();

    void setSouls(int souls);

    void addSouls(int amount);

    void removeSouls(int amount);
}
