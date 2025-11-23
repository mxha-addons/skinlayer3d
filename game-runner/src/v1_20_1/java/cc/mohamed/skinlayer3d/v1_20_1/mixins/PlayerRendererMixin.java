package cc.mohamed.skinlayer3d.v1_20_1.mixins;

import cc.mohamed.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import cc.mohamed.skinlayer3d.util.Constants;
import cc.mohamed.skinlayer3d.v1_20_1.accessor.ModelPartMeshHolder;
import cc.mohamed.skinlayer3d.v1_20_1.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_20_1.render.Mesh;
import cc.mohamed.skinlayer3d.v1_20_1.util.OffsetProvider;
import cc.mohamed.skinlayer3d.v1_20_1.util.SkinHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> playerModel, float v) {
        super(context, playerModel, v);
    }

    @Inject(method = "setModelProperties", at = @At("RETURN"))
    public void setModelProperties(AbstractClientPlayer player, CallbackInfo ci) {
        if (!Constants.enabled) return;

        PlayerModel<AbstractClientPlayer> playerModel = this.getModel();
        PlayerMeshStorage settings = (PlayerMeshStorage) player;
        boolean slim = ((PlayerEntityModelAccessor) playerModel).skinlayer3d$hasSlimArms();

        skinlayer3d$clearMeshes();

        if (Minecraft.getInstance().player == null ||
                player.distanceToSqr(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()) > Constants.renderDistance * Constants.renderDistance) {
            return;
        }

        if (!SkinHelper.setupPlayerMeshes(player, settings, slim)) {
            return;
        }

        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.HEAD);
        if (itemStack.isEmpty() || !SkinHelper.blacklistedHats.contains(itemStack.getItem())) {
            skinlayer3d$attachMesh(playerModel.hat, settings.skinlayer3d$getHatMesh(), OffsetProvider.HEAD);
        }
        skinlayer3d$attachMesh(playerModel.jacket, settings.skinlayer3d$getTorsoMesh(), OffsetProvider.BODY);
        skinlayer3d$attachMesh(playerModel.leftSleeve, settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.LEFT_ARM_SLIM : OffsetProvider.LEFT_ARM);
        skinlayer3d$attachMesh(playerModel.rightSleeve, settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.RIGHT_ARM_SLIM : OffsetProvider.RIGHT_ARM);
        skinlayer3d$attachMesh(playerModel.leftPants, settings.skinlayer3d$getLeftLegMesh(), OffsetProvider.LEFT_LEG);
        skinlayer3d$attachMesh(playerModel.rightPants, settings.skinlayer3d$getRightLegMesh(), OffsetProvider.RIGHT_LEG);
    }

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void renderHandStart(PoseStack poseStack, MultiBufferSource bufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart arm, ModelPart sleeve, CallbackInfo ci) {
        PlayerMeshStorage settings = (PlayerMeshStorage) abstractClientPlayer;
        if (settings == null) return;

        boolean slim = ((PlayerEntityModelAccessor) getModel()).skinlayer3d$hasSlimArms();
        skinlayer3d$attachMesh(sleeve, null, null);

        if (!SkinHelper.setupPlayerMeshes(abstractClientPlayer, settings, slim)) return;

        if (arm == getModel().leftArm) {
            skinlayer3d$attachMesh(sleeve, settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_LEFT_ARM_SLIM : OffsetProvider.FIRSTPERSON_LEFT_ARM);
        } else {
            skinlayer3d$attachMesh(sleeve, settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.FIRSTPERSON_RIGHT_ARM_SLIM : OffsetProvider.FIRSTPERSON_RIGHT_ARM);
        }
    }

    @Unique
    private void skinlayer3d$clearMeshes() {
        PlayerModel<AbstractClientPlayer> model = getModel();
        skinlayer3d$attachMesh(model.hat, null, null);
        skinlayer3d$attachMesh(model.jacket, null, null);
        skinlayer3d$attachMesh(model.leftSleeve, null, null);
        skinlayer3d$attachMesh(model.rightSleeve, null, null);
        skinlayer3d$attachMesh(model.leftPants, null, null);
        skinlayer3d$attachMesh(model.rightPants, null, null);
    }

    @Unique
    private void skinlayer3d$attachMesh(ModelPart modelPart, Mesh mesh, OffsetProvider offsetProvider) {
        ((ModelPartMeshHolder) (Object) modelPart).skinlayer3d$attachMesh(mesh, offsetProvider);
    }
}
