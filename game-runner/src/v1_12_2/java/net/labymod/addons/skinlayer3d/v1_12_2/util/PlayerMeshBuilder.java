package net.labymod.addons.skinlayer3d.v1_12_2.util;

import net.labymod.addons.skinlayer3d.model.TextureData;
import net.labymod.addons.skinlayer3d.util.BodyPart;
import net.labymod.addons.skinlayer3d.v1_12_2.accessor.PlayerMeshStorage;
import net.labymod.addons.skinlayer3d.v1_12_2.render.Mesh;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;


public class PlayerMeshBuilder {
    private static final TextureResolver TEXTURE_RESOLVER = new TextureResolver();

    public static boolean buildMeshes(AbstractClientPlayer player, PlayerMeshStorage storage, boolean slim) {
        ResourceLocation skinTexture = player.getLocationSkin();

        if (isMeshCacheValid(storage, skinTexture, slim)) {
            return storage.skinlayer3d$getHatMesh() != null;
        }

        // validate texture dimensions
        BufferedImage texture = TEXTURE_RESOLVER.resolvePlayerSkin(skinTexture);
        if (!isValidPlayerSkin(texture)) {
            updateCacheState(storage, skinTexture, slim);
            storage.clearMeshes();
            return false;
        }

        // generate all meshes
        generateAllBodyParts(storage, texture, slim);
        updateCacheState(storage, skinTexture, slim);
        return true;
    }


    private static boolean isMeshCacheValid(PlayerMeshStorage storage, ResourceLocation skin, boolean slim) {
        return skin.equals(storage.skinlayer3d$getStoredSkin()) && slim == storage.skinlayer3d$hasSlimArms();
    }

    private static boolean isValidPlayerSkin(BufferedImage image) {
        return image != null && image.getWidth() == 64 && image.getHeight() == 64;
    }

    private static void updateCacheState(PlayerMeshStorage storage, ResourceLocation skin, boolean slim) {
        storage.skinlayer3d$storeSkin(skin);
        storage.skinlayer3d$setSlimArms(slim);
    }

    private static void generateAllBodyParts(PlayerMeshStorage storage, BufferedImage texture, boolean slim) {
        TextureData wrapped = new VersionedTextureData(texture);

        storage.skinlayer3d$setLeftLegMesh(BodyPart.LEFT_LEG.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setRightLegMesh(BodyPart.RIGHT_LEG.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setLeftSleeveMesh((slim ? BodyPart.LEFT_ARM_SLIM : BodyPart.LEFT_ARM).createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setRightSleeveMesh((slim ? BodyPart.RIGHT_ARM_SLIM : BodyPart.RIGHT_ARM).createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setTorsoMesh(BodyPart.TORSO.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setHatMesh(BodyPart.HEAD.createMesh(wrapped, Mesh::new));
    }
}
