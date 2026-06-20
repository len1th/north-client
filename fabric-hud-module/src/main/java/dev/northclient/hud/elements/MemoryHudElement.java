package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class MemoryHudElement extends AbstractHudElement {
  public MemoryHudElement() {
    super("memory", "Bellek HUD", 12, 214, 156, 54, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    Runtime runtime = Runtime.getRuntime();
    long used = (runtime.totalMemory() - runtime.freeMemory()) / 1024L / 1024L;
    long max = runtime.maxMemory() / 1024L / 1024L;
    float ratio = max <= 0 ? 0.0f : used / (float) max;
    int color = ratio > 0.85f ? 0xFFFF5A5A : ratio > 0.65f ? 0xFFFFB84D : 0xFF35D07F;
    drawPanel(context, "Memory", color);
    drawText(context, used + " / " + max + " MB", (int) getBounds().x() + 10, (int) getBounds().y() + 24, 0xFFEAF4FF);
    drawBar(context, (int) getBounds().x() + 10, (int) getBounds().y() + 40, 136, 5, ratio, color);
  }
}
