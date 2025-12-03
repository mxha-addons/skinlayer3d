package net.labymod.addons.skinlayer3d.v1_21_5.util;

import net.labymod.addons.skinlayer3d.SkinLayer3D;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import com.mojang.blaze3d.platform.NativeImage;
import net.labymod.core.client.accessor.resource.texture.NativeImageAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TextureResolver {
    private static final Cache<ResourceLocation, NativeImage> SKIN_CACHE = CacheBuilder.newBuilder()
            .maximumSize(256)
            .expireAfterAccess(60, TimeUnit.SECONDS)
            .removalListener(TextureResolver::cleanupTexture)
            .build();

    private static void cleanupTexture(RemovalNotification<ResourceLocation, NativeImage> notification) {
        NativeImage image = notification.getValue();
        if (image != null) {
            try {
                image.close();
            } catch (Exception e) {
                SkinLayer3D.LOGGER.error("Failed to close cached texture: {}", notification.getKey(), e);
            }
        }
    }

    public NativeImage resolvePlayerSkin(ResourceLocation location) {
        if (location == null) return null;

        NativeImage cached = SKIN_CACHE.getIfPresent(location);
        if (isImageValid(cached)) {
            return cached;
        }

        if (cached != null) {
            SKIN_CACHE.invalidate(location);
        }

        // cache images we load, so we can free them later
        NativeImage loaded = loadFromResources(location);
        if (loaded != null) {
            SKIN_CACHE.put(location, loaded);
            return loaded;
        }

        return extractFromTextureManager(location);
    }

    private NativeImage loadFromResources(ResourceLocation location) {
        try {
            return Minecraft.getInstance().getResourceManager().getResource(location).map(res -> {
                try {
                    return NativeImage.read(res.open());
                } catch (IOException e) {
                    return null;
                }
            }).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private NativeImage extractFromTextureManager(ResourceLocation location) {
        try {
            AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(location);
            if (tex instanceof DynamicTexture dt) {
                NativeImage pixels = dt.getPixels();
                if (isImageValid(pixels)) {
                    return pixels;
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private boolean isImageValid(NativeImage img) {
        if (img == null) return false;
        if ((Object) img instanceof NativeImageAccessor accessor) {
            return !accessor.isFreed();
        }
        return true;
    }
}
