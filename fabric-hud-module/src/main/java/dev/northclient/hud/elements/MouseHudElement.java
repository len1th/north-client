package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class MouseHudElement extends AbstractHudElement {
  public MouseHudElement() {
    super("mouse", "Mouse HUD", 12, 300, 128, 56, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Mouse");
    int x = (int) getBounds().x();
    int y = (int) getBounds().y();
    if (style().compact) {
      drawKey(context, "L", x + 10, y + 22, 48, 22, cps.isLeftDown());
      drawKey(context, "R", x + 66, y + 22, 48, 22, cps.isRightDown());
      return;
    }
    drawKey(context, "LMB", x + 8, y + 24, 52, 24, cps.isLeftDown());
    drawKey(context, "RMB", x + 68, y + 24, 52, 24, cps.isRightDown());
  }

  @Override
  public String settingsHint() {
    return "Mouse HUD: LMB/RMB basili rengi, kompakt L/R modu ve animasyon.";
  }
}
