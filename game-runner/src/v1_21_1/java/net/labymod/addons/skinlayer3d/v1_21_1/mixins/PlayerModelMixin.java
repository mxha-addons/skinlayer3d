package net.labymod.addons.skinlayer3d.v1_21_1.mixins;

import net.labymod.addons.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerModel.class)
public class PlayerModelMixin extends HumanoidModel implements PlayerEntityModelAccessor {

    @Final
    @Shadow
    private boolean slim;

    public PlayerModelMixin(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public boolean skinlayer3d$hasSlimArms() {
        return slim;
    }
}
