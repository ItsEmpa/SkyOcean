package codes.cookies.skyocean.mixins;

import codes.cookies.skyocean.EntityRenderAccessor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderAccessor {
    @Unique
    private Entity self;

    @Override
    public void ocean$setSelf(Entity entity) {
        self = entity;
    }

    @Override
    public Entity ocean$getSelf() {
        return self;
    }
}
