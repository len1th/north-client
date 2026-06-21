package dev.northclient;

import dev.northclient.hud.HudManager;
import dev.northclient.keybind.KeybindManager;
import dev.northclient.screen.HudEditorScreen;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class NorthClientMod implements ClientModInitializer {
  public static final String MOD_ID = "north-client-hud";
  private static HudManager activeHudManager;

  private final HudManager hudManager = new HudManager();
  private final CpsTracker cpsTracker = new CpsTracker();
  private final KeystrokesTracker keystrokesTracker = new KeystrokesTracker();

  @Override
  public void onInitializeClient() {
    activeHudManager = hudManager;
    hudManager.load();
    KeybindManager.register(hudManager);
    HudRenderCallback.EVENT.register((context, tickDelta) ->
      hudManager.render(context, tickDelta.getTickProgress(false), cpsTracker, keystrokesTracker)
    );
    ClientTickEvents.END_CLIENT_TICK.register(client -> {
      cpsTracker.tick(client);
      keystrokesTracker.tick(client);
      KeybindManager.tick(client, hudManager);
    });
  }

  public static void openHudEditor(MinecraftClient client) {
    if (client == null) return;
    HudManager manager = activeHudManager;
    if (manager == null) {
      manager = new HudManager();
      manager.load();
      activeHudManager = manager;
    }
    client.setScreen(new HudEditorScreen(manager));
  }
}
