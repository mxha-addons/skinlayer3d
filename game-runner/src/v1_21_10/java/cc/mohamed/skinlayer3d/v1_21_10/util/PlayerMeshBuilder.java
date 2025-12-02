package cc.mohamed.skinlayer3d.v1_21_10.util;

import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.util.BodyPart;
import cc.mohamed.skinlayer3d.v1_21_10.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_21_10.render.Mesh;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class PlayerMeshBuilder {
    private static final TextureResolver TEXTURE_RESOLVER = new TextureResolver();

    private static final Item[] HELMET_BLACKLIST = {Items.ZOMBIE_HEAD, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL};

    public static boolean isHelmetAllowed(Item item) {
        for (Item blocked : HELMET_BLACKLIST) {
            if (blocked == item) return false;
        }
        return true;
    }

    public static boolean buildMeshes(Avatar player, PlayerMeshStorage storage, boolean slim) {
        ResourceLocation skinTexture = ((ClientAvatarEntity) player).getSkin().body().texturePath();

        if (isMeshCacheValid(storage, skinTexture, slim)) {
            return storage.skinlayer3d$getHatMesh() != null;
        }

        // validate texture dimensions
        NativeImage texture = TEXTURE_RESOLVER.resolvePlayerSkin(skinTexture);
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

    private static boolean isValidPlayerSkin(NativeImage image) {
        return image != null && image.getWidth() == 64 && image.getHeight() == 64;
    }

    private static void updateCacheState(PlayerMeshStorage storage, ResourceLocation skin, boolean slim) {
        storage.skinlayer3d$storeSkin(skin);
        storage.skinlayer3d$setSlimArms(slim);
    }

    private static void generateAllBodyParts(PlayerMeshStorage storage, NativeImage texture, boolean slim) {
        TextureData wrapped = new VersionedTextureData(texture);

        storage.skinlayer3d$setLeftLegMesh(BodyPart.LEFT_LEG.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setRightLegMesh(BodyPart.RIGHT_LEG.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setLeftSleeveMesh((slim ? BodyPart.LEFT_ARM_SLIM : BodyPart.LEFT_ARM).createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setRightSleeveMesh((slim ? BodyPart.RIGHT_ARM_SLIM : BodyPart.RIGHT_ARM).createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setTorsoMesh(BodyPart.TORSO.createMesh(wrapped, Mesh::new));
        storage.skinlayer3d$setHatMesh(BodyPart.HEAD.createMesh(wrapped, Mesh::new));
    }
}
