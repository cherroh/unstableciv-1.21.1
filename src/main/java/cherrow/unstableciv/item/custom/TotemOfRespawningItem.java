package cherrow.unstableciv.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/**
 * Consumed on fatal damage like a totem of undying, triggering the same particles and sounds,
 * but the entity still dies afterward.
 */
public class TotemOfRespawningItem extends Item {
    public TotemOfRespawningItem(Settings settings) {
        super(settings);
    }

    public static boolean tryUse(LivingEntity entity, DamageSource damageSource) {
        if (damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }

        ItemStack usedStack = null;
        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.getItem() instanceof TotemOfRespawningItem) {
                usedStack = stack.copy();
                break;
            }
        }

        if (usedStack == null) {
            return false;
        }

        World world = entity.getWorld();
        if (!world.isClient) {
            world.sendEntityStatus(entity, (byte) 35);
        }

        for (Hand hand : Hand.values()) {
            ItemStack stack = entity.getStackInHand(hand);
            if (stack.getItem() instanceof TotemOfRespawningItem) {
                stack.decrement(1);
                break;
            }
        }

        if (entity instanceof ServerPlayerEntity serverPlayer) {
            serverPlayer.incrementStat(Stats.USED.getOrCreateStat(usedStack.getItem()));
            entity.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        }

        return true;
    }
}
