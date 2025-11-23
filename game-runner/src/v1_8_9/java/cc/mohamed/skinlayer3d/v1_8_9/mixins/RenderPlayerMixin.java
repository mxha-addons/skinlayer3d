package cc.mohamed.skinlayer3d.v1_8_9.mixins;

import cc.mohamed.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import cc.mohamed.skinlayer3d.util.Constants;
import cc.mohamed.skinlayer3d.v1_8_9.accessor.ModelPartMeshHolder;
import cc.mohamed.skinlayer3d.v1_8_9.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_8_9.render.Mesh;
import cc.mohamed.skinlayer3d.v1_8_9.util.OffsetProvider;
import cc.mohamed.skinlayer3d.v1_8_9.util.SkinHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderPlayer.class)
public abstract class RenderPlayerMixin extends RendererLivingEntity<AbstractClientPlayer> {

    public RenderPlayerMixin(RenderManager renderManagerIn, ModelPlayer modelBaseIn, float shadowSizeIn) {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    @Inject(method = "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V", at = @At("HEAD"))
    public void doRender(AbstractClientPlayer player, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        ModelPlayer playerModel = (ModelPlayer) this.mainModel;
        PlayerMeshStorage settings = (PlayerMeshStorage) player;
        boolean slim = ((PlayerEntityModelAccessor) playerModel).skinlayer3d$hasSlimArms();

        skinlayer3d$clearMeshes(playerModel);

        if (!Constants.enabled) return;

        if (Minecraft.getMinecraft().thePlayer == null || player.getDistanceSqToEntity(Minecraft.getMinecraft()
                .getRenderViewEntity()) > Constants.renderDistance * Constants.renderDistance) {
            return;
        }

        if (!SkinHelper.setupPlayerMeshes(player, settings, slim)) {
            return;
        }

        ItemStack itemStack = player.getCurrentArmor(3); // head slot is 3
        if (itemStack == null || !SkinHelper.blacklistedHats.contains(itemStack.getItem())) {
            playerModel.bipedHeadwear.isHidden = true;
            skinlayer3d$attachMesh(playerModel.bipedHeadwear, settings.skinlayer3d$getHatMesh(), OffsetProvider.HEAD);
        }
        playerModel.bipedBodyWear.isHidden = true;
        skinlayer3d$attachMesh(playerModel.bipedBodyWear, settings.skinlayer3d$getTorsoMesh(), OffsetProvider.BODY);

        playerModel.bipedLeftArmwear.isHidden = true;
        skinlayer3d$attachMesh(playerModel.bipedLeftArmwear, settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.LEFT_ARM_SLIM : OffsetProvider.LEFT_ARM);

        playerModel.bipedRightArmwear.isHidden = true;
        skinlayer3d$attachMesh(playerModel.bipedRightArmwear, settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.RIGHT_ARM_SLIM : OffsetProvider.RIGHT_ARM);

        playerModel.bipedLeftLegwear.isHidden = true;
        skinlayer3d$attachMesh(playerModel.bipedLeftLegwear, settings.skinlayer3d$getLeftLegMesh(), OffsetProvider.LEFT_LEG);

        playerModel.bipedRightLegwear.isHidden = true;
        skinlayer3d$attachMesh(playerModel.bipedRightLegwear, settings.skinlayer3d$getRightLegMesh(), OffsetProvider.RIGHT_LEG);
    }

    @Unique
    private void skinlayer3d$clearMeshes(ModelPlayer model) {
        model.bipedHeadwear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedHeadwear, null, null);

        model.bipedBodyWear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedBodyWear, null, null);

        model.bipedLeftArmwear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedLeftArmwear, null, null);

        model.bipedRightArmwear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedRightArmwear, null, null);

        model.bipedLeftLegwear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedLeftLegwear, null, null);

        model.bipedRightLegwear.isHidden = false;
        skinlayer3d$attachMesh(model.bipedRightLegwear, null, null);
    }

    @Unique
    private void skinlayer3d$attachMesh(ModelRenderer modelPart, Mesh mesh, OffsetProvider offsetProvider) {
        ((ModelPartMeshHolder) modelPart).skinlayer3d$attachMesh(mesh, offsetProvider);
    }
}
