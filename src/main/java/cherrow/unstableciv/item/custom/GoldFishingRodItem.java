package cherrow.unstableciv.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class GoldFishingRodItem extends OrbitalCannonRodItem {
    private static final int DOG_COUNT = 100;
    private static final double DOG_SPAWN_RADIUS = 5.0;
    private static final double DOG_SPACING = 0.7;

    public GoldFishingRodItem(Settings settings) {
        super(settings);
    }

    @Override
    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        List<Vec3d> spawnOffsets = buildSpawnOffsets();
        double centerX = player.getX();
        double centerY = player.getY();
        double centerZ = player.getZ();

        for (int index = 0; index < DOG_COUNT && index < spawnOffsets.size(); index++) {
            Vec3d offset = spawnOffsets.get(index);
            WolfEntity wolf = EntityType.WOLF.create(world);
            if (wolf == null) {
                continue;
            }

            wolf.refreshPositionAndAngles(
                    centerX + offset.x,
                    centerY,
                    centerZ + offset.z,
                    player.getYaw(),
                    0.0F
            );
            wolf.setTamed(true, true);
            wolf.setOwner(player);
            wolf.equipStack(EquipmentSlot.BODY, new ItemStack(Items.WOLF_ARMOR));
            wolf.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, StatusEffectInstance.INFINITE, 1, false, true));
            wolf.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, StatusEffectInstance.INFINITE, 1, false, true));
            wolf.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, StatusEffectInstance.INFINITE, 0, false, true));
            world.spawnEntity(wolf);
        }
    }

    private List<Vec3d> buildSpawnOffsets() {
        List<Vec3d> offsets = new ArrayList<>();
        double radiusSquared = DOG_SPAWN_RADIUS * DOG_SPAWN_RADIUS;

        for (double x = -DOG_SPAWN_RADIUS; x <= DOG_SPAWN_RADIUS; x += DOG_SPACING) {
            for (double z = -DOG_SPAWN_RADIUS; z <= DOG_SPAWN_RADIUS; z += DOG_SPACING) {
                if ((x * x) + (z * z) <= radiusSquared) {
                    offsets.add(new Vec3d(x, 0.0, z));
                }
            }
        }

        offsets.sort((left, right) -> Double.compare(left.lengthSquared(), right.lengthSquared()));
        return offsets;
    }
}
