package coffeecatrailway.soulpowered.client.entity;

import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author CoffeeCatRailway
 * Created: 5/12/2020
 */
@OnlyIn(Dist.CLIENT)
public class PosUv
{
    public Vector3f pos;

    public float u;
    public float v;

    public PosUv(float x, float y, float z, Vector2f uv)
    {
        this(new Vector3f(x, y, z), uv.x, uv.y);
    }

    public PosUv(float x, float y, float z, float u, float v)
    {
        this(new Vector3f(x, y, z), u, v);
    }

    public PosUv(Vector3f pos, float u, float v)
    {
        this.pos = pos;
        this.u = u;
        this.v = v;
    }
}
