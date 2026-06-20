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
    int color = fps >= 120 ? 0xFF35D07F : fps >= 60 ? 0xFFFFB84D : 0xFFFF5A5A;
    drawPanel(context, "FPS: " + fps);
    if (client != null) {
      context.drawTextWithShadow(client.textRenderer, "FPS: " + fps, (int) getBounds().x() + 8, (int) getBounds().y() + 8, color);
    }
  }
}
