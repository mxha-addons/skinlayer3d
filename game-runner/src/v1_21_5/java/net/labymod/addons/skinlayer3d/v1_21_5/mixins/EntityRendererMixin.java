package net.labymod.addons.skinlayer3d.v1_21_5.mixins;

import net.labymod.addons.skinlayer3d.v1_21_5.accessor.EntityRenderStateAccessor;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
            at = @At("HEAD"))
    private void captureEntity(T entity, S state, float partialTick, CallbackInfo ci) {
        if (state instanceof EntityRenderStateAccessor accessor) {
            accessor.skinlayer3d$setEntity(entity);
        }
    }
}
