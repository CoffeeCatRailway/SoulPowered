package coffeecatrailway.soulpowered;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulData
{
    public static class SoulLang implements NonNullConsumer<RegistrateLangProvider>
    {
        @Override
        public void accept(RegistrateLangProvider provider)
        {
            provider.add("commands.souls.get", "%1$s has %2$s soul(s)");
            provider.add("commands.souls.set", "%1$s now has %2$s soul(s)");
        }
    }
}
