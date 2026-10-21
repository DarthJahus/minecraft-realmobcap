# Real Mobcap

Simple **server-side** mobcap control for Minecraft (Fabric).

Patches `MobCategory` so the spawn engine, Carpet, and the built-in command all see the same values.  
JSON config only; no Cloth, no ModMenu, no Fabric API.

Inspired by the idea behind [Proper Mobcap Modifier](https://github.com/vlad2305m/Proper-Mobcap-Modifier-Fabric) (getter overrides for spawn groups), reimplemented for 26.3 with field patching and a diagnostic command (inspired by Cloth).

## Config
`config/realmobcap.json` (written on first run with vanilla defaults)

```json
{
  "enabled": true,
  "categories": {
    "monster": {
      "max": 70,
      "friendly": false,
      "persistent": false,
      "despawnDistance": 128,
      "noDespawnDistance": 32
    },
    "creature": { "max": 10, "friendly": true, "persistent": true, "despawnDistance": 128, "noDespawnDistance": 32 },
    "ambient": { "max": 15, "friendly": true, "persistent": false, "despawnDistance": 128, "noDespawnDistance": 32 },
    "axolotls": { "max": 5, "friendly": true, "persistent": false, "despawnDistance": 128, "noDespawnDistance": 32 },
    "underground_water_creature": { "max": 5, "friendly": true, "persistent": false, "despawnDistance": 128, "noDespawnDistance": 32 },
    "water_creature": { "max": 5, "friendly": true, "persistent": false, "despawnDistance": 128, "noDespawnDistance": 32 },
    "water_ambient": { "max": 20, "friendly": true, "persistent": false, "despawnDistance": 64, "noDespawnDistance": 32 },
    "misc": { "max": -1, "friendly": true, "persistent": true, "despawnDistance": 128, "noDespawnDistance": 32 }
  }
}
```

Omit any field to keep the vanilla value. **Restart** after editing.

## Command
```text
/realmobcap
/realmobcap show
```

Op level 2. Shows count / effective cap for the current dimension (same formula as the spawn engine), plus per-chunk max and flags.

## License

Apache 2.0

