package cherrow.unstableciv.spell;

import net.minecraft.entity.TntEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Server-side Diamond orbital strike: raycast target, center TNT aloft, expanding ring pattern.
 */
public final class DiamondOrbitalStrike {
    private static final List<ActiveStrike> ACTIVE_STRIKES = new ArrayList<>();

    private DiamondOrbitalStrike() {
    }

    public static void trigger(ServerWorld world, ServerPlayerEntity player) {
        Vec3d referencePoint = getReferencePoint(world, player);
        double spawnY = referencePoint.y + DiamondOrbitalSpellConfig.CENTER_TNT_HEIGHT_ABOVE_REFERENCE;
        Vec3d spawnPoint = new Vec3d(referencePoint.x, spawnY, referencePoint.z);

        spawnLitTnt(world, player, spawnPoint);

        List<AnimatedTnt> animatedTnt = new ArrayList<>(DiamondOrbitalSpellConfig.RING_COUNT * DiamondOrbitalSpellConfig.TNT_PER_RING);
        for (int ring = 0; ring < DiamondOrbitalSpellConfig.RING_COUNT; ring++) {
            double targetRadius = DiamondOrbitalSpellConfig.RING_RADIUS_SPACING * (ring + 1);
            spawnRing(world, player, referencePoint, spawnPoint, targetRadius, animatedTnt);
        }

        ACTIVE_STRIKES.add(new ActiveStrike(world.getRegistryKey(), referencePoint, animatedTnt, 0));
    }

    public static void tick(MinecraftServer server) {
        Iterator<ActiveStrike> iterator = ACTIVE_STRIKES.iterator();
        while (iterator.hasNext()) {
            ActiveStrike strike = iterator.next();
            if (!strike.tick(server)) {
                iterator.remove();
            }
        }
    }

    private static Vec3d getReferencePoint(ServerWorld world, ServerPlayerEntity player) {
        Vec3d eyePos = player.getEyePos();
        Vec3d endPos = eyePos.add(player.getRotationVector().multiply(DiamondOrbitalSpellConfig.RAYCAST_DISTANCE));
        BlockHitResult hit = world.raycast(new RaycastContext(
                eyePos,
                endPos,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                player
        ));

        if (hit.getType() == HitResult.Type.BLOCK) {
            return hit.getPos();
        }

        return player.getPos();
    }

    private static void spawnRing(
            ServerWorld world,
            ServerPlayerEntity player,
            Vec3d referencePoint,
            Vec3d spawnPoint,
            double targetRadius,
            List<AnimatedTnt> animatedTnt
    ) {
        for (int index = 0; index < DiamondOrbitalSpellConfig.TNT_PER_RING; index++) {
            double angle = (Math.PI * 2.0 * index) / DiamondOrbitalSpellConfig.TNT_PER_RING;
            double targetX = referencePoint.x + Math.cos(angle) * targetRadius;
            double targetZ = referencePoint.z + Math.sin(angle) * targetRadius;

            TntEntity tnt = spawnLitTnt(world, player, spawnPoint);
            animatedTnt.add(new AnimatedTnt(tnt.getUuid(), targetX, targetZ));
        }
    }

    private static TntEntity spawnLitTnt(ServerWorld world, ServerPlayerEntity player, Vec3d pos) {
        TntEntity tnt = new TntEntity(world, pos.x, pos.y, pos.z, player);
        tnt.setFuse(DiamondOrbitalSpellConfig.TNT_FUSE_TICKS);
        world.spawnEntity(tnt);
        return tnt;
    }

    private static final class AnimatedTnt {
        private final UUID entityUuid;
        private final double targetX;
        private final double targetZ;

        private AnimatedTnt(UUID entityUuid, double targetX, double targetZ) {
            this.entityUuid = entityUuid;
            this.targetX = targetX;
            this.targetZ = targetZ;
        }
    }

    private static final class ActiveStrike {
        private final RegistryKey<World> worldKey;
        private final double startX;
        private final double startZ;
        private final List<AnimatedTnt> animatedTnt;
        private int ticksElapsed;

        private ActiveStrike(
                RegistryKey<World> worldKey,
                Vec3d referencePoint,
                List<AnimatedTnt> animatedTnt,
                int ticksElapsed
        ) {
            this.worldKey = worldKey;
            this.startX = referencePoint.x;
            this.startZ = referencePoint.z;
            this.animatedTnt = animatedTnt;
            this.ticksElapsed = ticksElapsed;
        }

        private boolean tick(MinecraftServer server) {
            ServerWorld world = server.getWorld(worldKey);
            if (world == null) {
                return false;
            }

            ticksElapsed++;
            if (ticksElapsed > DiamondOrbitalSpellConfig.EXPANSION_DURATION_TICKS) {
                return false;
            }

            double progress = (double) ticksElapsed / DiamondOrbitalSpellConfig.EXPANSION_DURATION_TICKS;

            for (AnimatedTnt animated : animatedTnt) {
                TntEntity tnt = getTnt(world, animated.entityUuid);
                if (tnt == null) {
                    continue;
                }

                double x = startX + (animated.targetX - startX) * progress;
                double z = startZ + (animated.targetZ - startZ) * progress;
                tnt.setPosition(x, tnt.getY(), z);
            }

            return ticksElapsed < DiamondOrbitalSpellConfig.EXPANSION_DURATION_TICKS;
        }

        private static TntEntity getTnt(ServerWorld world, UUID uuid) {
            if (world.getEntity(uuid) instanceof TntEntity tnt) {
                return tnt;
            }
            return null;
        }
    }
}
