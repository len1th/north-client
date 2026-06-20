package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class ClockHudElement extends AbstractHudElement {
  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

  public ClockHudElement() {
    super("clock", "Saat HUD", 12, 112, 120, 42, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Clock", 0xFF22C7FF);
    drawText(context, LocalTime.now().format(FORMAT), (int) getBounds().x() + 10, (int) getBounds().y() + 24, 0xFFEAF4FF);
  }
}
