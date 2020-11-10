package coffeecatrailway.soulpowered.common.capability;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public interface ISoulsHandler
{
    int getSouls();

    void setSouls(int souls);

    boolean addSouls(int amount, boolean force);

    boolean removeSouls(int amount, boolean force);

    void checkAmount(int souls);
}
