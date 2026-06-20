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
    drawPanel(context, "CPS", 0xFF4DA3FF);
    int x = (int) getBounds().x();
    int y = (int) getBounds().y();
    drawText(context, "LMB  " + cps.leftCps() + " cps", x + 10, y + 24, cps.isLeftDown() ? 0xFF22C7FF : 0xFFEAF4FF);
    drawText(context, "RMB  " + cps.rightCps() + " cps", x + 10, y + 40, cps.isRightDown() ? 0xFF22C7FF : 0xFF9FB4CC);
  }
}
