package net.labymod.addons.skinlayer3d.v1_18_2.accessor;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.FileNotFoundException;

public interface HttpTextureAccessor {

    NativeImage skinlayer3d$getImage() throws FileNotFoundException;

}
