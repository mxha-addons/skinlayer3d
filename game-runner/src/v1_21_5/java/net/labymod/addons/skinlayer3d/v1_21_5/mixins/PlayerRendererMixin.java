package net.labymod.addons.skinlayer3d.v1_21_5.mixins;

import net.labymod.addons.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import net.labymod.addons.skinlayer3d.v1_21_5.accessor.ModelPartMeshHolder;
import net.labymod.addons.skinlayer3d.v1_21_5.accessor.PlayerMeshStorage;
import net.labymod.addons.skinlayer3d.v1_21_5.util.OffsetProvider;
import net.labymod.addons.skinlayer3d.v1_21_5.util.PlayerMeshBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerRenderState, PlayerModel> {

    public PlayerRendererMixin(Context context, PlayerModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHandStart(PoseStack poseStack, MultiBufferSource multiBufferSource, int val, ResourceLocation resourceLocation, ModelPart modelPart, boolean val2, CallbackInfo ci) {
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;
        ModelPart sleeve;

        sleeve = getModel().leftSleeve.equals(modelPart) ? getModel().leftSleeve : getModel().rightSleeve;

        PlayerMeshStorage settings = (PlayerMeshStorage) abstractClientPlayer;
        if (settings == null) return;

        boolean slim = ((PlayerEntityModelAccessor) getModel()).skinlayer3d$hasSlimArms();
        ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(null, null);

        if (!PlayerMeshBuilder.buildMeshes(abstractClientPlayer, settings, slim)) return;

        if (getModel().leftArm.equals(modelPart)) {
            ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_LEFT_ARM_SLIM : OffsetProvider.FIRSTPERSON_LEFT_ARM);
        } else {
            ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_RIGHT_ARM_SLIM : OffsetProvider.FIRSTPERSON_RIGHT_ARM);
        }
    }
}
