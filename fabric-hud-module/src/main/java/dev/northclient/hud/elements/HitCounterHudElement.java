package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class HitCounterHudElement extends AbstractHudElement {
  public HitCounterHudElement() {
    super("hit-counter", "Hit Sayaci", 12, 154, 132, 58, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Hit counter");
    if (style().compact) {
      drawText(context, cps.sessionLeftClicks() + " / " + cps.sessionRightClicks(), (int) getBounds().x() + 10, (int) getBounds().y() + 24, textColor());
      return;
    }
    drawText(context, "Left hits  " + cps.sessionLeftClicks(), (int) getBounds().x() + 10, (int) getBounds().y() + 24, textColor());
    drawText(context, "Right use  " + cps.sessionRightClicks(), (int) getBounds().x() + 10, (int) getBounds().y() + 40, mutedColor());
  }

  @Override
  public String settingsHint() {
    return "Hit sayaci sadece kendi sol/sag tik olcumunu tutar; otomasyon yok.";
  }
}
