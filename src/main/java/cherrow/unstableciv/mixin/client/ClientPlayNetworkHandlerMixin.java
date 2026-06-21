package cherrow.unstableciv.mixin.client;

import cherrow.unstableciv.item.ModItems;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "getActiveTotemOfUndying", at = @At("HEAD"), cancellable = true)
    private static void unstableciv$getActiveTotemOfUndying(PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                cir.setReturnValue(stack);
                return;
            }
        }

        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.isOf(ModItems.TOTEM_OF_RESPAWNING)) {
                cir.setReturnValue(stack);
                return;
            }
        }

        cir.setReturnValue(new ItemStack(Items.TOTEM_OF_UNDYING));
    }
}
