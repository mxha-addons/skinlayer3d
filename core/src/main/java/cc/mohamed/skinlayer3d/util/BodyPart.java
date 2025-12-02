package cc.mohamed.skinlayer3d.util;

import cc.mohamed.skinlayer3d.model.TextureData;
import cc.mohamed.skinlayer3d.render.AbstractMesh;
import cc.mohamed.skinlayer3d.render.Cube;
import cc.mohamed.skinlayer3d.render.MeshGenerator;

import java.util.List;
import java.util.function.Function;

/**
 * Defines body part's second layer with their UV coordinates and mesh generation
 */
public enum BodyPart {
    HEAD(8, 8, 8, 32, 0, false, 0.6f, 0.0f),
    TORSO(8, 12, 4, 16, 32, true, 0f, -0.2f),
    LEFT_LEG(4, 12, 4, 0, 48, true, 0f, -0.2f),
    RIGHT_LEG(4, 12, 4, 0, 32, true, 0f, -0.2f),
    LEFT_ARM(4, 12, 4, 48, 48, true, -2f, -0.1f),
    RIGHT_ARM(4, 12, 4, 40, 32, true, -2f, -0.1f),
    LEFT_ARM_SLIM(3, 12, 4, 48, 48, true, -2f, -0.1f),
    RIGHT_ARM_SLIM(3, 12, 4, 40, 32, true, -2f, -0.1f),

    // Legacy enum values for OffsetData compatibility
    LEGS(4, 12, 4, 0, 32, true, 0f, -0.2f),
    ARMS(4, 12, 4, 48, 48, true, -2f, -0.1f),
    ARMS_SLIM(3, 12, 4, 48, 48, true, -2f, -0.1f);

    private final int width;
    private final int height;
    private final int depth;
    private final int textureU;
    private final int textureV;
    private final boolean topPivot;
    private final float pivotOffset;
    public final float verticalOffset;

    BodyPart(int width, int height, int depth, int textureU, int textureV, boolean topPivot, float pivotOffset, float verticalOffset) {
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.textureU = textureU;
        this.textureV = textureV;
        this.topPivot = topPivot;
        this.pivotOffset = pivotOffset;
        this.verticalOffset = verticalOffset;
    }

    /**
     * Generates a mesh for this body part overlay.
     *
     * @param texture     the player skin texture
     * @param meshFactory factory to create mesh from cubes
     * @return the generated mesh
     */
    public <T extends AbstractMesh> T createMesh(TextureData texture, Function<List<Cube>, T> meshFactory) {
        return MeshGenerator.createMesh(texture, width, height, depth, textureU, textureV, topPivot, pivotOffset, meshFactory);
    }
}
