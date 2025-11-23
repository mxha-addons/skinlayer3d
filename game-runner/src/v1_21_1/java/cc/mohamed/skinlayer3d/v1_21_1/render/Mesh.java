package cc.mohamed.skinlayer3d.v1_21_1.render;

import cc.mohamed.skinlayer3d.render.AbstractMesh;
import cc.mohamed.skinlayer3d.render.Cube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class Mesh extends AbstractMesh {

    private final Vector4f posBuffer = new Vector4f();
    private final Vector3f normBuffer = new Vector3f();

    public Mesh(List<Cube> cubes) {
        super(cubes);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, int color) {
        if (quadCount == 0) return;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f modelMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        // apply mesh offset
        if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            poseStack.translate(offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);
            modelMatrix = poseStack.last().pose();
            normalMatrix = poseStack.last().normal();
        }

        // render all quads
        int vIdx = 0;
        int nIdx = 0;

        for (int q = 0; q < quadCount; q++) {
            // transform normal (once per quad)
            normBuffer.set(normals[nIdx], normals[nIdx + 1], normals[nIdx + 2]);
            normalMatrix.transform(normBuffer);
            nIdx += 3;

            // emit 4 vertices per quad
            for (int v = 0; v < 4; v++) {
                float px = vertices[vIdx];
                float py = vertices[vIdx + 1];
                float pz = vertices[vIdx + 2];
                float tu = vertices[vIdx + 3];
                float tv = vertices[vIdx + 4];
                vIdx += 5;

                posBuffer.set(px, py, pz, 1.0F);
                modelMatrix.transform(posBuffer);

                consumer.addVertex(posBuffer.x(), posBuffer.y(), posBuffer.z());
                consumer.setColor(color);
                consumer.setUv(tu, tv);
                consumer.setOverlay(overlay);
                consumer.setLight(light);
                consumer.setNormal(normBuffer.x(), normBuffer.y(), normBuffer.z());
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }
}
