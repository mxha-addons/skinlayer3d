package net.labymod.addons.skinlayer3d.v1_21.mixins;

import net.labymod.addons.skinlayer3d.util.Constants;
import net.labymod.addons.skinlayer3d.v1_21.accessor.ModelPartMeshHolder;
import net.labymod.addons.skinlayer3d.v1_21.render.Mesh;
import net.labymod.addons.skinlayer3d.v1_21.util.OffsetProvider;
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
    private Mesh skinlayer3d$voxelLayer = null;
    @Unique
    private OffsetProvider skinlayer3d$offsetProvider = null;

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
            at = @At("HEAD"), cancellable = true)
    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color, CallbackInfo ci) {
        if (Constants.enabled && visible && skinlayer3d$voxelLayer != null) {
            poseStack.pushPose();
            translateAndRotate(poseStack);
            skinlayer3d$offsetProvider.applyOffset(poseStack, skinlayer3d$voxelLayer);
            skinlayer3d$voxelLayer.render(poseStack, consumer, light, overlay, color);
            poseStack.popPose();
            ci.cancel();
        }
    }

    @Shadow
    public void translateAndRotate(PoseStack poseStack) {
    }

    @Override
    public void skinlayer3d$attachMesh(Mesh voxelMesh, OffsetProvider offsetProvider) {
        this.skinlayer3d$voxelLayer = voxelMesh;
        this.skinlayer3d$offsetProvider = offsetProvider;
    }
}
