package cc.mohamed.skinlayer3d.v1_21_10.util;

import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.model.UV;
import com.mojang.blaze3d.platform.NativeImage;

public class VersionedTextureData implements TextureData {

    private final NativeImage nativeImage;

    public VersionedTextureData(NativeImage nativeImage) {
        this.nativeImage = nativeImage;
    }

    @Override
    public int width() {
        return nativeImage.getWidth();
    }

    @Override
    public int height() {
        return nativeImage.getHeight();
    }

    @Override
    public boolean isSolid(UV uv) {
        return nativeImage.getLuminanceOrAlpha(uv.u(), uv.v()) == -1;
    }

    @Override
    public boolean isPresent(UV uv) {
        return nativeImage.getLuminanceOrAlpha(uv.u(), uv.v()) != 0;
    }
}
