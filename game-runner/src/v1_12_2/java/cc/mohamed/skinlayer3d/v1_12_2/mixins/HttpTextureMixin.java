package cc.mohamed.skinlayer3d.v1_12_2.mixins;

import cc.mohamed.skinlayer3d.v1_12_2.accessor.HttpTextureAccessor;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.SimpleTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

@Mixin(ThreadDownloadImageData.class)
public abstract class HttpTextureMixin extends SimpleTexture implements HttpTextureAccessor {

    @Final
    @Shadow
    private File cacheFile;

    @Final
    @Shadow
    private IImageBuffer imageBuffer;

    protected HttpTextureMixin(net.minecraft.util.ResourceLocation textureResourceLocation) {
        super(textureResourceLocation);
    }

    @Override
    public BufferedImage skinlayer3d$getImage() {
        if (this.cacheFile != null && this.cacheFile.isFile()) {
            try {
                BufferedImage image = ImageIO.read(new FileInputStream(this.cacheFile));
                if (image != null && this.imageBuffer != null) {
                    image = this.imageBuffer.parseUserSkin(image);
                }
                return image;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
