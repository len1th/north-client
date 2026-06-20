package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

public final class KeystrokesHudElement extends AbstractHudElement {
  public KeystrokesHudElement() {
    super("keystrokes", "Klavye HUD", 12, 420, 132, 132, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, "Keystrokes", 0xFF22C7FF);
    int x = (int) getBounds().x();
    int y = (int) getBounds().y();
    drawKey(context, "W", x + 50, y + 24, 28, 24, keys.forward);
    drawKey(context, "A", x + 20, y + 52, 28, 24, keys.left);
    drawKey(context, "S", x + 50, y + 52, 28, 24, keys.back);
    drawKey(context, "D", x + 80, y + 52, 28, 24, keys.right);
    drawKey(context, "SPACE", x + 20, y + 82, 88, 20, keys.jump);
    drawKey(context, "SHIFT", x + 20, y + 106, 42, 18, keys.sneak);
    drawKey(context, "CTRL", x + 66, y + 106, 42, 18, keys.sprint);
  }
}
