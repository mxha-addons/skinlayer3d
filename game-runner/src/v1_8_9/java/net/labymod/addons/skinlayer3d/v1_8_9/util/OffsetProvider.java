package net.labymod.addons.skinlayer3d.v1_8_9.util;

import net.labymod.addons.skinlayer3d.offset.OffsetData;
import net.labymod.addons.skinlayer3d.v1_8_9.render.Mesh;
import net.minecraft.client.renderer.GlStateManager;

public interface OffsetProvider {

    void applyOffset(Mesh mesh, float scale);

    OffsetProvider HEAD = from(OffsetData.HEAD);
    OffsetProvider LEFT_LEG = from(OffsetData.LEFT_LEG);
    OffsetProvider RIGHT_LEG = from(OffsetData.RIGHT_LEG);
    OffsetProvider LEFT_ARM = from(OffsetData.LEFT_ARM);
    OffsetProvider LEFT_ARM_SLIM = from(OffsetData.LEFT_ARM_SLIM);
    OffsetProvider RIGHT_ARM = from(OffsetData.RIGHT_ARM);
    OffsetProvider RIGHT_ARM_SLIM = from(OffsetData.RIGHT_ARM_SLIM);
    OffsetProvider BODY = from(OffsetData.BODY);

    static OffsetProvider from(OffsetData data) {
        return (mesh, scale) -> {
            OffsetData.TransformData transform = data.transform();

            // transform before scale
            if (transform.translateBeforeX() != 0 || transform.translateBeforeY() != 0 || transform.translateBeforeZ() != 0) {
                GlStateManager.translate(transform.translateBeforeX(), transform.translateBeforeY(), transform.translateBeforeZ());
            }

            GlStateManager.scale(transform.scaleX(), transform.scaleY(), transform.scaleZ());

            // transform after scale
            if (transform.translateAfterX() != 0 || transform.translateAfterY() != 0 || transform.translateAfterZ() != 0) {
                GlStateManager.translate(transform.translateAfterX(), transform.translateAfterY(), transform.translateAfterZ());
            }

            mesh.setPosition(data.meshX(), data.meshY(), data.meshZ());
        };
    }

}
