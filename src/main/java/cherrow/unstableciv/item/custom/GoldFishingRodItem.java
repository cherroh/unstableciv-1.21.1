package cherrow.unstableciv.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class GoldFishingRodItem extends OrbitalCannonRodItem {
    public GoldFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        Vec3d pos = getSpawnPos(player);
        WolfEntity wolf = EntityType.WOLF.create(world);
        if (wolf == null) {
            return;
        }

        wolf.refreshPositionAndAngles(pos.x, pos.y, pos.z, player.getYaw(), 0.0F);
        world.spawnEntity(wolf);
    }
}
