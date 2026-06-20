package dev.northclient.tracker;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public final class CpsTracker {
  private final Deque<Long> leftClicks = new ArrayDeque<>();
  private final Deque<Long> rightClicks = new ArrayDeque<>();
  private boolean leftDown;
  private boolean rightDown;
  private int sessionLeftClicks;
  private int sessionRightClicks;

  public void tick(MinecraftClient client) {
    if (client == null || client.getWindow() == null) return;
    long now = System.currentTimeMillis();
    long handle = client.getWindow().getHandle();
    boolean currentLeft = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
    boolean currentRight = GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
    if (currentLeft && !leftDown) {
      leftClicks.addLast(now);
      sessionLeftClicks++;
    }
    if (currentRight && !rightDown) {
      rightClicks.addLast(now);
      sessionRightClicks++;
    }
    leftDown = currentLeft;
    rightDown = currentRight;
    trim(leftClicks, now);
    trim(rightClicks, now);
  }

  public int leftCps() {
    return leftClicks.size();
  }

  public int rightCps() {
    return rightClicks.size();
  }

  public boolean isLeftDown() {
    return leftDown;
  }

  public boolean isRightDown() {
    return rightDown;
  }

  public int sessionLeftClicks() {
    return sessionLeftClicks;
  }

  public int sessionRightClicks() {
    return sessionRightClicks;
  }

  private void trim(Deque<Long> clicks, long now) {
    while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000L) {
      clicks.removeFirst();
    }
  }
}
