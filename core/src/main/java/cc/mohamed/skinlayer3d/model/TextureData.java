package cc.mohamed.skinlayer3d.model;

/**
 * Version-independent Wrapper for Minecraft Textures
 */
public interface TextureData {

    /**
     * @return the width of the texture
     */
    int width();

    /**
     * @return the height of the texture
     */
    int height();

    /**
     * @return true if pixel is fully opaque (alpha = 255)
     */
    boolean isSolid(UV uv);

    /**
     * @return true if pixel exists and is not fully transparent (alpha > 0)
     */
    boolean isPresent(UV uv);
}
