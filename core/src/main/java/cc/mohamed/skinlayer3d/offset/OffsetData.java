package cc.mohamed.skinlayer3d.offset;

import cc.mohamed.skinlayer3d.util.Constants;
import cc.mohamed.skinlayer3d.util.Shape;

public record OffsetData(float meshX, float meshY, float meshZ, TransformData transform) {

    public static final OffsetData HEAD = create(Shape.HEAD, false, false);
    public static final OffsetData LEFT_LEG = create(Shape.LEGS, false, false);
    public static final OffsetData RIGHT_LEG = create(Shape.LEGS, false, false);
    public static final OffsetData LEFT_ARM = create(Shape.ARMS, false, false);
    public static final OffsetData LEFT_ARM_SLIM = create(Shape.ARMS_SLIM, false, false);
    public static final OffsetData RIGHT_ARM = create(Shape.ARMS, true, false);
    public static final OffsetData RIGHT_ARM_SLIM = create(Shape.ARMS_SLIM, true, false);
    public static final OffsetData FIRSTPERSON_LEFT_ARM = create(Shape.ARMS, false, true);
    public static final OffsetData FIRSTPERSON_LEFT_ARM_SLIM = create(Shape.ARMS_SLIM, false, true);
    public static final OffsetData FIRSTPERSON_RIGHT_ARM = create(Shape.ARMS, true, true);
    public static final OffsetData FIRSTPERSON_RIGHT_ARM_SLIM = create(Shape.ARMS_SLIM, true, true);
    public static final OffsetData BODY = create(Shape.BODY, false, false);

    private static OffsetData create(Shape shape, boolean mirrored, boolean firstPerson) {
        float pixelScaling = Constants.baseVoxelSize;
        float heightScaling = 1.035f;
        float widthScaling = Constants.baseVoxelSize;
        if (firstPerson) {
            pixelScaling = Constants.firstPersonPixelScaling;
            widthScaling = Constants.firstPersonPixelScaling;
        }

        float x = 0;
        float y = 0;
        if (shape == Shape.ARMS) {
            x = 0.998f;
        } else if (shape == Shape.ARMS_SLIM) {
            x = 0.499f;
        }
        if (shape == Shape.BODY) {
            widthScaling = Constants.bodyVoxelWidthSize;
        }
        if (mirrored) {
            x *= -1;
        }

        TransformData transform;
        if (shape == Shape.HEAD) {
            float voxelSize = Constants.headVoxelSize;
            transform = new TransformData(0, -0.25f, 0,  // translate before scale
                    voxelSize, voxelSize, voxelSize,                           // scale
                    0, 0.25f - 0.04f, 0                           // translate after scale
            );
        } else {
            y = shape.yOffsetMagicValue();
            transform = new TransformData(0, 0, 0,        // no translate before scale
                    widthScaling, heightScaling, pixelScaling,                  // scale
                    0, 0, 0                                       // no translate after scale
            );
        }

        return new OffsetData(x, y, 0, transform);
    }

    public record TransformData(float translateBeforeX, float translateBeforeY, float translateBeforeZ, float scaleX, float scaleY, float scaleZ, float translateAfterX, float translateAfterY, float translateAfterZ) {
    }
}
