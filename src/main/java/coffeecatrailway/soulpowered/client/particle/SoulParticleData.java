package coffeecatrailway.soulpowered.client.particle;

import coffeecatrailway.soulpowered.registry.OtherRegistries;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author CoffeeCatRailway
 * Created: 6/11/2020
 */
public class SoulParticleData implements IParticleData
{
    public static final IParticleData.IDeserializer<SoulParticleData> DESERIALIZER = new IDeserializer<SoulParticleData>()
    {
        @Override
        public SoulParticleData fromCommand(ParticleType<SoulParticleData> type, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            ServerPlayerEntity player = Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayerByName(reader.readString());
            reader.expect(' ');
            boolean avoid = reader.readBoolean();
            return new SoulParticleData(player, avoid);
        }

        @Override
        public SoulParticleData fromNetwork(ParticleType<SoulParticleData> type, PacketBuffer buffer)
        {
            return new SoulParticleData((PlayerEntity) Minecraft.getInstance().level.getEntity(buffer.readVarInt()), buffer.readBoolean());
        }
    };
    public static final Codec<SoulParticleData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("player").forGetter(data -> data.player.getName().getString()),
            Codec.BOOL.fieldOf("avoid").forGetter(data -> data.avoid)
    ).apply(instance, SoulParticleData::new));

    public static SoulParticleData create(PlayerEntity player, boolean goAway) {
        return new SoulParticleData(player, goAway);
    }

    public final PlayerEntity player;
    public final boolean avoid;

    public SoulParticleData(String playerName, boolean avoid) {
        this(Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayerByName(playerName), avoid);
    }

    public SoulParticleData(PlayerEntity player, boolean avoid) {
        this.player = player;
        this.avoid = avoid;
    }

    @OnlyIn(Dist.CLIENT)
    public PlayerEntity getPlayer()
    {
        return this.player;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean doesAvoid()
    {
        return this.avoid;
    }

    @Override
    public ParticleType<?> getType()
    {
        return OtherRegistries.SOUL_PARTICLE.get();
    }

    @Override
    public void writeToNetwork(PacketBuffer buffer)
    {
        if (this.player == null)
            buffer.writeVarInt(-1);
        else
            buffer.writeVarInt(this.player.getId());
        buffer.writeBoolean(this.avoid);
    }

    @Override
    public String writeToString()
    {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + (this.player == null ? "NULL_PLAYER" : this.player.getName()) + " " + this.avoid;
    }
}
