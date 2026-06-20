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
    int color = ping < 0 ? 0xFF9FB4CC : ping <= 80 ? 0xFF35D07F : ping <= 160 ? 0xFFFFB84D : 0xFFFF5A5A;
    drawPanel(context, "Ping", color);
    drawText(context, ping < 0 ? "local" : ping + " ms", (int) getBounds().x() + 10, (int) getBounds().y() + 24, color);
  }

  private int readOwnPing() {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null || client.getNetworkHandler() == null) return -1;
    PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
    return entry == null ? -1 : entry.getLatency();
  }
}
