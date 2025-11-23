package cc.mohamed.skinlayer3d.v1_21_10.util;

import cc.mohamed.skinlayer3d.SkinLayer3D;
import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.render.MeshGenerator;
import cc.mohamed.skinlayer3d.v1_21_10.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_21_10.render.Mesh;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.NativeImage;
import net.labymod.core.client.accessor.resource.texture.NativeImageAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SkinHelper {
    public static final Set<Item> blacklistedHats = Sets.newHashSet(Items.ZOMBIE_HEAD, Items.CREEPER_HEAD, Items.DRAGON_HEAD, Items.SKELETON_SKULL, Items.WITHER_SKELETON_SKULL);

    private static final Cache<AbstractTexture, NativeImage> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(60L, TimeUnit.SECONDS)
            .removalListener((RemovalListener<AbstractTexture, NativeImage>) notification -> {
                try {
                    notification.getValue().close();
                } catch (Exception ex) {
                    SkinLayer3D.LOGGER.error("Error while closing a texture.", ex);
                }
            })
            .build();

    public static NativeImage getTexture(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        try {
            Optional<Resource> optionalRes = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            if (optionalRes.isPresent()) {
                return NativeImage.read(optionalRes.get().open());
            }
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(resourceLocation);
            NativeImage cachedImage = cache.getIfPresent(texture);
            if (cachedImage != null && (Object) cachedImage instanceof NativeImageAccessor ac && !ac.isFreed()) {
                return cachedImage;
            } else {
                // got invalidated, remove from cache
                cache.invalidate(texture);
            }
            if (texture instanceof DynamicTexture) {
                try {
                    NativeImage img = ((DynamicTexture) texture).getPixels();
                    if (img != null && (Object) img instanceof NativeImageAccessor ac && !ac.isFreed()) {
                        return img;
                    }
                } catch (Exception ignored) {
                }
                return null;
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean setupPlayerMeshes(net.minecraft.world.entity.Avatar abstractClientPlayerEntity, PlayerMeshStorage settings, boolean slimArms) {
        ResourceLocation skinLocation = ((ClientAvatarEntity) abstractClientPlayerEntity).getSkin()
                .body()
                .texturePath();

        if (skinLocation.equals(settings.skinlayer3d$getStoredSkin()) && slimArms == settings.skinlayer3d$hasSlimArms()) {
            return settings.skinlayer3d$getHatMesh() != null;
        }
        NativeImage skin = getTexture(skinLocation);
        if (skin == null || skin.getWidth() != 64 || skin.getHeight() != 64) {
            settings.skinlayer3d$storeSkin(skinLocation);
            settings.skinlayer3d$setSlimArms(slimArms);
            settings.clearMeshes();
            return false;
        }

        TextureData wrappedSkin = new VersionedTextureData(skin);

        settings.skinlayer3d$setLeftLegMesh(MeshGenerator.createMesh(wrappedSkin, 4, 12, 4, 0, 48, true, 0f, Mesh::new));
        settings.skinlayer3d$setRightLegMesh(MeshGenerator.createMesh(wrappedSkin, 4, 12, 4, 0, 32, true, 0f, Mesh::new));
        if (slimArms) {
            settings.skinlayer3d$setLeftSleeveMesh(MeshGenerator.createMesh(wrappedSkin, 3, 12, 4, 48, 48, true, -2f, Mesh::new));
            settings.skinlayer3d$setRightSleeveMesh(MeshGenerator.createMesh(wrappedSkin, 3, 12, 4, 40, 32, true, -2f, Mesh::new));
        } else {
            settings.skinlayer3d$setLeftSleeveMesh(MeshGenerator.createMesh(wrappedSkin, 4, 12, 4, 48, 48, true, -2, Mesh::new));
            settings.skinlayer3d$setRightSleeveMesh(MeshGenerator.createMesh(wrappedSkin, 4, 12, 4, 40, 32, true, -2, Mesh::new));
        }
        settings.skinlayer3d$setTorsoMesh(MeshGenerator.createMesh(wrappedSkin, 8, 12, 4, 16, 32, true, 0, Mesh::new));
        settings.skinlayer3d$setHatMesh(MeshGenerator.createMesh(wrappedSkin, 8, 8, 8, 32, 0, false, 0.6f, Mesh::new));
        settings.skinlayer3d$storeSkin(skinLocation);
        settings.skinlayer3d$setSlimArms(slimArms);
        return true;
    }

}
