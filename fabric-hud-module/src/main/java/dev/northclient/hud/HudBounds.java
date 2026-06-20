package dev.northclient.hud;

public record HudBounds(float x, float y, float width, float height) {
  public boolean contains(double mouseX, double mouseY) {
    return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
  }
}

