package cc.mohamed.skinlayer3d.v1_17_1.mixins;

import cc.mohamed.skinlayer3d.v1_17_1.accessor.HttpTextureAccessor;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.HttpTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Mixin(HttpTexture.class)
public abstract class HttpTextureMixin extends AbstractTexture implements HttpTextureAccessor {

    @Final
    @Shadow
    private File file;

    @Override
    public NativeImage skinlayer3d$getImage() throws FileNotFoundException {
        if (this.file != null && this.file.isFile()) {
            return load(new FileInputStream(this.file));
        }
        return null;
    }

    @Shadow
    protected abstract NativeImage load(InputStream inputStream);

}
