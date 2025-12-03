package net.labymod.addons.skinlayer3d.v1_17_1.render;

import net.labymod.addons.skinlayer3d.render.AbstractMesh;
import net.labymod.addons.skinlayer3d.render.Cube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;

import java.util.List;

public class Mesh extends AbstractMesh {

    private final Vector4f posBuffer = new Vector4f();
    private final Vector3f normBuffer = new Vector3f();

    public Mesh(List<Cube> cubes) {
        super(cubes);
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (quadCount == 0) return;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f modelMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            poseStack.translate(offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);
            modelMatrix = poseStack.last().pose();
            normalMatrix = poseStack.last().normal();
        }

        int vIdx = 0;
        int nIdx = 0;

        for (int q = 0; q < quadCount; q++) {
            normBuffer.set(normals[nIdx], normals[nIdx + 1], normals[nIdx + 2]);
            normBuffer.transform(normalMatrix);
            nIdx += 3;

            for (int v = 0; v < 4; v++) {
                float px = vertices[vIdx];
                float py = vertices[vIdx + 1];
                float pz = vertices[vIdx + 2];
                float tu = vertices[vIdx + 3];
                float tv = vertices[vIdx + 4];
                vIdx += 5;

                posBuffer.set(px, py, pz, 1.0F);
                posBuffer.transform(modelMatrix);

                consumer.vertex(posBuffer.x(), posBuffer.y(), posBuffer.z(), red, green, blue, alpha, tu, tv, overlay, light, normBuffer.x(), normBuffer.y(), normBuffer.z());
            }
        }
    }

    public void setPosition(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }
}
