package net.jahus.realmobcap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.jahus.realmobcap.RealMobcap;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final Path PATH = FMLPaths.CONFIGDIR.get().resolve("realmobcap.json");

    private static Data data = Data.defaults();

    private ModConfig() {}

    public static void load() {
        if (!Files.exists(PATH)) {
            data = Data.defaults();
            save();
            RealMobcap.LOGGER.info("Wrote default config to {}", PATH);
            logLoaded();
            return;
        }
        try (Reader reader = Files.newBufferedReader(PATH)) {
            Data loaded = GSON.fromJson(reader, Data.class);
            data = loaded != null ? loaded : Data.defaults();
            if (data.categories == null) {
                data.categories = Data.defaults().categories;
            }
            logLoaded();
        } catch (Exception e) {
            RealMobcap.LOGGER.error("Failed to load config, using defaults", e);
            data = Data.defaults();
        }
    }

    private static void logLoaded() {
        RealMobcap.LOGGER.info("Config enabled={} path={}", data.enabled, PATH.toAbsolutePath());
        if (data.categories == null) return;
        for (Map.Entry<String, CategoryOptions> e : data.categories.entrySet()) {
            CategoryOptions o = e.getValue();
            RealMobcap.LOGGER.info(
                    "  {} max={} friendly={} persistent={} despawn={}/{}",
                    e.getKey(),
                    o != null ? o.max : null,
                    o != null ? o.friendly : null,
                    o != null ? o.persistent : null,
                    o != null ? o.despawnDistance : null,
                    o != null ? o.noDespawnDistance : null
            );
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            RealMobcap.LOGGER.error("Failed to save config", e);
        }
    }

    public static boolean isEnabled() {
        return data != null && data.enabled;
    }

    public static CategoryOptions get(String categoryName) {
        if (!isEnabled() || data.categories == null || categoryName == null) {
            return null;
        }
        return data.categories.get(categoryName);
    }

    public static final class Data {
        public boolean enabled = true;
        public Map<String, CategoryOptions> categories = new LinkedHashMap<>();

        static Data defaults() {
            Data d = new Data();
            d.enabled = true;
            d.categories.put("monster", new CategoryOptions(70, false, false, 128, 32));
            d.categories.put("creature", new CategoryOptions(10, true, true, 128, 32));
            d.categories.put("ambient", new CategoryOptions(15, true, false, 128, 32));
            d.categories.put("axolotls", new CategoryOptions(5, true, false, 128, 32));
            d.categories.put("underground_water_creature", new CategoryOptions(5, true, false, 128, 32));
            d.categories.put("water_creature", new CategoryOptions(5, true, false, 128, 32));
            d.categories.put("water_ambient", new CategoryOptions(20, true, false, 64, 32));
            d.categories.put("misc", new CategoryOptions(-1, true, true, 128, 32));
            return d;
        }
    }
}
