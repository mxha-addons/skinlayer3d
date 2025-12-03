package net.labymod.addons.skinlayer3d.v1_16_5.util;

import net.labymod.addons.skinlayer3d.model.TextureData;
import net.labymod.addons.skinlayer3d.model.UV;
import com.mojang.blaze3d.platform.NativeImage;

public class VersionedTextureData implements TextureData {

    private final NativeImage image;

    public VersionedTextureData(NativeImage image) {
        this.image = image;
    }

    @Override
    public int width() {
        return image.getWidth();
    }

    @Override
    public int height() {
        return image.getHeight();
    }

    @Override
    public boolean isSolid(UV uv) {
        if (uv.u() < 0 || uv.u() >= image.getWidth() || uv.v() < 0 || uv.v() >= image.getHeight()) {
            return false;
        }
        int abgr = image.getPixelRGBA(uv.u(), uv.v());
        int alpha = (abgr >> 24) & 0xFF;
        return alpha == 255;
    }

    @Override
    public boolean isPresent(UV uv) {
        if (uv.u() < 0 || uv.u() >= image.getWidth() || uv.v() < 0 || uv.v() >= image.getHeight()) {
            return false;
        }
        int abgr = image.getPixelRGBA(uv.u(), uv.v());
        int alpha = (abgr >> 24) & 0xFF;
        return alpha > 0;
    }
}
