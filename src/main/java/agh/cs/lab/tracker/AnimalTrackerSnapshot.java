package agh.cs.lab.tracker;

public final class AnimalTrackerSnapshot {

    private final int children;
    private final int descendants;

    public AnimalTrackerSnapshot(int children, int descendants) {
        this.children = children;
        this.descendants = descendants;
    }

    public int getChildren() {
        return children;
    }

    public int getDescendants() {
        return descendants;
    }
}
