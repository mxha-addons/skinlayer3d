package net.labymod.addons.skinlayer3d.v1_12_2.mixins;

import net.labymod.addons.skinlayer3d.util.Constants;
import net.labymod.addons.skinlayer3d.v1_12_2.accessor.ModelPartMeshHolder;
import net.labymod.addons.skinlayer3d.v1_12_2.render.Mesh;
import net.labymod.addons.skinlayer3d.v1_12_2.util.OffsetProvider;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModelRenderer.class, priority = 300)
public abstract class ModelRendererMixin implements ModelPartMeshHolder {

    @Shadow
    public boolean showModel;

    @Shadow
    public boolean isHidden;

    @Shadow
    public float rotationPointX;

    @Shadow
    public float rotationPointY;

    @Shadow
    public float rotationPointZ;

    @Shadow
    public float rotateAngleX;

    @Shadow
    public float rotateAngleY;

    @Shadow
    public float rotateAngleZ;

    @Shadow
    public float offsetX;

    @Shadow
    public float offsetY;

    @Shadow
    public float offsetZ;

    @Unique
    private Mesh skinlayer3d$voxelLayer = null;
    @Unique
    private OffsetProvider skinlayer3d$offsetProvider = null;

    @Inject(method = "render", at = @At(value = "HEAD"))
    public void render(float scale, CallbackInfo ci) {
        if (this.showModel && Constants.enabled && skinlayer3d$voxelLayer != null && skinlayer3d$offsetProvider != null) {
            GlStateManager.pushMatrix();

            // apply offset transformations
            GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);

            // apply rotation point and rotations
            if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
                if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                }
            } else {
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);

                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleZ * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleY * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate(this.rotateAngleX * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                }
            }

            // render mesh
            skinlayer3d$offsetProvider.applyOffset(skinlayer3d$voxelLayer, scale);
            skinlayer3d$voxelLayer.render(scale);

            GlStateManager.popMatrix();
        }
    }

    @Override
    public void skinlayer3d$attachMesh(Mesh voxelMesh, OffsetProvider offsetProvider) {
        this.skinlayer3d$voxelLayer = voxelMesh;
        this.skinlayer3d$offsetProvider = offsetProvider;
    }
}
