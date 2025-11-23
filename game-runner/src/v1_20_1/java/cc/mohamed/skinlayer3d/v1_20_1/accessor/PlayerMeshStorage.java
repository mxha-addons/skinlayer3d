package cc.mohamed.skinlayer3d.v1_20_1.accessor;

import cc.mohamed.skinlayer3d.v1_20_1.render.Mesh;
import net.minecraft.resources.ResourceLocation;

public interface PlayerMeshStorage {

    Mesh skinlayer3d$getHatMesh();
    Mesh skinlayer3d$getTorsoMesh();
    Mesh skinlayer3d$getLeftSleeveMesh();
    Mesh skinlayer3d$getRightSleeveMesh();
    Mesh skinlayer3d$getLeftLegMesh();
    Mesh skinlayer3d$getRightLegMesh();

    void skinlayer3d$setHatMesh(Mesh mesh);
    void skinlayer3d$setTorsoMesh(Mesh mesh);
    void skinlayer3d$setLeftSleeveMesh(Mesh mesh);
    void skinlayer3d$setRightSleeveMesh(Mesh mesh);
    void skinlayer3d$setLeftLegMesh(Mesh mesh);
    void skinlayer3d$setRightLegMesh(Mesh mesh);

    boolean skinlayer3d$hasSlimArms();
    void skinlayer3d$setSlimArms(boolean slimArms);

    ResourceLocation skinlayer3d$getStoredSkin();
    void skinlayer3d$storeSkin(ResourceLocation skin);

    default void clearMeshes() {
        skinlayer3d$setHatMesh(null);
        skinlayer3d$setTorsoMesh(null);
        skinlayer3d$setLeftSleeveMesh(null);
        skinlayer3d$setRightSleeveMesh(null);
        skinlayer3d$setLeftLegMesh(null);
        skinlayer3d$setRightLegMesh(null);
    }
}
