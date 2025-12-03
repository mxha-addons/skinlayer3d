package net.labymod.addons.skinlayer3d.v1_16_5.mixins;

import net.labymod.addons.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerModel.class)
public class PlayerModelMixin extends HumanoidModel implements PlayerEntityModelAccessor {

    @Final
    @Shadow private boolean slim;

    public PlayerModelMixin(float lvt_1_1_) {
        super(lvt_1_1_);
    }

    @Override
    public boolean skinlayer3d$hasSlimArms() {
        return slim;
    }
}
