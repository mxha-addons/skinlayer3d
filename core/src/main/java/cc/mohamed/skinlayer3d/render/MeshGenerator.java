package cc.mohamed.skinlayer3d.render;

import cc.mohamed.skinlayer3d.model.Direction;
import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.model.UV;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Generates cubes for a body part.
 */
public class MeshGenerator {

    protected final TextureData skin;
    protected final int sizeX, sizeY, sizeZ;
    protected final int texBaseU, texBaseV;

    private static final Direction[] ALL_DIRECTIONS = Direction.values();

    /**
     * @param skin     texture data
     * @param width    model width
     * @param height   model height
     * @param depth    model depth
     * @param textureU X offset of the part on the texture
     * @param textureV Y offset of the part on the texture
     */
    public MeshGenerator(TextureData skin, int width, int height, int depth, int textureU, int textureV) {
        this.skin = skin;
        this.sizeX = width;
        this.sizeY = height;
        this.sizeZ = depth;
        this.texBaseU = textureU;
        this.texBaseV = textureV;
    }

    /**
     * Generates cubes for all visible pixels on all 6 faces.
     *
     * @param pivotAtTop  whether the pivot is at the top of the part
     * @param pivotOffset vertical pivot offset
     */
    public List<Cube> generateCubes(boolean pivotAtTop, float pivotOffset) {
        List<Cube> result = new ArrayList<>();

        float baseX = -sizeX / 2f;
        float baseY = pivotAtTop ? pivotOffset : -sizeY + pivotOffset;
        float baseZ = -sizeZ / 2f;

        for (Direction face : ALL_DIRECTIONS) {
            int faceW = faceDimU(face);
            int faceH = faceDimV(face);

            for (int u = 0; u < faceW; u++) {
                for (int v = 0; v < faceH; v++) {
                    UV texUV = faceToTexture(face, u, v);
                    if (!skin.isPresent(texUV)) continue;

                    int[] pos = faceToWorld(face, u, v);
                    int visibility = computeHiddenFaces(face, u, v, pos[0], pos[1], pos[2]);

                    if (visibility != 0) {
                        result.add(new Cube(texUV.u(), texUV.v(), baseX + pos[0], baseY + pos[1], baseZ + pos[2], skin.width(), skin.height(), visibility));
                    }
                }
            }
        }

        return result;
    }

    public static <T extends AbstractMesh> T createMesh(TextureData textureData, int width, int height, int depth, int textureU, int textureV, boolean topPivot, float rotationOffset, Function<List<Cube>, T> meshFactory) {
        MeshGenerator generator = new MeshGenerator(textureData, width, height, depth, textureU, textureV);
        return meshFactory.apply(generator.generateCubes(topPivot, rotationOffset));
    }

    /**
     * @return bitmask of visible faces (indexed by Direction.ordinal())
     */
    private int computeHiddenFaces(Direction currentFace, int faceU, int faceV, int voxelX, int voxelY, int voxelZ) {
        UV currentUV = faceToTexture(currentFace, faceU, faceV);
        boolean isSolid = skin.isSolid(currentUV);
        int visible = 0;

        // always draw the front face
        visible |= (1 << currentFace.ordinal());

        // check side faces visibility
        for (Direction dir : ALL_DIRECTIONS) {
            if (dir.getAxis() == currentFace.getAxis()) continue;

            if (isFaceVisible(dir, currentFace, voxelX, voxelY, voxelZ, isSolid)) {
                visible |= (1 << dir.ordinal());
            }
        }

        // back face only if at boundary
        if (isBackFaceVisible(currentFace, voxelX, voxelY, voxelZ)) {
            visible |= (1 << currentFace.getOpposite().ordinal());
        }

        return visible;
    }

    /**
     * @return true if this face is hidden by a neighboring voxel
     */
    private boolean isFaceVisible(Direction checkDir, Direction sourceFace, int x, int y, int z, boolean sourceSolid) {
        int nx = x + checkDir.getStepX();
        int ny = y + checkDir.getStepY();
        int nz = z + checkDir.getStepZ();

        // check same-face neighbor
        int[] sameFaceUV = worldToFaceUV(nx, ny, nz, sourceFace);
        int[] faceSize = {faceDimU(sourceFace), faceDimV(sourceFace)};

        if (isOnFace(sameFaceUV, faceSize)) {
            UV neighborUV = faceToTexture(sourceFace, sameFaceUV[0], sameFaceUV[1]);
            if (skin.isPresent(neighborUV)) {
                return sourceSolid && !skin.isSolid(neighborUV);
            }
            return true;
        }

        // check adjacent face
        int[] adjFaceUV = worldToFaceUV(x, y, z, checkDir);
        UV adjPixelUV = faceToTexture(checkDir, adjFaceUV[0], adjFaceUV[1]);

        if (skin.isPresent(adjPixelUV)) {
            return sourceSolid && !skin.isSolid(adjPixelUV);
        }

        return true;
    }

    /**
     * @return true if the back face should be rendered
     */
    private boolean isBackFaceVisible(Direction sourceFace, int x, int y, int z) {
        return switch (sourceFace) {
            case DOWN -> y == 0;
            case UP -> y == sizeY - 1;
            case NORTH -> z == 0;
            case SOUTH -> z == sizeZ - 1;
            case WEST -> x == 0;
            case EAST -> x == sizeX - 1;
        };
    }

    private boolean isOnFace(int[] uv, int[] size) {
        return uv[0] >= 0 && uv[0] < size[0] && uv[1] >= 0 && uv[1] < size[1];
    }


    private int faceDimU(Direction face) {
        return switch (face) {
            case UP, DOWN, NORTH, SOUTH -> sizeX;
            case WEST, EAST -> sizeZ;
        };
    }

    private int faceDimV(Direction face) {
        return switch (face) {
            case UP, DOWN -> sizeZ;
            case NORTH, SOUTH, WEST, EAST -> sizeY;
        };
    }

    private int[] faceToWorld(Direction face, int u, int v) {
        return switch (face) {
            case DOWN -> new int[]{u, 0, sizeZ - 1 - v};
            case UP -> new int[]{u, sizeY - 1, sizeZ - 1 - v};
            case NORTH -> new int[]{u, v, 0};
            case SOUTH -> new int[]{sizeX - 1 - u, v, sizeZ - 1};
            case WEST -> new int[]{0, v, sizeZ - 1 - u};
            case EAST -> new int[]{sizeX - 1, v, u};
        };
    }

    private UV faceToTexture(Direction face, int u, int v) {
        return switch (face) {
            case DOWN -> new UV(texBaseU + sizeZ + u, texBaseV + v);
            case UP -> new UV(texBaseU + sizeX + sizeZ + u, texBaseV + v);
            case NORTH -> new UV(texBaseU + sizeZ + u, texBaseV + sizeZ + v);
            case SOUTH -> new UV(texBaseU + sizeZ + sizeX + sizeZ + u, texBaseV + sizeZ + v);
            case WEST -> new UV(texBaseU + u, texBaseV + sizeZ + v);
            case EAST -> new UV(texBaseU + sizeZ + sizeX + u, texBaseV + sizeZ + v);
        };
    }

    private int[] worldToFaceUV(int x, int y, int z, Direction face) {
        return switch (face) {
            case DOWN, UP -> new int[]{x, sizeZ - 1 - z};
            case NORTH -> new int[]{x, y};
            case SOUTH -> new int[]{sizeX - 1 - x, y};
            case WEST -> new int[]{sizeZ - 1 - z, y};
            case EAST -> new int[]{z, y};
        };
    }
}
