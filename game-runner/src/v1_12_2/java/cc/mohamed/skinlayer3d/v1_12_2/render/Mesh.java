package cc.mohamed.skinlayer3d.v1_12_2.render;

import cc.mohamed.skinlayer3d.render.AbstractMesh;
import cc.mohamed.skinlayer3d.render.Cube;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class Mesh extends AbstractMesh {

    public Mesh(List<Cube> cubes) {
        super(cubes);
    }

    public void render(float scale) {
        if (quadCount == 0) return;

        // add polygon offset to prevent z-fighting
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(-1.0f, -1.0f);

        Tessellator tessellator = Tessellator.getInstance();
        var buffer = tessellator.getBuffer();


        if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
            GL11.glTranslatef(offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);
        }

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);

        int vIdx = 0;
        int nIdx = 0;

        for (int q = 0; q < quadCount; q++) {
            float nx = normals[nIdx];
            float ny = normals[nIdx + 1];
            float nz = normals[nIdx + 2];
            nIdx += 3;

            for (int v = 0; v < 4; v++) {
                float px = vertices[vIdx];
                float py = vertices[vIdx + 1];
                float pz = vertices[vIdx + 2];
                float tu = vertices[vIdx + 3];
                float tv = vertices[vIdx + 4];
                vIdx += 5;

                buffer.pos(px, py, pz).tex(tu, tv).normal(nx, ny, nz).endVertex();
            }
        }

        tessellator.draw();

        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }

    public void setPosition(float x, float y, float z) {
        this.offsetX = x;
        this.offsetY = y;
        this.offsetZ = z;
    }
}
