package net.labymod.addons.skinlayer3d.render;

import java.util.List;

/**
 * Flattens cubes into vertex buffers for rendering
 * stores 20 floats per quad (4 verts * 5: x,y,z,u,v) + 3 normal floats
 */
public abstract class AbstractMesh {

    /**
     * mesh translation offset
     */
    protected float offsetX, offsetY, offsetZ;

    /**
     * vertex buffer: position (x,y,z) + UV (u,v) per vertex
     */
    protected final float[] vertices;

    /**
     * normal buffer: one (nx,ny,nz) vector per quad
     */
    protected final float[] normals;

    /**
     * total quad count
     */
    protected final int quadCount;

    protected AbstractMesh(List<Cube> cubes) {
        int totalQuads = countQuads(cubes);
        this.quadCount = totalQuads;
        this.vertices = new float[totalQuads * 20]; // 4 verts * 5 floats
        this.normals = new float[totalQuads * 3];

        writeVertices(cubes);
    }

    private int countQuads(List<Cube> cubes) {
        int sum = 0;
        for (Cube cube : cubes) {
            sum += cube.quadCount;
        }
        return sum;
    }

    private void writeVertices(List<Cube> cubes) {
        int vertOffset = 0;
        int normOffset = 0;

        for (Cube cube : cubes) {
            for (int q = 0; q < cube.quadCount; q++) {
                Cube.Quad quad = cube.quads[q];

                // pack vertices
                vertices[vertOffset++] = quad.x0();
                vertices[vertOffset++] = quad.y0();
                vertices[vertOffset++] = quad.z0();
                vertices[vertOffset++] = quad.u0();
                vertices[vertOffset++] = quad.v0();

                vertices[vertOffset++] = quad.x1();
                vertices[vertOffset++] = quad.y1();
                vertices[vertOffset++] = quad.z1();
                vertices[vertOffset++] = quad.u1();
                vertices[vertOffset++] = quad.v1();

                vertices[vertOffset++] = quad.x2();
                vertices[vertOffset++] = quad.y2();
                vertices[vertOffset++] = quad.z2();
                vertices[vertOffset++] = quad.u2();
                vertices[vertOffset++] = quad.v2();

                vertices[vertOffset++] = quad.x3();
                vertices[vertOffset++] = quad.y3();
                vertices[vertOffset++] = quad.z3();
                vertices[vertOffset++] = quad.u3();
                vertices[vertOffset++] = quad.v3();

                // pack normal (once per quad)
                normals[normOffset++] = quad.nx();
                normals[normOffset++] = quad.ny();
                normals[normOffset++] = quad.nz();
            }
        }
    }
}
