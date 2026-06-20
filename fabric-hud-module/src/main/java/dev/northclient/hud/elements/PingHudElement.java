package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public final class PingHudElement extends AbstractHudElement {
  public PingHudElement() {
    super("ping", "Ping HUD", 136, 12, 116, 42, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    int ping = readOwnPing();
    int color = ping < 0 ? mutedColor() : ping <= 80 ? successColor() : ping <= 160 ? warningColor() : errorColor();
    drawPanel(context, "Ping");
    drawText(context, ping < 0 ? "local" : ping + " ms", (int) getBounds().x() + 10, (int) getBounds().y() + 24, color);
  }

  @Override
  public String settingsHint() {
    return "Ping HUD: kendi baglanti gecikmesini gosterir; sunucu avantaj verisi yok.";
  }

  private int readOwnPing() {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null || client.getNetworkHandler() == null) return -1;
    PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
    return entry == null ? -1 : entry.getLatency();
  }
}
