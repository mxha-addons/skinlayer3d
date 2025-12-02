package cc.mohamed.skinlayer3d.v1_20_1.util;

import cc.mohamed.skinlayer3d.SkinLayer3D;
import cc.mohamed.skinlayer3d.v1_20_1.accessor.HttpTextureAccessor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.TimeUnit;

public class TextureResolver {
    private static final Cache<AbstractTexture, NativeImage> SKIN_CACHE = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .removalListener(TextureResolver::cleanupTexture)
            .build();

    private static void cleanupTexture(RemovalNotification<AbstractTexture, NativeImage> notification) {
        NativeImage image = notification.getValue();
        if (image != null) {
            try {
                image.close();
            } catch (Exception e) {
                SkinLayer3D.LOGGER.error("Failed to close cached texture", e);
            }
        }
    }

    public NativeImage resolvePlayerSkin(ResourceLocation location) {
        if (location == null) {
            return null;
        }

        try {
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(location);
            if (texture == null) {
                return null;
            }

            NativeImage cached = SKIN_CACHE.getIfPresent(texture);
            if (cached != null) {
                return cached;
            }

            if (texture instanceof HttpTextureAccessor httpTexture) {
                try {
                    NativeImage img = httpTexture.skinlayer3d$getImage();
                    if (img != null) {
                        SKIN_CACHE.put(texture, img);
                        return img;
                    }
                } catch (Exception e) {
                    SkinLayer3D.LOGGER.error("Failed to get image from HttpTexture", e);
                }
            }

            return null;
        } catch (Exception e) {
            SkinLayer3D.LOGGER.error("Failed to resolve player skin: {}", location, e);
            return null;
        }
    }
}