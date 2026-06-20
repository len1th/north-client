package dev.northclient.hud.elements;

import dev.northclient.hud.AbstractHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public final class ArmorHudElement extends AbstractHudElement {
  public ArmorHudElement() {
    super("armor", "Zirh HUD", 320, 88, 150, 92, true);
  }

  @Override
  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    drawPanel(context, style().compact ? "Armor" : "Armor durability");
    MinecraftClient client = MinecraftClient.getInstance();
    if (client == null || client.player == null) return;
    EquipmentSlot[] slots = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    int x = (int) getBounds().x() + 8;
    int y = (int) getBounds().y() + 24;
    for (int i = 0; i < slots.length; i++) {
      ItemStack stack = client.player.getEquippedStack(slots[i]);
      if (stack == null || stack.isEmpty()) continue;
      int rowY = y + i * 16;
      context.drawItem(stack, x, rowY);
      if (stack.isDamageable() && stack.getMaxDamage() > 0) {
        int pct = Math.max(0, 100 - Math.round((stack.getDamage() * 100f) / stack.getMaxDamage()));
        int color = pct <= 25 ? errorColor() : textColor();
        if (!style().compact) {
          drawText(context, pct + "%", x + 24, rowY + 2, color);
        }
        drawBar(context, x + (style().compact ? 24 : 62), rowY + 6, style().compact ? 120 : 82, 5, pct / 100.0f, pct <= 25 ? errorColor() : successColor());
      }
    }
  }

  @Override
  public String settingsHint() {
    return "Zirh HUD: durability bar ac/kapat, compact ikon+bar modu ve dusuk dayaniklilik uyarisi.";
  }
}
