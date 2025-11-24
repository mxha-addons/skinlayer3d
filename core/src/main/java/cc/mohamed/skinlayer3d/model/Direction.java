package cc.mohamed.skinlayer3d.model;

public enum Direction {
    DOWN(0, -1, 0, Axis.Y),
    UP(0, 1, 0, Axis.Y),
    NORTH(0, 0, -1, Axis.Z),
    SOUTH(0, 0, 1, Axis.Z),
    WEST(-1, 0, 0, Axis.X),
    EAST(1, 0, 0, Axis.X);

    private final int stepX;
    private final int stepY;
    private final int stepZ;
    private final Axis axis;

    Direction(int stepX, int stepY, int stepZ, Axis axis) {
        this.stepX = stepX;
        this.stepY = stepY;
        this.stepZ = stepZ;
        this.axis = axis;
    }

    public int getStepX() {
        return stepX;
    }

    public int getStepY() {
        return stepY;
    }

    public int getStepZ() {
        return stepZ;
    }

    public Axis getAxis() {
        return axis;
    }

    /**
     * @return the opposite direction (e.g., UP returns DOWN)
     */
    public Direction getOpposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case WEST -> EAST;
            case EAST -> WEST;
        };
    }

    public enum Axis {
        X,
        Y,
        Z;
    }
}
