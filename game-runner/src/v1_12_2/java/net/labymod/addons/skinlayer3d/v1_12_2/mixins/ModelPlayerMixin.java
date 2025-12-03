package net.labymod.addons.skinlayer3d.v1_12_2.mixins;

import net.labymod.addons.skinlayer3d.model.accessor.PlayerEntityModelAccessor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelPlayer.class)
public class ModelPlayerMixin extends ModelBiped implements PlayerEntityModelAccessor {

    @Shadow
    private boolean smallArms;

    @Override
    public boolean skinlayer3d$hasSlimArms() {
        return smallArms;
    }
}
