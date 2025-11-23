package cc.mohamed.skinlayer3d.v1_21_10.mixins;

import cc.mohamed.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import cc.mohamed.skinlayer3d.v1_21_10.accessor.ModelPartMeshHolder;
import cc.mohamed.skinlayer3d.v1_21_10.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_21_10.util.OffsetProvider;
import cc.mohamed.skinlayer3d.v1_21_10.util.SkinHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, AvatarRenderState, PlayerModel> {

    public AvatarRendererMixin(Context context, PlayerModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHandStart(PoseStack poseStack, SubmitNodeCollector multiBufferSource, int i, ResourceLocation resourceLocation, ModelPart arm, boolean bl, CallbackInfo info) {
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;
        ModelPart sleeve;
        if (arm == getModel().leftArm) {
            sleeve = getModel().leftSleeve;
        } else {
            sleeve = getModel().rightSleeve;
        }
        PlayerMeshStorage settings = (PlayerMeshStorage) abstractClientPlayer;
        if (settings == null) return;

        boolean slim = ((PlayerEntityModelAccessor) getModel()).skinlayer3d$hasSlimArms();
        ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(null, null);

        if (!SkinHelper.setupPlayerMeshes(abstractClientPlayer, settings, slim)) return;

        if (arm == getModel().leftArm) {
            ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_LEFT_ARM_SLIM : OffsetProvider.FIRSTPERSON_LEFT_ARM);
        } else {
            ((ModelPartMeshHolder) (Object) sleeve).skinlayer3d$attachMesh(settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_RIGHT_ARM_SLIM : OffsetProvider.FIRSTPERSON_RIGHT_ARM);
        }
    }
}
