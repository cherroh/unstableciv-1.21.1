package cherrow.unstableciv.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Fishing rod orbital cannon. Casting triggers an effect 3 blocks ahead of the player.
 * Works indefinitely in creative; consumes 1 durability per cast in survival.
 */
public class OrbitalCannonRodItem extends FishingRodItem {
    protected static final double SPAWN_DISTANCE = 4.0;

    public OrbitalCannonRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        boolean retrieving = user.fishHook != null;

        TypedActionResult<ItemStack> result = super.use(world, user, hand);

        if (!world.isClient && !retrieving) {
            if (user instanceof ServerPlayerEntity serverPlayer && world instanceof ServerWorld serverWorld) {
                triggerEffect(serverWorld, serverPlayer);
            }

            if (!user.getAbilities().creativeMode) {
                stack.damage(1, user, LivingEntity.getSlotForHand(hand));
            }
        }

        return result;
    }

    protected void triggerEffect(ServerWorld world, ServerPlayerEntity player) {
        spawnLitTnt(world, player, getSpawnPos(player));
    }

    protected Vec3d getSpawnPos(ServerPlayerEntity player) {
        return player.getEyePos().add(player.getRotationVector().multiply(SPAWN_DISTANCE));
    }

    protected static Vec3d getSideOffset(ServerPlayerEntity player) {
        Vec3d look = player.getRotationVector();
        return new Vec3d(-look.z, 0.0, look.x).normalize();
    }

    protected void spawnLitTnt(ServerWorld world, ServerPlayerEntity player, Vec3d pos) {
        TntEntity tnt = new TntEntity(world, pos.x, pos.y, pos.z, player);
        world.spawnEntity(tnt);
    }
}
