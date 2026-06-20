package dev.northclient.tracker;

import net.minecraft.client.MinecraftClient;

public final class KeystrokesTracker {
  public boolean forward;
  public boolean left;
  public boolean back;
  public boolean right;
  public boolean jump;
  public boolean sneak;
  public boolean sprint;

  public void tick(MinecraftClient client) {
    if (client.options == null) return;
    forward = client.options.forwardKey.isPressed();
    left = client.options.leftKey.isPressed();
    back = client.options.backKey.isPressed();
    right = client.options.rightKey.isPressed();
    jump = client.options.jumpKey.isPressed();
    sneak = client.options.sneakKey.isPressed();
    sprint = client.options.sprintKey.isPressed();
  }
}

