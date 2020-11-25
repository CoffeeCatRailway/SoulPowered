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

        public ForgeConfigSpec.BooleanValue canRightClickEquipCurio;

        public ForgeConfigSpec.IntValue soulAmuletPoweredExtract;

        public ForgeConfigSpec.BooleanValue oreGeneration;

        public Server(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Server Configurable Settings").push("soulParticle");
            this.soulParticleSpeed = builder.comment("The speed at which the particle moves").translation(CONFIG + "soulParticle.soulParticleSpeed")
                    .defineInRange("soulParticleSpeed", .2d, 0d, 1d);
            this.soulParticleExpireDistance = builder.comment("The max distance that the particle with 'stay' near a player").translation(CONFIG + "soulParticle.soulParticleExpireDistance")
                    .defineInRange("soulParticleExpireDistance", .25d, 0d, 1d);

            builder.pop().push("curios");
            this.canRightClickEquipCurio = builder.comment("Whether you can press right-click to equip a curios item").translation(CONFIG + "curios.canRightClickEquipCurio")
                    .define("canRightClickEquipCurio", true);

            this.soulAmuletPoweredExtract = builder.comment("The amount of power (rf/se) that is extracted from a Powered Soul Amulet").translation(CONFIG + "curios.soulAmuletPoweredExtract")
                    .defineInRange("soulAmuletPoweredExtract", 50, 0, 5000);

            builder.pop().push("world");
            this.oreGeneration = builder.comment("Whether ore generation is enabled or not").translation(CONFIG + "world.oreGeneration")
                    .define("oreGeneration", true);

            builder.pop();
        }
    }
}
