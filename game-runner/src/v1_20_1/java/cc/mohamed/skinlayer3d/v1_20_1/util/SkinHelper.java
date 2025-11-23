package cc.mohamed.skinlayer3d.v1_20_1.util;

import cc.mohamed.skinlayer3d.SkinLayer3D;
import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.render.MeshGenerator;
import cc.mohamed.skinlayer3d.v1_20_1.accessor.HttpTextureAccessor;
import cc.mohamed.skinlayer3d.v1_20_1.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_20_1.render.Mesh;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SkinHelper {
    
    public static final Set<Item> blacklistedHats = new HashSet<>();

    static {
        blacklistedHats.add(Items.ZOMBIE_HEAD);
        blacklistedHats.add(Items.CREEPER_HEAD);
        blacklistedHats.add(Items.DRAGON_HEAD);
        blacklistedHats.add(Items.SKELETON_SKULL);
        blacklistedHats.add(Items.WITHER_SKELETON_SKULL);
    }

    private static final Cache<AbstractTexture, NativeImage> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(60L, TimeUnit.SECONDS)
            .removalListener((RemovalListener<AbstractTexture, NativeImage>) notification -> {
                try {
                    if (notification.getValue() != null) {
                        notification.getValue().close();
                    }
                } catch (Exception ex) {
                    SkinLayer3D.LOGGER.error("Error while closing a skin texture.", ex);
                }
            })
            .build();

    public static NativeImage getTexture(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        try {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(resourceLocation);
            if (texture == null) {
                return null;
            }

            NativeImage cachedImage = cache.getIfPresent(texture);
            if (cachedImage != null) {
                return cachedImage;
            }

            if (texture instanceof HttpTextureAccessor httpTexture) {
                try {
                    NativeImage img = httpTexture.skinlayer3d$getImage();
                    if (img != null) {
                        cache.put(texture, img);
                        return img;
                    }
                } catch (Exception e) {
                    SkinLayer3D.LOGGER.error("Failed to load HttpTexture for: {}", resourceLocation, e);
                }
            }

            return null;
        } catch (Exception ex) {
            SkinLayer3D.LOGGER.error("Error while resolving skin texture.", ex);
            return null;
        }
    }

    public static boolean setupPlayerMeshes(AbstractClientPlayer player, PlayerMeshStorage settings, boolean slimArms) {
        ResourceLocation skinLocation = player.getSkinTextureLocation();

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
