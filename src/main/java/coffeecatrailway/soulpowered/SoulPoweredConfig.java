package coffeecatrailway.soulpowered;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class SoulPoweredConfig
{
    private static final String CONFIG = "config." + SoulPoweredMod.MOD_ID + ".";

    public static class Client
    {
        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client Configurable Settings");
            builder.pop();
        }
    }

    public static class Server
    {
        public ForgeConfigSpec.DoubleValue soulParticleSpeed;
        public ForgeConfigSpec.DoubleValue soulParticleExpireDistance;

        public Server(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Server Configurable Settings").push("soulParticle");
            this.soulParticleSpeed = builder.comment("The speed at which the particle moves")
                    .translation(CONFIG + "soulParticle.soulParticleSpeed").defineInRange("soulParticleSpeed", .5d, 0d, 10d);
            this.soulParticleExpireDistance = builder.comment("The max distance that the particle with 'stay' near a player")
                    .translation(CONFIG + "soulParticle.soulParticleExpireDistance").defineInRange("soulParticleExpireDistance", .25d, 0d, 1d);

            builder.pop();
        }
    }
}
