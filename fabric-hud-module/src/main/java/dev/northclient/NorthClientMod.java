package dev.northclient;

import dev.northclient.hud.HudManager;
import dev.northclient.keybind.KeybindManager;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class NorthClientMod implements ClientModInitializer {
  public static final String MOD_ID = "north-client-hud";

  private final HudManager hudManager = new HudManager();
  private final CpsTracker cpsTracker = new CpsTracker();
  private final KeystrokesTracker keystrokesTracker = new KeystrokesTracker();

  @Override
  public void onInitializeClient() {
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
}
