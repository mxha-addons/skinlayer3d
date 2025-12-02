package cc.mohamed.skinlayer3d.offset;

import cc.mohamed.skinlayer3d.util.BodyPart;
import cc.mohamed.skinlayer3d.util.Constants;

public record OffsetData(float meshX, float meshY, float meshZ, TransformData transform) {

    public static final OffsetData HEAD = create(BodyPart.HEAD, false, false);
    public static final OffsetData LEFT_LEG = create(BodyPart.LEGS, false, false);
    public static final OffsetData RIGHT_LEG = create(BodyPart.LEGS, false, false);
    public static final OffsetData LEFT_ARM = create(BodyPart.ARMS, false, false);
    public static final OffsetData LEFT_ARM_SLIM = create(BodyPart.ARMS_SLIM, false, false);
    public static final OffsetData RIGHT_ARM = create(BodyPart.ARMS, true, false);
    public static final OffsetData RIGHT_ARM_SLIM = create(BodyPart.ARMS_SLIM, true, false);
    public static final OffsetData FIRSTPERSON_LEFT_ARM = create(BodyPart.ARMS, false, true);
    public static final OffsetData FIRSTPERSON_LEFT_ARM_SLIM = create(BodyPart.ARMS_SLIM, false, true);
    public static final OffsetData FIRSTPERSON_RIGHT_ARM = create(BodyPart.ARMS, true, true);
    public static final OffsetData FIRSTPERSON_RIGHT_ARM_SLIM = create(BodyPart.ARMS_SLIM, true, true);
    public static final OffsetData BODY = create(BodyPart.TORSO, false, false);

    private static OffsetData create(BodyPart part, boolean mirrored, boolean firstPerson) {
        float pixelScaling = Constants.voxelSize;
        float heightScaling = 1.035f;
        float widthScaling = Constants.voxelSize;
        /*if (firstPerson) {
            pixelScaling = Constants.firstPersonVoxelSize;
            widthScaling = Constants.firstPersonVoxelSize;
        }*/

        float x = 0;
        float y = 0;
        if (part == BodyPart.ARMS) {
            x = 0.998f;
        } else if (part == BodyPart.ARMS_SLIM) {
            x = 0.499f;
        }
        if (part == BodyPart.TORSO) {
            widthScaling = Constants.torsoVoxelSize;
        }
        if (mirrored) {
            x *= -1;
        }

        TransformData transform;
        if (part == BodyPart.HEAD) {
            float voxelSize = Constants.headVoxelSize;
            transform = new TransformData(0, -0.25f, 0,  // translate before scale
                    voxelSize, voxelSize, voxelSize,                           // scale
                    0, 0.25f - 0.04f, 0                           // translate after scale
            );
        } else {
            y = part.verticalOffset;
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
