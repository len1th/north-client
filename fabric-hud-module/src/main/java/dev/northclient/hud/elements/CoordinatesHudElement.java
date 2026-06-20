package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesHudElement extends AbstractHudElement {
  public CoordinatesHudElement() {
    super("coordinates", "Koordinat HUD", 12, 270, 160, 58, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Coordinates");
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null) return;
    int x = (int) Math.floor(client.player.getX());
    int y = (int) Math.floor(client.player.getY());
    int z = (int) Math.floor(client.player.getZ());
    if (style().compact) {
      drawText(context, x + " " + y + " " + z, (int) getBounds().x() + 10, (int) getBounds().y() + 24, textColor());
      return;
    }
    drawText(context, "X " + x + "  Y " + y, (int) getBounds().x() + 10, (int) getBounds().y() + 24, textColor());
    drawText(context, "Z " + z, (int) getBounds().x() + 10, (int) getBounds().y() + 40, mutedColor());
  }

  @Override
  public String settingsHint() {
    return "Koordinat HUD sadece kendi konumunu gosterir; compact tek satir modu var.";
  }
}
