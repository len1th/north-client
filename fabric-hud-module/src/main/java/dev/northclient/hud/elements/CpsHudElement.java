package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class CpsHudElement extends AbstractHudElement {
  public CpsHudElement() {
    super("cps", "CPS HUD", 12, 360, 132, 62, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "CPS");
    int x = (int) getBounds().x();
    int y = (int) getBounds().y();
    if (style().compact) {
      drawText(context, cps.leftCps() + " | " + cps.rightCps(), x + 10, y + 24, cps.isLeftDown() || cps.isRightDown() ? pressedColor() : textColor());
      return;
    }
    drawText(context, "LMB  " + cps.leftCps() + " cps", x + 10, y + 24, cps.isLeftDown() ? pressedColor() : textColor());
    drawText(context, "RMB  " + cps.rightCps() + " cps", x + 10, y + 40, cps.isRightDown() ? pressedColor() : mutedColor());
  }

  @Override
  public String settingsHint() {
    return "CPS HUD: tiklayinca kullanilan basili renk ve compact L/R gorunumu.";
  }
}
