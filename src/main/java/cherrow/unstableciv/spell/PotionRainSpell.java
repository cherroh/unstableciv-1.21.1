package cherrow.unstableciv.spell;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Server-side "spell" that rains harming splash potions from above the player.
 */
public final class PotionRainSpell {
    private static final List<ActiveRain> ACTIVE_RAINS = new ArrayList<>();
    private static final ItemStack HARMING_SPLASH_STACK =
            PotionContentsComponent.createStack(Items.SPLASH_POTION, Potions.HARMING);

    private PotionRainSpell() {
    }

    public static void trigger(ServerWorld world, ServerPlayerEntity player) {
        ACTIVE_RAINS.add(new ActiveRain(world.getRegistryKey(), player.getUuid(), FragileRodSpellConfig.DURATION_TICKS));

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

    private static void spawnHarmingPotion(ServerWorld world, double centerX, double centerZ, Random random) {
        double angle = random.nextDouble() * Math.PI * 2.0;
        double distance = random.nextDouble() * FragileRodSpellConfig.SPAWN_RADIUS;
        double x = centerX + Math.cos(angle) * distance;
        double z = centerZ + Math.sin(angle) * distance;

        double jitter = FragileRodSpellConfig.BLOCK_POSITION_JITTER;
        x += (random.nextDouble() - 0.5) * jitter;
        z += (random.nextDouble() - 0.5) * jitter;

        PotionEntity potion = new PotionEntity(world, x, FragileRodSpellConfig.SPAWN_Y, z);
        potion.setItem(HARMING_SPLASH_STACK.copy());
        world.spawnEntity(potion);
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

            if (ticksRemaining % FragileRodSpellConfig.SPAWN_INTERVAL_TICKS == 0) {
                Random random = world.getRandom();
                int count = random.nextBetween(
                        FragileRodSpellConfig.MIN_POTIONS_PER_WAVE,
                        FragileRodSpellConfig.MAX_POTIONS_PER_WAVE
                );
                double centerX = player.getX();
                double centerZ = player.getZ();
                for (int i = 0; i < count; i++) {
                    spawnHarmingPotion(world, centerX, centerZ, random);
                }
            }

            ticksRemaining--;
            return true;
        }
    }
}
