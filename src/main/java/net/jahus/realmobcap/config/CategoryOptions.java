package net.jahus.realmobcap.config;

/** Per-MobCategory overrides. Null field = keep vanilla. */
public class CategoryOptions {
    public Integer max;
    public Boolean friendly;
    public Boolean persistent;
    public Integer despawnDistance;
    public Integer noDespawnDistance;

    public CategoryOptions() {}

    public CategoryOptions(int max, boolean friendly, boolean persistent, int despawnDistance, int noDespawnDistance) {
        this.max = max;
        this.friendly = friendly;
        this.persistent = persistent;
        this.despawnDistance = despawnDistance;
        this.noDespawnDistance = noDespawnDistance;
    }
}
