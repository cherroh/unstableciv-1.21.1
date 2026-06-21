package cherrow.unstableciv.spell;

import cherrow.unstableciv.entity.OrbitalFireballEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PointedDripstoneBlock;
import net.minecraft.block.enums.Thickness;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Server-side orbital strike: rains hazards from above the player.
 */
public final class OrbitalThingy {
    private static final List<ActiveRain> ACTIVE_RAINS = new ArrayList<>();
    private static final ItemStack HARMING_SPLASH_STACK =
            PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.HARMING);
    private static final BlockState FALLING_DRIPSTONE = Blocks.POINTED_DRIPSTONE.getDefaultState()
            .with(PointedDripstoneBlock.VERTICAL_DIRECTION, Direction.DOWN)
            .with(PointedDripstoneBlock.THICKNESS, Thickness.TIP);
    private static final BlockState FALLING_ANVIL = Blocks.ANVIL.getDefaultState();

    private static final int RESISTANCE_AMPLIFIER = 254;

    private enum RainType {
        POTION,
        DRIPSTONE,
        PUFFERFISH,
        ANVIL,
        FIREBALL,
        TNT_MINECART,
        CHARGED_CREEPER
    }

    private OrbitalThingy() {
    }

    public static void trigger(ServerWorld world, ServerPlayerEntity player) {
        ACTIVE_RAINS.add(new ActiveRain(world.getRegistryKey(), player.getUuid(), OrbitalSpellConfig.DURATION_TICKS));

        world.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK,
                SoundCategory.PLAYERS,
                1.25F,
                0.6F
        );
    }

    public static void tick(MinecraftServer server) {
        Iterator<ActiveRain> iterator = ACTIVE_RAINS.iterator();
        while (iterator.hasNext()) {
            ActiveRain rain = iterator.next();
            if (!rain.tick(server)) {
                iterator.remove();
            }
        }
    }

    private static double[] randomSpawnXZ(double centerX, double centerZ, Random random) {
        double angle = random.nextDouble() * Math.PI * 2.0;
        double distance = random.nextDouble() * OrbitalSpellConfig.SPAWN_RADIUS;
        double jitter = OrbitalSpellConfig.BLOCK_POSITION_JITTER;
        double x = centerX + Math.cos(angle) * distance + (random.nextDouble() - 0.5) * jitter;
        double z = centerZ + Math.sin(angle) * distance + (random.nextDouble() - 0.5) * jitter;
        return new double[]{x, z};
    }

    private static void spawnProjectile(ServerWorld world, double centerX, double centerZ, Random random) {
        double[] xz = randomSpawnXZ(centerX, centerZ, random);
        double x = xz[0];
        double z = xz[1];
        double y = OrbitalSpellConfig.SPAWN_Y;

        RainType type = RainType.values()[random.nextInt(RainType.values().length)];
        switch (type) {
            case POTION -> spawnHarmingPotion(world, x, y, z);
            case DRIPSTONE -> spawnFallingBlock(world, x, y, z, FALLING_DRIPSTONE);
            case PUFFERFISH -> spawnPufferfish(world, x, y, z, random);
            case ANVIL -> spawnFallingBlock(world, x, y, z, FALLING_ANVIL);
            case FIREBALL -> spawnDownwardFireball(world, x, y, z);
            case TNT_MINECART -> spawnTntMinecart(world, x, y, z);
            case CHARGED_CREEPER -> spawnChargedCreeper(world, x, y, z, random);
        }
    }

    private static void applyMaxResistance(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(
                StatusEffects.RESISTANCE,
                StatusEffectInstance.INFINITE,
                RESISTANCE_AMPLIFIER,
                false,
                false,
                false
        ));
    }

    private static void spawnHarmingPotion(ServerWorld world, double x, double y, double z) {
        PotionEntity potion = new PotionEntity(world, x, y, z);
        potion.setItem(HARMING_SPLASH_STACK.copy());
        world.spawnEntity(potion);
    }

    private static void spawnFallingBlock(ServerWorld world, double x, double y, double z, BlockState state) {
        BlockPos pos = BlockPos.ofFloored(x, y, z);
        FallingBlockEntity falling = FallingBlockEntity.spawnFromBlock(world, pos, state);
        falling.setPosition(x, y, z);
    }

    private static void spawnPufferfish(ServerWorld world, double x, double y, double z, Random random) {
        PufferfishEntity pufferfish = EntityType.PUFFERFISH.create(world);
        if (pufferfish == null) {
            return;
        }
        pufferfish.refreshPositionAndAngles(x, y, z, random.nextFloat() * 360.0F, 0.0F);
        applyMaxResistance(pufferfish);
        world.spawnEntity(pufferfish);
    }

    private static void spawnDownwardFireball(ServerWorld world, double x, double y, double z) {
        Vec3d velocity = new Vec3d(0.0, -OrbitalSpellConfig.FIREBALL_DOWNWARD_SPEED, 0.0);
        OrbitalFireballEntity fireball = new OrbitalFireballEntity(world, x, y, z, velocity);
        world.spawnEntity(fireball);
    }

    private static void spawnTntMinecart(ServerWorld world, double x, double y, double z) {
        TntMinecartEntity minecart = new TntMinecartEntity(world, x, y, z);
        world.spawnEntity(minecart);
    }

    private static void spawnChargedCreeper(ServerWorld world, double x, double y, double z, Random random) {
        CreeperEntity creeper = EntityType.CREEPER.create(world);
        if (creeper == null) {
            return;
        }
        creeper.refreshPositionAndAngles(x, y, z, random.nextFloat() * 360.0F, 0.0F);

        LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
        if (lightning != null) {
            lightning.refreshPositionAndAngles(x, y, z, 0.0F, 0.0F);
            creeper.onStruckByLightning(world, lightning);
        }

        applyMaxResistance(creeper);
        world.spawnEntity(creeper);
    }

    private static final class ActiveRain {
        private final RegistryKey<World> worldKey;
        private final UUID playerUuid;
        private int ticksRemaining;

        private ActiveRain(RegistryKey<World> worldKey, UUID playerUuid, int ticksRemaining) {
            this.worldKey = worldKey;
            this.playerUuid = playerUuid;
            this.ticksRemaining = ticksRemaining;
        }

        private boolean tick(MinecraftServer server) {
            if (ticksRemaining <= 0) {
                return false;
            }

            ServerWorld world = server.getWorld(worldKey);
            if (world == null) {
                return false;
            }

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);
            if (player == null || !player.isAlive() || player.getWorld() != world) {
                return false;
            }

            if (ticksRemaining % OrbitalSpellConfig.SPAWN_INTERVAL_TICKS == 0) {
                Random random = world.getRandom();
                int count = random.nextBetween(
                        OrbitalSpellConfig.MIN_PROJECTILES_PER_WAVE,
                        OrbitalSpellConfig.MAX_PROJECTILES_PER_WAVE
                );
                double centerX = player.getX();
                double centerZ = player.getZ();
                for (int i = 0; i < count; i++) {
                    spawnProjectile(world, centerX, centerZ, random);
                }
            }

            ticksRemaining--;
            return true;
        }
    }
}
