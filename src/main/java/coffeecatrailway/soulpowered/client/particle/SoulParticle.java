package coffeecatrailway.soulpowered.client.particle;

import coffeecatrailway.soulpowered.SoulMod;
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
    private static final Logger LOGGER = SoulMod.getLogger("Soul-Particle");

    private final PlayerEntity player;
    private final boolean avoid;

    public SoulParticle(ClientWorld world, double x, double y, double z, IAnimatedSprite sprite, PlayerEntity player, boolean avoid)
    {
        super(world, x + offset(world), y + offset(world), z + offset(world), sprite, -5e-4f);
        this.xd = 0d;
        this.yd = 0d;
        this.zd = 0d;
        this.quadSize *= .75f;
        this.age = 30 + this.random.nextInt(12);
        this.setFadeColor(0xf2dec9);
        this.setSpriteFromAge(sprite);
        this.player = player;
        this.avoid = avoid;
    }

    public static void spawnParticles(World world, PlayerEntity player, Vector3d pos, int count, boolean avoid) {
        if (world.isClientSide()) {
            LOGGER.warn("World {} was not server side!", world);
            return;
        }
        ((ServerWorld) world).sendParticles(SoulParticleData.create(player, avoid), pos.x, pos.y, pos.z, count, 0d, 0d, 0d, 1f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.player != null) {
            float motionSpeed = SoulMod.SERVER_CONFIG.soulParticleSpeed.get().floatValue();
            Vector3d pos = new Vector3d(this.x, this.y, this.z);
            Vector3d playerPos = this.player.position().add(0f, 1f, 0f);
            Vector3d motion = pos.subtract(playerPos).normalize().multiply(motionSpeed, motionSpeed, motionSpeed).reverse();

            if (this.avoid)
                motion = motion.reverse();

            this.xd = motion.x;
            this.yd = motion.y;
            this.zd = motion.z;

            if (pos.distanceToSqr(playerPos) <= SoulMod.SERVER_CONFIG.soulParticleExpireDistance.get() * SoulMod.SERVER_CONFIG.soulParticleExpireDistance.get())
                this.remove();
        }
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    private static double offset(World world) {
        return -.5d + world.random.nextDouble();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<SoulParticleData>
    {
        private final IAnimatedSprite sprite;

        public Factory(IAnimatedSprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SoulParticleData type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SoulParticle(world, x, y, z, this.sprite, type.getPlayer(), type.doesAvoid());
        }
    }
}
