package cc.mohamed.skinlayer3d.v1_20_1.util;

import cc.mohamed.skinlayer3d.offset.OffsetData;
import cc.mohamed.skinlayer3d.v1_20_1.render.Mesh;
import com.mojang.blaze3d.vertex.PoseStack;

public interface OffsetProvider {

    void applyOffset(PoseStack stack, Mesh mesh);

    OffsetProvider HEAD = from(OffsetData.HEAD);
    OffsetProvider LEFT_LEG = from(OffsetData.LEFT_LEG);
    OffsetProvider RIGHT_LEG = from(OffsetData.RIGHT_LEG);
    OffsetProvider LEFT_ARM = from(OffsetData.LEFT_ARM);
    OffsetProvider LEFT_ARM_SLIM = from(OffsetData.LEFT_ARM_SLIM);
    OffsetProvider RIGHT_ARM = from(OffsetData.RIGHT_ARM);
    OffsetProvider RIGHT_ARM_SLIM = from(OffsetData.RIGHT_ARM_SLIM);
    OffsetProvider FIRSTPERSON_LEFT_ARM = from(OffsetData.FIRSTPERSON_LEFT_ARM);
    OffsetProvider FIRSTPERSON_LEFT_ARM_SLIM = from(OffsetData.FIRSTPERSON_LEFT_ARM_SLIM);
    OffsetProvider FIRSTPERSON_RIGHT_ARM = from(OffsetData.FIRSTPERSON_RIGHT_ARM);
    OffsetProvider FIRSTPERSON_RIGHT_ARM_SLIM = from(OffsetData.FIRSTPERSON_RIGHT_ARM_SLIM);
    OffsetProvider BODY = from(OffsetData.BODY);

    private static OffsetProvider from(OffsetData data) {
        return (stack, mesh) -> {
            OffsetData.TransformData transform = data.transform();

            // transform before scale
            if (transform.translateBeforeX() != 0 || transform.translateBeforeY() != 0 || transform.translateBeforeZ() != 0) {
                stack.translate(transform.translateBeforeX(), transform.translateBeforeY(), transform.translateBeforeZ());
            }

            stack.scale(transform.scaleX(), transform.scaleY(), transform.scaleZ());

            // transform after scale
            if (transform.translateAfterX() != 0 || transform.translateAfterY() != 0 || transform.translateAfterZ() != 0) {
                stack.translate(transform.translateAfterX(), transform.translateAfterY(), transform.translateAfterZ());
            }

            mesh.setPosition(data.meshX(), data.meshY(), data.meshZ());
        };
    }

}
