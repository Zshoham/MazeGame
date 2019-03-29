package algorithms.search;

public abstract class AState {

    public int color;

    protected AState parent;

    protected int cost;

    public AState() {
        this.color = -1;
        this.parent = null;
    }

    public AState(int color) {
        this.color = color;
        this.parent = null;
    }

    public AState(AState parent) {
        this.color = -1;
        this.parent = parent;
    }

    public AState(int color, AState parent) {
        this.color = color;
        this.parent = parent;
    }


    //TODO: add state cost ?
}
