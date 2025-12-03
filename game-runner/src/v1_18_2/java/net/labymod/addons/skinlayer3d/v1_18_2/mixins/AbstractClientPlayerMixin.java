package net.labymod.addons.skinlayer3d.v1_18_2.mixins;

import net.labymod.addons.skinlayer3d.v1_18_2.accessor.PlayerMeshStorage;
import net.labymod.addons.skinlayer3d.v1_18_2.render.Mesh;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends LivingEntity implements PlayerMeshStorage {

    protected AbstractClientPlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Unique private Mesh skinlayer3d$hatMesh;
    @Unique private Mesh skinlayer3d$torsoMesh;
    @Unique private Mesh skinlayer3d$leftSleeveMesh;
    @Unique private Mesh skinlayer3d$rightSleeveMesh;
    @Unique private Mesh skinlayer3d$leftLegMesh;
    @Unique private Mesh skinlayer3d$rightLegMesh;
    @Unique private ResourceLocation skinlayer3d$storedSkin;
    @Unique private boolean skinlayer3d$slimArms;

    @Override
    public Mesh skinlayer3d$getHatMesh() {
        return skinlayer3d$hatMesh;
    }

    @Override
    public Mesh skinlayer3d$getTorsoMesh() {
        return skinlayer3d$torsoMesh;
    }

    @Override
    public Mesh skinlayer3d$getLeftSleeveMesh() {
        return skinlayer3d$leftSleeveMesh;
    }

    @Override
    public Mesh skinlayer3d$getRightSleeveMesh() {
        return skinlayer3d$rightSleeveMesh;
    }

    @Override
    public Mesh skinlayer3d$getLeftLegMesh() {
        return skinlayer3d$leftLegMesh;
    }

    @Override
    public Mesh skinlayer3d$getRightLegMesh() {
        return skinlayer3d$rightLegMesh;
    }

    @Override
    public void skinlayer3d$setHatMesh(Mesh mesh) {
        this.skinlayer3d$hatMesh = mesh;
    }

    @Override
    public void skinlayer3d$setTorsoMesh(Mesh mesh) {
        this.skinlayer3d$torsoMesh = mesh;
    }

    @Override
    public void skinlayer3d$setLeftSleeveMesh(Mesh mesh) {
        this.skinlayer3d$leftSleeveMesh = mesh;
    }

    @Override
    public void skinlayer3d$setRightSleeveMesh(Mesh mesh) {
        this.skinlayer3d$rightSleeveMesh = mesh;
    }

    @Override
    public void skinlayer3d$setLeftLegMesh(Mesh mesh) {
        this.skinlayer3d$leftLegMesh = mesh;
    }

    @Override
    public void skinlayer3d$setRightLegMesh(Mesh mesh) {
        this.skinlayer3d$rightLegMesh = mesh;
    }

    @Override
    public boolean skinlayer3d$hasSlimArms() {
        return skinlayer3d$slimArms;
    }

    @Override
    public void skinlayer3d$setSlimArms(boolean slimArms) {
        this.skinlayer3d$slimArms = slimArms;
    }

    @Override
    public ResourceLocation skinlayer3d$getStoredSkin() {
        return skinlayer3d$storedSkin;
    }

    @Override
    public void skinlayer3d$storeSkin(ResourceLocation skin) {
        this.skinlayer3d$storedSkin = skin;
    }

}
