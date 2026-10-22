package net.jahus.realmobcap.command;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.jahus.realmobcap.config.ModConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;

public final class MobcapCommand {
    private static final int MAGIC = 289;  // 17^2

    private MobcapCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("realmobcap")
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        .then(Commands.literal("show").executes(ctx -> show(ctx.getSource())))
                        .executes(ctx -> show(ctx.getSource()))
        );
    }

    private static int show(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        NaturalSpawner.SpawnState state = level.getChunkSource().getLastSpawnState();

        int chunks = state != null ? state.getSpawnableChunkCount() : 0;
        Object2IntMap<MobCategory> counts = state != null ? state.getMobCategoryCounts() : null;
        String dimId = level.dimension().identifier().toString();

        source.sendSuccess(() -> Component.literal(String.format(
                "[%s] realmobcap enabled=%s  spawnableChunks=%s",
                dimId,
                ModConfig.isEnabled(),
                state == null ? "n/a" : Integer.toString(chunks)
        )), false);

        for (MobCategory cat : MobCategory.values()) {
            if (cat == MobCategory.MISC) {
                continue;
            }

            int perChunk = cat.getMaxInstancesPerChunk();
            int count = counts != null ? counts.getInt(cat) : -1;

            String effective;
            if (perChunk < 0) {
                effective = "∞";
            } else if (state == null) {
                effective = "n/a";
            } else {
                effective = Integer.toString(perChunk * chunks / MAGIC);
            }

            String countStr = count < 0 ? "n/a" : Integer.toString(count);

            String line = String.format(
                    "  %s: %s / %s  (C=%d, f=%s, p=%s, d=%d/%d)",
                    cat.getName(),
                    countStr,
                    effective,
                    perChunk,
                    cat.isFriendly(),
                    cat.isPersistent(),
                    cat.getDespawnDistance(),
                    cat.getNoDespawnDistance()
            );

            source.sendSuccess(() -> Component.literal(line), false);
        }

        if (state == null) {
            source.sendSuccess(() -> Component.literal(
                    "  (no SpawnState yet. Wait a few ticks or move around)"
            ), false);
        }

        return 1;
    }
}
