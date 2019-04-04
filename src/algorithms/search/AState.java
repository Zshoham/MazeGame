package algorithms.search;

import java.util.Comparator;

public abstract class AState {

    protected AState parent;

    protected int cost;

    public AState() {
        this.parent = null;
    }

    public AState(AState parent) {
        this.parent = parent;
    }

    public AState(int color, AState parent) {
        this.parent = parent;
    }


    public static class StateComparator implements Comparator<AState>
    {

        @Override
        public int compare(AState first, AState second) {
            return Integer.compare(first.cost, second.cost);
        }
    }
}
