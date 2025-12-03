package net.labymod.addons.skinlayer3d.v1_21_10.mixins;

import net.labymod.addons.skinlayer3d.util.Constants;
import net.labymod.addons.skinlayer3d.v1_21_10.accessor.ModelPartMeshHolder;
import net.labymod.addons.skinlayer3d.v1_21_10.render.Mesh;
import net.labymod.addons.skinlayer3d.v1_21_10.util.OffsetProvider;
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
public class ModelPartMixin implements ModelPartMeshHolder {

    @Shadow
    public boolean visible;

    @Unique
    private Mesh skinlayer3d$attachedMesh = null;
    @Unique
    private OffsetProvider skinlayer3d$offsetProvider = null;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void render(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, int color, CallbackInfo ci) {
        if (Constants.enabled && visible && skinlayer3d$attachedMesh != null) {
            poseStack.pushPose();
            skinlayer3d$offsetProvider.applyOffset(poseStack, skinlayer3d$attachedMesh);
            skinlayer3d$attachedMesh.render(poseStack, vertexConsumer, light, overlay, color);
            poseStack.popPose();
            ci.cancel();
        }

    }

    @Override
    public void skinlayer3d$attachMesh(Mesh voxelMesh, OffsetProvider offsetProvider) {
        this.skinlayer3d$attachedMesh = voxelMesh;
        this.skinlayer3d$offsetProvider = offsetProvider;
    }
}
