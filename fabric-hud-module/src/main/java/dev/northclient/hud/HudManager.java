package dev.northclient.hud;

import dev.northclient.hud.elements.ArmorHudElement;
import dev.northclient.hud.elements.CpsHudElement;
import dev.northclient.hud.elements.EffectsHudElement;
import dev.northclient.hud.elements.FpsHudElement;
import dev.northclient.hud.elements.InventoryHudElement;
import dev.northclient.hud.elements.KeystrokesHudElement;
import dev.northclient.hud.elements.MouseHudElement;
import dev.northclient.tracker.CpsTracker;
import dev.northclient.tracker.KeystrokesTracker;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public final class HudManager {
  private final List<HudElement> elements = new ArrayList<>();

  public void load() {
    elements.clear();
    elements.add(new FpsHudElement());
    elements.add(new KeystrokesHudElement());
    elements.add(new MouseHudElement());
    elements.add(new CpsHudElement());
    elements.add(new ArmorHudElement());
    elements.add(new EffectsHudElement());
    elements.add(new InventoryHudElement());
    HudLayoutStorage.apply(elements);
  }

  public List<HudElement> elements() {
    return elements;
  }

  public void render(DrawContext context, float tickDelta, CpsTracker cps, KeystrokesTracker keys) {
    for (HudElement element : elements) {
      if (element.isEnabled()) {
        element.render(context, tickDelta, cps, keys);
      }
    }
  }

  public void save() {
    HudLayoutStorage.save(elements);
  }
}
