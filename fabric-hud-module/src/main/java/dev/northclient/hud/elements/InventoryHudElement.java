package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public final class InventoryHudElement extends AbstractHudElement {
  public InventoryHudElement() {
    super("inventory", "Envanter HUD", 760, 780, 404, 128, false);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null) {
      drawPanel(context, "Inventory");
      return;
    }
    int x0 = (int) getBounds().x();
    int y0 = (int) getBounds().y();
    int width = (int) getBounds().width();
    int height = (int) getBounds().height();
    context.fill(x0, y0, x0 + width, y0 + height, 0xAA0E1522);
    drawBorder(context, x0, y0, width, height, 0xFF22C7FF);
    PlayerInventory inventory = client.player.getInventory();
    for (int slot = 9; slot < 36; slot++) {
      ItemStack stack = inventory.getStack(slot);
      if (stack == null || stack.isEmpty()) continue;
      int index = slot - 9;
      int x = x0 + 6 + (index % 9) * 18;
      int y = y0 + 8 + (index / 9) * 18;
      context.drawItem(stack, x, y);
    }
  }
}
