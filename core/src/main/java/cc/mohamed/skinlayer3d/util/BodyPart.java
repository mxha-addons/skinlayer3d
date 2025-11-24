package cc.mohamed.skinlayer3d.util;

/**
 * Defines body part shapes with their dimensions and vertical offset.
 * Each shape represents a different player model-part.
 */
public enum BodyPart {
    HEAD(0.0f, 8, 8, 8),
    BODY(-0.2f, 8, 12, 4),
    LEGS(-0.2f, 4, 14, 4),
    ARMS(-0.1f, 4, 14, 4),
    ARMS_SLIM(-0.1f, 3, 14, 4);

    public final float verticalOffset;
    public final int width;
    public final int height;
    public final int depth;

    BodyPart(float verticalOffset, int width, int height, int depth) {
        this.verticalOffset = verticalOffset;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }
}
