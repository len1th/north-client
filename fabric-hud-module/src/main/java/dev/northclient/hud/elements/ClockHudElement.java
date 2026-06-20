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
    drawPanel(context, "Clock");
    String value = style().compact ? LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) : LocalTime.now().format(FORMAT);
    drawText(context, value, (int) getBounds().x() + 10, (int) getBounds().y() + 24, textColor());
  }

  @Override
  public String settingsHint() {
    return "Saat HUD: compact mod saniyeyi kapatir, renkler bu HUD'a ozel kaydedilir.";
  }
}
