package cc.mohamed.skinlayer3d.v1_12_2.util;

import cc.mohamed.skinlayer3d.SkinLayer3D;
import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.render.MeshGenerator;
import cc.mohamed.skinlayer3d.v1_12_2.accessor.HttpTextureAccessor;
import cc.mohamed.skinlayer3d.v1_12_2.accessor.PlayerMeshStorage;
import cc.mohamed.skinlayer3d.v1_12_2.render.Mesh;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SkinHelper {

    public static final Set<Item> blacklistedHats = new HashSet<>();

    static {
        blacklistedHats.add(Items.SKULL);
    }

    private static final Cache<ITextureObject, BufferedImage> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(60L, TimeUnit.SECONDS)
            .build();

    public static BufferedImage getTexture(ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        try {
            ITextureObject texture = Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation);
            if (texture == null) {
                return null;
            }

            BufferedImage cachedImage = cache.getIfPresent(texture);
            if (cachedImage != null) {
                return cachedImage;
            }

            if (texture instanceof HttpTextureAccessor httpTexture) {
                try {
                    BufferedImage img = httpTexture.skinlayer3d$getImage();
                    if (img != null) {
                        cache.put(texture, img);
                        return img;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        } catch (Exception ex) {
            SkinLayer3D.LOGGER.error("Error while resolving skin texture.", ex);
            return null;
        }
    }

    public static boolean setupPlayerMeshes(AbstractClientPlayer player, PlayerMeshStorage settings, boolean slimArms) {
        ResourceLocation skinLocation = player.getLocationSkin();

        if (skinLocation.equals(settings.skinlayer3d$getStoredSkin()) && slimArms == settings.skinlayer3d$hasSlimArms()) {
            return settings.skinlayer3d$getHatMesh() != null;
        }

        BufferedImage skin = getTexture(skinLocation);

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
