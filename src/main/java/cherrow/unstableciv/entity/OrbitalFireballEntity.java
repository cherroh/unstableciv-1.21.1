package cherrow.unstableciv.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Ghast-style fireball spawned at a fixed position with no owner.
 */
public class OrbitalFireballEntity extends FireballEntity {
    public OrbitalFireballEntity(World world, double x, double y, double z, Vec3d velocity) {
        super(EntityType.FIREBALL, world);
        this.setPosition(x, y, z);
        this.setVelocity(velocity);
    }
}
