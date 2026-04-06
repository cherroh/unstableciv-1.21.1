package cherrow.unstableciv.item.custom;

import cherrow.unstableciv.client.PinkEffectClient;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Map;

public class HighItem extends Item {
    public HighItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            PinkEffectClient.toggle();
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
