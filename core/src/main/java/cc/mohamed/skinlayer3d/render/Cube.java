package cc.mohamed.skinlayer3d.render;

import cc.mohamed.skinlayer3d.model.Direction;

/**
 * 1x1x1 cube with up to 6 face quads
 */
public class Cube {

    private static final float SCALE = 1.0F / 16.0F;

    public final Quad[] quads = new Quad[6];
    public final int quadCount;

    public Cube(int texU, int texV, float posX, float posY, float posZ, float texWidth, float texHeight, int visibilityMask) {

        float u0 = texU / texWidth;
        float u1 = (texU + 1.0F) / texWidth;
        float v0 = texV / texHeight;
        float v1 = (texV + 1.0F) / texHeight;

        float x0 = posX * SCALE;
        float y0 = posY * SCALE;
        float z0 = posZ * SCALE;
        float x1 = (posX + 1) * SCALE;
        float y1 = (posY + 1) * SCALE;
        float z1 = (posZ + 1) * SCALE;

        int count = 0;

        for (Direction dir : Direction.values()) {
            // skip hidden faces
            if ((visibilityMask & (1 << dir.ordinal())) == 0) continue;

            quads[count++] = switch (dir) {
                case DOWN ->
                        new Quad(x1, y0, z1, u1, v0, x0, y0, z1, u0, v0, x0, y0, z0, u0, v1, x1, y0, z0, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
                case UP ->
                        new Quad(x1, y1, z0, u1, v0, x0, y1, z0, u0, v0, x0, y1, z1, u0, v1, x1, y1, z1, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
                case NORTH ->
                        new Quad(x1, y0, z0, u1, v0, x0, y0, z0, u0, v0, x0, y1, z0, u0, v1, x1, y1, z0, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
                case SOUTH ->
                        new Quad(x0, y0, z1, u1, v0, x1, y0, z1, u0, v0, x1, y1, z1, u0, v1, x0, y1, z1, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
                case WEST ->
                        new Quad(x0, y0, z0, u1, v0, x0, y0, z1, u0, v0, x0, y1, z1, u0, v1, x0, y1, z0, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
                case EAST ->
                        new Quad(x1, y0, z1, u1, v0, x1, y0, z0, u0, v0, x1, y1, z0, u0, v1, x1, y1, z1, u1, v1, dir.getStepX(), dir.getStepY(), dir.getStepZ());
            };
        }

        quadCount = count;
    }

    public record Quad(float x0, float y0, float z0, float u0, float v0, float x1, float y1, float z1, float u1, float v1, float x2, float y2, float z2, float u2, float v2, float x3, float y3, float z3, float u3, float v3, float nx, float ny, float nz) {
    }
}
