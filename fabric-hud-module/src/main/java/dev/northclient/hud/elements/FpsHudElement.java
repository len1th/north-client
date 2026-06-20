package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class FpsHudElement extends AbstractHudElement {
  public FpsHudElement() {
    super("fps", "FPS HUD", 12, 12, 96, 28, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    MinecraftClient client = MinecraftClient.getInstance();
    int fps = client != null ? client.getCurrentFps() : 0;
    int color = fps >= 120 ? successColor() : fps >= 60 ? warningColor() : errorColor();
    drawPanel(context, "FPS: " + fps);
    if (client != null) {
      String label = style().compact ? String.valueOf(fps) : "FPS: " + fps;
      context.drawTextWithShadow(client.textRenderer, label, (int) getBounds().x() + 8, (int) getBounds().y() + (style().labelsEnabled ? 18 : 8), color);
    }
  }

  @Override
  public String settingsHint() {
    return "FPS HUD: accent rengi, compact sayi modu ve dusuk FPS renk uyarisi.";
  }
}
