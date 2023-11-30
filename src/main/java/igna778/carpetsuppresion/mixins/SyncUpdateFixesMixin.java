package igna778.carpetsuppresion.mixins;

import igna778.carpetsuppresion.utils.CSSettings;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockEventS2CPacket;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class SyncUpdateFixesMixin extends World {

    private BlockEvent failedUpdatesCursor = null;

    @Shadow @Final private List<BlockEvent> blockEventQueue;

    @Shadow @Final
    ObjectLinkedOpenHashSet<BlockEvent> syncedBlockEventQueue;

    @Shadow @Final
    MinecraftServer server;

    protected SyncUpdateFixesMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    /**
     * @author Igna778
     * @reason Modification Needed for vanilla behaviour (Could be omitted if necessary)
     */
    @Override
    public void addSyncedBlockEvent(BlockPos pos, Block block, int type, int data) {
        BlockEvent blockEvent= new BlockEvent(pos, block, type, data);
        if (!syncedBlockEventQueue.contains(blockEvent))
            this.blockEventQueue.add(blockEvent);
    }

    /**
     * @author Igna778
     * @reason Prevents Lag form moving Block Updates Between list
     */
    @Overwrite
    private void processSyncedBlockEvents() {
        syncedBlockEventQueue.addAll(blockEventQueue);
        blockEventQueue.clear();
        processCoreSyncedBlockEvents();

    }
    private void processCoreSyncedBlockEvents(){
        BlockEvent cursor = failedUpdatesCursor;
        while (!syncedBlockEventQueue.isEmpty() && cursor != syncedBlockEventQueue.last()) {
            Iterator<BlockEvent> it = cursor == null ? syncedBlockEventQueue.iterator() : syncedBlockEventQueue.iterator(cursor);
            while (it.hasNext()) {
                BlockEvent blockEvent = it.next();
                if (this.shouldTickBlockPos(blockEvent.pos())) {
                    it.remove();
                    if (this.processBlockEvent(blockEvent))
                        this.server.getPlayerManager().sendToAround((PlayerEntity) null, (double) blockEvent.pos().getX(), (double) blockEvent.pos().getY(), (double) blockEvent.pos().getZ(), 64.0, this.getRegistryKey(), new BlockEventS2CPacket(blockEvent.pos(), blockEvent.block(), blockEvent.type(), blockEvent.data()));
                }
            }
            cursor = syncedBlockEventQueue.isEmpty() ? null : syncedBlockEventQueue.last();
            syncedBlockEventQueue.addAll(blockEventQueue);
            blockEventQueue.clear();
        }

        if (!CSSettings.doOldSyncUpdates)
            failedUpdatesCursor = cursor;
        else
            failedUpdatesCursor = null;
    }

    @Shadow
    private boolean processBlockEvent(BlockEvent event) {
        return false;
    }

}
