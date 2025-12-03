package net.labymod.addons.skinlayer3d.v1_19_4.mixins;

import net.labymod.addons.skinlayer3d.util.Constants;
import net.labymod.addons.skinlayer3d.v1_19_4.accessor.ModelPartMeshHolder;
import net.labymod.addons.skinlayer3d.v1_19_4.render.Mesh;
import net.labymod.addons.skinlayer3d.v1_19_4.util.OffsetProvider;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelPart.class, priority = 300)
public abstract class ModelPartMixin implements ModelPartMeshHolder {

    @Shadow
    public boolean visible;

    @Shadow
    public abstract void translateAndRotate(PoseStack $$0);

    @Unique
    private Mesh skinlayer3d$voxelLayer = null;
    @Unique
    private OffsetProvider skinlayer3d$offsetProvider = null;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;IIFFFF)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay,
                      float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (Constants.enabled && visible && skinlayer3d$voxelLayer != null) {
            poseStack.pushPose();
            translateAndRotate(poseStack);
            skinlayer3d$offsetProvider.applyOffset(poseStack, skinlayer3d$voxelLayer);
            skinlayer3d$voxelLayer.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
            poseStack.popPose();
            ci.cancel();
        }
    }


    @Override
    public void skinlayer3d$attachMesh(Mesh voxelMesh, OffsetProvider offsetProvider) {
        this.skinlayer3d$voxelLayer = voxelMesh;
        this.skinlayer3d$offsetProvider = offsetProvider;
    }
}
