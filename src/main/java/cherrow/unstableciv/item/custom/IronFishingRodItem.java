package cherrow.unstableciv.item.custom;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class IronFishingRodItem extends OrbitalCannonRodItem {
    private static final double TNT_PAIR_OFFSET = 0.55;

    public IronFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        Vec3d center = getSpawnPos(player);
        Vec3d side = getSideOffset(player);
        spawnLitTnt(world, player, center.add(side.multiply(TNT_PAIR_OFFSET)));
        spawnLitTnt(world, player, center.add(side.multiply(-TNT_PAIR_OFFSET)));
    }
}
