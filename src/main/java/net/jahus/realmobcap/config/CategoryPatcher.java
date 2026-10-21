package net.jahus.realmobcap.config;

import net.jahus.realmobcap.RealMobcap;
import net.minecraft.world.entity.MobCategory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Rewrite private fields on MobCategory enum constants so the spawn engine,
 * Carpet, and commands all observe the configured values.
 */
public final class CategoryPatcher {
    private CategoryPatcher() {}

    public static void apply() {
        if (!ModConfig.isEnabled()) {
            RealMobcap.LOGGER.info("CategoryPatcher skipped (disabled)");
            return;
        }

        try {
            Field maxF = findField(MobCategory.class, "max", "maxInstancesPerChunk");
            Field friendlyF = findField(MobCategory.class, "isFriendly", "friendly");
            Field persistentF = findField(MobCategory.class, "isPersistent", "persistent");
            Field despawnF = findField(MobCategory.class, "despawnDistance");

            makeWritable(maxF, friendlyF, persistentF, despawnF);

            int patched = 0;
            for (MobCategory cat : MobCategory.values()) {
                CategoryOptions o = ModConfig.get(cat.getName());
                if (o == null) {
                    o = ModConfig.get(cat.getSerializedName());
                }
                if (o == null) {
                    continue;
                }

                if (o.max != null && maxF != null) {
                    maxF.setInt(cat, o.max);
                }
                if (o.friendly != null && friendlyF != null) {
                    friendlyF.setBoolean(cat, o.friendly);
                }
                if (o.persistent != null && persistentF != null) {
                    persistentF.setBoolean(cat, o.persistent);
                }
                if (o.despawnDistance != null && despawnF != null) {
                    despawnF.setInt(cat, o.despawnDistance);
                }

                patched++;
                RealMobcap.LOGGER.info(
                        "Patched {} -> max={} (getter={})",
                        cat.getName(),
                        o.max,
                        cat.getMaxInstancesPerChunk()
                );
            }

            RealMobcap.LOGGER.info("CategoryPatcher done, {} categories", patched);
        } catch (Exception e) {
            RealMobcap.LOGGER.error("CategoryPatcher failed", e);
        }
    }

    private static Field findField(Class<?> clazz, String... names) {
        for (String name : names) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) {
            }
        }
        RealMobcap.LOGGER.warn("No field matching {} on {}", String.join("/", names), clazz.getName());
        return null;
    }

    private static void makeWritable(Field... fields) throws Exception {
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
        } catch (NoSuchFieldException ignored) {
        }

        for (Field f : fields) {
            if (f == null) continue;
            f.setAccessible(true);
            if (modifiersField != null) {
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
        }
    }
}
