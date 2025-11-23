package cc.mohamed.skinlayer3d.v1_21_8.mixins;

import cc.mohamed.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import cc.mohamed.skinlayer3d.util.Constants;
import cc.mohamed.skinlayer3d.v1_21_8.accessor.ModelPartMeshHolder;
import cc.mohamed.skinlayer3d.v1_21_8.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_21_8.render.Mesh;
import cc.mohamed.skinlayer3d.v1_21_8.util.OffsetProvider;
import cc.mohamed.skinlayer3d.v1_21_8.util.SkinHelper;
import net.labymod.v1_21_8.client.util.EntityRenderStateAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel implements PlayerEntityModelAccessor {

    public PlayerModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Final
    @Shadow
    public ModelPart leftSleeve;

    @Final
    @Shadow
    public ModelPart rightSleeve;

    @Final
    @Shadow
    public ModelPart leftPants;

    @Final
    @Shadow
    public ModelPart rightPants;

    @Final
    @Shadow
    public ModelPart jacket;

    @Final
    @Shadow
    private boolean slim;

    @Override
    public boolean skinlayer3d$hasSlimArms() {
        return slim;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/PlayerRenderState;)V", at = @At("TAIL"))
    public void setupAnim(PlayerRenderState playerRenderState, CallbackInfo ci) {
        if (!Constants.enabled) return;
        if (!(playerRenderState instanceof EntityRenderStateAccessor<?> accessor) || !(accessor.labyMod$getEntity() instanceof AbstractClientPlayer clientPlayer))
            return;

        PlayerMeshStorage settings = (PlayerMeshStorage) clientPlayer;
        skinlayer3d$clearMeshes();

        if (Minecraft.getInstance().player == null) return;
        double distanceSquared = clientPlayer.distanceToSqr(Minecraft.getInstance().gameRenderer.getMainCamera()
                .getPosition());
        if (distanceSquared > Constants.renderDistance * Constants.renderDistance) {
            return;
        }

        if (!SkinHelper.setupPlayerMeshes(clientPlayer, settings, slim)) return;

        ItemStack itemStack = clientPlayer.getItemBySlot(EquipmentSlot.HEAD);
        if (!SkinHelper.blacklistedHats.contains(itemStack.getItem())) {
            skinlayer3d$attachMesh(hat, settings.skinlayer3d$getHatMesh(), OffsetProvider.HEAD);
        }
        skinlayer3d$attachMesh(jacket, settings.skinlayer3d$getTorsoMesh(), OffsetProvider.BODY);
        skinlayer3d$attachMesh(leftSleeve, settings.skinlayer3d$getLeftSleeveMesh(), slim ? OffsetProvider.LEFT_ARM_SLIM : OffsetProvider.LEFT_ARM);
        skinlayer3d$attachMesh(rightSleeve, settings.skinlayer3d$getRightSleeveMesh(), slim ? OffsetProvider.RIGHT_ARM_SLIM : OffsetProvider.RIGHT_ARM);
        skinlayer3d$attachMesh(leftPants, settings.skinlayer3d$getLeftLegMesh(), OffsetProvider.LEFT_LEG);
        skinlayer3d$attachMesh(rightPants, settings.skinlayer3d$getRightLegMesh(), OffsetProvider.RIGHT_LEG);

    }

    @Unique
    private void skinlayer3d$clearMeshes() {
        skinlayer3d$attachMesh(hat, null, null);
        skinlayer3d$attachMesh(jacket, null, null);
        skinlayer3d$attachMesh(leftSleeve, null, null);
        skinlayer3d$attachMesh(rightSleeve, null, null);
        skinlayer3d$attachMesh(leftPants, null, null);
        skinlayer3d$attachMesh(rightPants, null, null);
    }

    @Unique
    private void skinlayer3d$attachMesh(ModelPart modelPart, Mesh mesh, OffsetProvider offsetProvider) {
        ((ModelPartMeshHolder) (Object) modelPart).skinlayer3d$attachMesh(mesh, offsetProvider);
    }
}
