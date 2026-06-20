package dev.northclient.keybind;

import dev.northclient.hud.HudManager;
import dev.northclient.screen.HudEditorScreen;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public final class KeybindManager {
  private static KeyBinding hudEditorKey;

  private KeybindManager() {
  }

  public static void register(HudManager hudManager) {
    hudEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
      "key.northclient.hud_editor",
      InputUtil.Type.KEYSYM,
      GLFW.GLFW_KEY_RIGHT_SHIFT,
      KeyBinding.Category.create(Identifier.of("northclient", "hud"))
    ));
  }

  public static void tick(MinecraftClient client, HudManager hudManager) {
    while (hudEditorKey != null && hudEditorKey.wasPressed()) {
      client.setScreen(new HudEditorScreen(hudManager));
    }
  }
}
