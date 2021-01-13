package coffeecatrailway.soulpowered;

import coffeecatrailway.soulpowered.common.item.*;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * @author CoffeeCatRailway
 * Created: 21/10/2020
 */
public class SoulConfig
{
    private static final String CONFIG = "config." + SoulPoweredMod.MOD_ID + ".";

    public static class Client
    {
        public ForgeConfigSpec.DoubleValue soulShieldEndDuration;
        public ForgeConfigSpec.IntValue soulShieldParticleColor;

        public Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client Configurable Settings").push("curios").push("soulShield");
            this.soulShieldEndDuration = builder.comment("The duration of which a soul shield starts to end (fall)").comment("Stored in ticks: 20 ticks = 1 second")
                    .translation(CONFIG + "curios.soulShield.soulShieldEndDuration")
                    .defineInRange("soulShieldEndDuration", 40f, 5f, 100f);
            this.soulShieldParticleColor = builder.comment("The color of the particles a soul shield makes on creation").translation(CONFIG + "curios.soulShield.soulShieldParticleColor")
                    .defineInRange("soulShieldParticleColor", 0x60f5fa, Integer.MIN_VALUE, Integer.MAX_VALUE);

            builder.pop(2);
        }
    }

    public static class Common
    {
        public ForgeConfigSpec.BooleanValue oreGeneration;

        public ForgeConfigSpec.BooleanValue soulCastleGeneration;
        public ForgeConfigSpec.IntValue soulCastleChunksMax;
        public ForgeConfigSpec.IntValue soulCastleChunksMin;

        public Common(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Common Configurable Settings").push("world");
            this.oreGeneration = builder.comment("Whether ore generation is enabled or not").translation(CONFIG + "world.oreGeneration")
                    .define("oreGeneration", true);

            builder.push("soulCastle");
            this.soulCastleGeneration = builder.comment("Whether the soul castle is enabled or not").translation(CONFIG + "world.soulCastle.generation")
                    .define("generation", true);
            this.soulCastleChunksMax = builder.comment("The max distance between two soul castles").translation(CONFIG + "world.soulCastle.chunksMax")
                    .defineInRange("chunksMax", 10, 0, 100);
            this.soulCastleChunksMin = builder.comment("The minimum distance between two soul castles").translation(CONFIG + "world.soulCastle.chunksMin")
                    .defineInRange("chunksMin", 5, 0, 100);

            builder.pop(2);
        }
    }

    public static class Server
    {
        public ForgeConfigSpec.DoubleValue soulParticleSpeed;
        public ForgeConfigSpec.DoubleValue soulParticleExpireDistance;

        public ForgeConfigSpec.BooleanValue canRightClickEquipCurio;

        public ForgeConfigSpec.IntValue soulAmuletPoweredExtract;

        public ForgeConfigSpec.IntValue soulShieldSoulsUse;

        public ForgeConfigSpec.DoubleValue soulShieldBounceOffset;

        public ForgeConfigSpec.DoubleValue poweredSouliumSwordSoulChance;
        public ForgeConfigSpec.IntValue poweredSouliumSwordEnergyCost;
        public ForgeConfigSpec.IntValue poweredSouliumSwordEffectEnergyAmount;

        public Server(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Server Configurable Settings").push("particle");
            this.soulParticleSpeed = builder.comment("The speed at which the particle moves").translation(CONFIG + "particle.soulParticleSpeed")
                    .defineInRange("soulParticleSpeed", .2d, 0d, 1d);
            this.soulParticleExpireDistance = builder.comment("The max distance that the particle with 'stay' near a player").translation(CONFIG + "particle.soulParticleExpireDistance")
                    .defineInRange("soulParticleExpireDistance", .25d, 0d, 1d);

            builder.pop().push("curios");
            this.canRightClickEquipCurio = builder.comment("Whether you can press right-click to equip a curios item").translation(CONFIG + "curios.canRightClickEquipCurio")
                    .define("canRightClickEquipCurio", true);

            this.soulAmuletPoweredExtract = builder.comment("The amount of power (rf/se) that is extracted from a Powered Soul Amulet").translation(CONFIG + "curios.soulAmuletPoweredExtract")
                    .defineInRange("soulAmuletPoweredExtract", 50, 0, 5000);

            builder.push("soulShield");
            this.soulShieldSoulsUse = builder.comment("The amount of soul(s) that is extracted from the player").translation(CONFIG + "curios.soulShield.soulShieldSoulsUse")
                    .defineInRange("soulShieldSoulsUse", 2, 0, 20);

            this.soulShieldBounceOffset = builder.comment("How close a projectile appears to bounce off a Soul Shield").translation(CONFIG + "curios.soulShield.shieldBounceOffset")
                    .defineInRange("shieldBounceOffset", .5d, 0d, 1d);

            builder.pop(2).push("toolsAndWeapons").push("poweredSoulium").push("sword");
            this.poweredSouliumSwordSoulChance = builder.comment("The chance that the player attacking will gain soul(s)").translation(CONFIG + "toolsAndWeapons.poweredSoulium.sword.soulChance")
                    .defineInRange("soulChance", .25d, 0d, 1d);
            this.poweredSouliumSwordEnergyCost = builder.comment("The amount of energy consumed when used", "Note: Doubled for special use and block breaking").translation(CONFIG + "toolsAndWeapons.poweredSoulium.sword.energyCost")
                    .defineInRange("energyCost", 1, 0, PoweredSouliumSwordItem.CAPACITY);
            this.poweredSouliumSwordEffectEnergyAmount = builder.comment("The amount of energy needed for special properties").translation(CONFIG + "toolsAndWeapons.poweredSoulium.sword.effectEnergyAmount")
                    .defineInRange("effectEnergyAmount", PoweredSouliumSwordItem.CAPACITY / 4, 0, PoweredSouliumSwordItem.CAPACITY);

            builder.pop(3);
        }
    }
}
