package net.labymod.addons.skinlayer3d.v1_12_2.util;

import net.labymod.addons.skinlayer3d.model.TextureData;
import net.labymod.addons.skinlayer3d.model.UV;

import java.awt.image.BufferedImage;

public class VersionedTextureData implements TextureData {

    private final BufferedImage image;

    public VersionedTextureData(BufferedImage image) {
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
        int argb = image.getRGB(uv.u(), uv.v());
        int alpha = (argb >> 24) & 0xFF;
        return alpha == 255;
    }

    @Override
    public boolean isPresent(UV uv) {
        if (uv.u() < 0 || uv.u() >= image.getWidth() || uv.v() < 0 || uv.v() >= image.getHeight()) {
            return false;
        }
        int argb = image.getRGB(uv.u(), uv.v());
        int alpha = (argb >> 24) & 0xFF;
        return alpha > 0;
    }
}
