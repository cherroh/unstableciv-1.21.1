package cherrow.unstableciv.client;

import cherrow.unstableciv.mixin.GameRendererMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class PinkEffectClient {
    private static boolean enabled = false;

    public static void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public static void enable() {
        MinecraftClient client = MinecraftClient.getInstance();
        GameRenderer renderer = client.gameRenderer;

        try {
            ((GameRendererMixin) renderer).invokeLoadPostProcessor(
                    Identifier.of("unstableciv", "shaders/post/pink.json")
            );
            enabled = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.gameRenderer.disablePostProcessor();
        enabled = false;
    }
}