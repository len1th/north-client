package dev.northclient.hud;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.northclient.config.NorthClientSettings;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class HudLayoutStorage {
  private static final Gson GSON = new Gson();

  private HudLayoutStorage() {
  }

  public static Path layoutPath() {
    return MinecraftClient.getInstance().runDirectory.toPath().resolve("north-client").resolve("hud-layout.json");
  }

  public static void apply(List<HudElement> elements) {
    if (!Files.exists(layoutPath())) return;
    try (Reader reader = Files.newBufferedReader(layoutPath())) {
      JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
      JsonArray array = root.getAsJsonArray("elements");
      if (array == null) return;
      for (JsonElement raw : array) {
        JsonObject item = raw.getAsJsonObject();
        if (item == null || !item.has("id")) continue;
        String id = item.get("id").getAsString();
        for (HudElement element : elements) {
          if (!element.getId().equals(id)) continue;
          if (item.has("enabled")) element.setEnabled(item.get("enabled").getAsBoolean());
          if (item.has("x") && item.has("y")) element.setPosition(item.get("x").getAsFloat(), item.get("y").getAsFloat());
          if (item.has("scale")) element.setScale(item.get("scale").getAsFloat());
          applyStyle(item, element.style());
        }
      }
      applyClientSettings(root);
    } catch (Exception ignored) {
      save(elements);
    }
  }

  public static void save(Iterable<HudElement> elements) {
    JsonObject root = new JsonObject();
    root.addProperty("version", 1);
    root.addProperty("selectedPreset", "custom");
    root.addProperty("editorKey", "RIGHT_SHIFT");
    JsonObject clientSettings = new JsonObject();
    clientSettings.addProperty("itemPhysicsEnabled", NorthClientSettings.itemPhysicsEnabled());
    clientSettings.addProperty("characterAnimationEnabled", NorthClientSettings.characterAnimationEnabled());
    clientSettings.addProperty("hudAnimationsEnabled", NorthClientSettings.hudAnimationsEnabled());
    root.add("clientSettings", clientSettings);
    JsonObject grid = new JsonObject();
    grid.addProperty("enabled", true);
    grid.addProperty("size", 8);
    grid.addProperty("snap", true);
    root.add("grid", grid);
    JsonArray array = new JsonArray();
    for (HudElement element : elements) {
      JsonObject item = new JsonObject();
      item.addProperty("id", element.getId());
      item.addProperty("enabled", element.isEnabled());
      item.addProperty("x", element.getBounds().x());
      item.addProperty("y", element.getBounds().y());
      item.addProperty("scale", element.getScale());
      writeStyle(item, element.style());
      array.add(item);
    }
    root.add("elements", array);
    try {
      Files.createDirectories(layoutPath().getParent());
      Files.writeString(layoutPath(), GSON.toJson(root));
    } catch (IOException ignored) {
    }
  }

  private static void applyClientSettings(JsonObject root) {
    if (!root.has("clientSettings") || !root.get("clientSettings").isJsonObject()) return;
    JsonObject settings = root.getAsJsonObject("clientSettings");
    if (settings.has("itemPhysicsEnabled")) {
      NorthClientSettings.setItemPhysicsEnabled(settings.get("itemPhysicsEnabled").getAsBoolean());
    }
    if (settings.has("characterAnimationEnabled")) {
      NorthClientSettings.setCharacterAnimationEnabled(settings.get("characterAnimationEnabled").getAsBoolean());
    }
    if (settings.has("hudAnimationsEnabled")) {
      NorthClientSettings.setHudAnimationsEnabled(settings.get("hudAnimationsEnabled").getAsBoolean());
    }
  }

  private static void applyStyle(JsonObject item, HudStyle style) {
    if (item.has("accentColor")) style.accentColor = readColor(item.get("accentColor"), style.accentColor);
    if (item.has("pressedColor")) style.pressedColor = readColor(item.get("pressedColor"), style.pressedColor);
    if (item.has("textColor")) style.textColor = readColor(item.get("textColor"), style.textColor);
    if (item.has("mutedTextColor")) style.mutedTextColor = readColor(item.get("mutedTextColor"), style.mutedTextColor);
    if (item.has("backgroundColor")) style.backgroundColor = readColor(item.get("backgroundColor"), style.backgroundColor);
    if (item.has("borderColor")) style.borderColor = readColor(item.get("borderColor"), style.borderColor);
    if (item.has("barColor")) style.barColor = readColor(item.get("barColor"), style.barColor);
    if (item.has("backgroundEnabled")) style.backgroundEnabled = item.get("backgroundEnabled").getAsBoolean();
    if (item.has("labelsEnabled")) style.labelsEnabled = item.get("labelsEnabled").getAsBoolean();
    if (item.has("barsEnabled")) style.barsEnabled = item.get("barsEnabled").getAsBoolean();
    if (item.has("compact")) style.compact = item.get("compact").getAsBoolean();
  }

  private static void writeStyle(JsonObject item, HudStyle style) {
    item.addProperty("accentColor", colorString(style.accentColor));
    item.addProperty("pressedColor", colorString(style.pressedColor));
    item.addProperty("textColor", colorString(style.textColor));
    item.addProperty("mutedTextColor", colorString(style.mutedTextColor));
    item.addProperty("backgroundColor", colorString(style.backgroundColor));
    item.addProperty("borderColor", colorString(style.borderColor));
    item.addProperty("barColor", colorString(style.barColor));
    item.addProperty("backgroundEnabled", style.backgroundEnabled);
    item.addProperty("labelsEnabled", style.labelsEnabled);
    item.addProperty("barsEnabled", style.barsEnabled);
    item.addProperty("compact", style.compact);
  }

  private static int readColor(JsonElement element, int fallback) {
    try {
      if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
        String raw = element.getAsString();
        if (raw.startsWith("#")) raw = raw.substring(1);
        long parsed = Long.parseLong(raw, 16);
        if (raw.length() <= 6) parsed |= 0xFF000000L;
        return (int) parsed;
      }
      return element.getAsInt();
    } catch (Exception ignored) {
      return fallback;
    }
  }

  private static String colorString(int color) {
    return String.format("#%08X", color);
  }
}
