package net.labymod.addons.skinlayer3d.v1_8_9.util;

import net.labymod.addons.skinlayer3d.SkinLayer3D;
import net.labymod.addons.skinlayer3d.v1_8_9.accessor.HttpTextureAccessor;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

public class TextureResolver {
    private static final Cache<ITextureObject, BufferedImage> SKIN_CACHE = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .build();


    public BufferedImage resolvePlayerSkin(ResourceLocation location) {
        if (location == null) {
            return null;
        }

        try {
            ITextureObject texture = Minecraft.getMinecraft().getTextureManager().getTexture(location);
            if (texture == null) {
                return null;
            }

            BufferedImage cached = SKIN_CACHE.getIfPresent(texture);
            if (cached != null) {
                return cached;
            }

            if (texture instanceof HttpTextureAccessor httpTexture) {
                try {
                    BufferedImage img = httpTexture.skinlayer3d$getImage();
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