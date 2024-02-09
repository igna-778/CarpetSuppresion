package igna778.carpetsuppresion;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.logging.HUDController;
import carpet.logging.LoggerRegistry;
import igna778.carpetsuppresion.mixins.ServerEntityManagerAccesor;
import igna778.carpetsuppresion.mixins.ServerWorldAccessor;
import igna778.carpetsuppresion.utils.CSSettings;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CarpetSuppresion implements ModInitializer, CarpetExtension {
    public static boolean __blockEvents;
    public static boolean __EntityUUID;
    @SuppressWarnings("DataFlowIssue")
    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(this);
        HUDController.register(server -> {
            if (!CarpetSuppresion.__blockEvents) return;
            int total = 0;
            int ow = ((ServerWorldAccessor) server.getWorld(World.OVERWORLD)).getSyncedBlockEventQueue().size();
            int ne = ((ServerWorldAccessor) server.getWorld(World.NETHER)).getSyncedBlockEventQueue().size();
            int end = ((ServerWorldAccessor) server.getWorld(World.END)).getSyncedBlockEventQueue().size();
            for (ServerWorld world : server.getWorlds()) {
                total += ((ServerWorldAccessor) world).getSyncedBlockEventQueue().size();
            }
            int finalTotal = total;
            LoggerRegistry.getLogger("blockEventQueue").log(option -> mapOptions(option, finalTotal, ow, ne, end));
        });
        HUDController.register(server -> {
            if (!CarpetSuppresion.__EntityUUID) return;
            int total = 0;
            int ow = ((ServerEntityManagerAccesor) ((ServerWorldAccessor) server.getWorld(World.OVERWORLD)).getEntityManager()).getEntityUuids().size();
            int ne = ((ServerEntityManagerAccesor) ((ServerWorldAccessor) server.getWorld(World.NETHER)).getEntityManager()).getEntityUuids().size();
            int end = ((ServerEntityManagerAccesor) ((ServerWorldAccessor) server.getWorld(World.END)).getEntityManager()).getEntityUuids().size();
            for (ServerWorld world : server.getWorlds()) {
                total += ((ServerEntityManagerAccesor) ((ServerWorldAccessor) world).getEntityManager()).getEntityUuids().size();
            }
            int finalTotal = total;
            LoggerRegistry.getLogger("EntityUUID").log(option -> mapOptions(option, finalTotal, ow, ne, end));
        });
    }

    public void onGameStarted()
    {
        CarpetServer.settingsManager.parseSettingsClass(CSSettings.class);
    }

    private static Text[] mapOptions(String selected, int total, int ow, int ne, int end) {
        List<String> builder = new ArrayList<>();
        if (selected.equals("all") || selected.equals("total")) builder.add("T: " + total);
        if (selected.equals("all") || selected.equals("overworld")) builder.add("OW: " + ow);
        if (selected.equals("all") || selected.equals("nether")) builder.add("NE: " + ne);
        if (selected.equals("all") || selected.equals("end")) builder.add("END: " + end);
        return new Text[]{Text.of(String.join("; ", builder.toArray(new String[]{})))};
    }

    @Override
    public void registerLoggers() {
        LoggerRegistry.registerLogger("blockEventQueue", BlockEventQueueLogger.create());
        LoggerRegistry.registerLogger("EntityUUID", EntityUuidLogger.create());
    }

}