package dev.northclient.hud;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
        }
      }
    } catch (Exception ignored) {
      save(elements);
    }
  }

  public static void save(Iterable<HudElement> elements) {
    JsonObject root = new JsonObject();
    root.addProperty("version", 1);
    root.addProperty("selectedPreset", "custom");
    root.addProperty("editorKey", "RIGHT_SHIFT");
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
      array.add(item);
    }
    root.add("elements", array);
    try {
      Files.createDirectories(layoutPath().getParent());
      Files.writeString(layoutPath(), GSON.toJson(root));
    } catch (IOException ignored) {
    }
  }
}
