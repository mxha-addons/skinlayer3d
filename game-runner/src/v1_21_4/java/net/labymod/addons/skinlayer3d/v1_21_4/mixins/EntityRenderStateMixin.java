package net.labymod.addons.skinlayer3d.v1_21_4.mixins;

import net.labymod.addons.skinlayer3d.v1_21_4.accessor.EntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements EntityRenderStateAccessor {

    @Unique
    private Entity skinlayer3d$entity;

    @Override
    public Entity skinlayer3d$getEntity() {
        return skinlayer3d$entity;
    }

    @Override
    public void skinlayer3d$setEntity(Entity entity) {
        this.skinlayer3d$entity = entity;
    }
}
