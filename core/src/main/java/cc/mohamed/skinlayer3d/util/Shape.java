package cc.mohamed.skinlayer3d.util;

public record Shape(float yOffsetMagicValue, Dimensions dimensions) {
    public static final Shape HEAD = new Shape(0.0f, new Dimensions(8, 8, 8));
    public static final Shape BODY = new Shape(-0.2f, new Dimensions(8, 12, 4));
    public static final Shape LEGS = new Shape(-0.2f, new Dimensions(4, 14, 4));
    public static final Shape ARMS = new Shape(-0.1f, new Dimensions(4, 14, 4));
    public static final Shape ARMS_SLIM = new Shape(-0.1f, new Dimensions(3, 14, 4));

    public record Dimensions(int width, int height, int depth) {
    }
}

