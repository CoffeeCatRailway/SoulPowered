package coffeecatrailway.soulpowered.client.particle;

import coffeecatrailway.soulpowered.SoulPoweredMod;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
@OnlyIn(Dist.CLIENT)
public class SoulParticle extends SimpleAnimatedParticle
{
    private static final Logger LOGGER = SoulPoweredMod.getLogger("Soul-Particle");

    private final PlayerEntity player;
    private final boolean avoid;

    public SoulParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite sprite, PlayerEntity player, boolean avoid)
    {
        super(world, x + offset(world), y + offset(world), z + offset(world), sprite, -5E-4F);
        this.motionX = 0d;
        this.motionY = 0d;
        this.motionZ = 0d;
        this.particleScale *= .75f;
        this.maxAge = 30 + this.rand.nextInt(12);
        this.setColorFade(0xf2dec9);
        this.selectSpriteWithAge(sprite);
        this.player = player;
        this.avoid = avoid;
    }

    public static void spawnParticles(World world, PlayerEntity player, Vector3d pos, int count, boolean goAway) {
        if (world.isRemote()) {
            LOGGER.warn("World {} was not server side!", world);
            return;
        }
        ((ServerWorld) world).spawnParticle(SoulParticleData.create(player, goAway), pos.x, pos.y, pos.z, count, 0d, 0d, 0d, 1f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.player != null) {
            float motionSpeed = SoulPoweredMod.SERVER_CONFIG.soulParticleSpeed.get().floatValue();
            Vector3d pos = new Vector3d(this.posX, this.posY, this.posZ);
            Vector3d playerPos = this.player.getPositionVec().add(0f, 1f, 0f);
            Vector3d motion = pos.subtract(playerPos).normalize().mul(motionSpeed, motionSpeed, motionSpeed).inverse();

            if (this.avoid)
                motion = motion.inverse();

            this.motionX = motion.x;
            this.motionY = motion.y;
            this.motionZ = motion.z;

            if (pos.squareDistanceTo(playerPos) <= SoulPoweredMod.SERVER_CONFIG.soulParticleExpireDistance.get() * SoulPoweredMod.SERVER_CONFIG.soulParticleExpireDistance.get())
                this.setExpired();
        }
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.resetPositionToBB();
    }

    private static double offset(World world) {
        return -.5d + world.rand.nextDouble() * (.5d - -.5d);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<SoulParticleData>
    {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle makeParticle(SoulParticleData type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SoulParticle(world, x, y, z, this.sprite, type.getPlayer(), type.doesAvoid());
        }
    }
}
