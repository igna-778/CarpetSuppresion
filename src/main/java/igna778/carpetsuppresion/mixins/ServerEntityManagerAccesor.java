package igna778.carpetsuppresion.mixins;

import net.minecraft.server.world.ServerEntityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;
import java.util.UUID;

@Mixin(ServerEntityManager.class)
public interface ServerEntityManagerAccesor {
    @Accessor
    Set<UUID>  getEntityUuids();
}
