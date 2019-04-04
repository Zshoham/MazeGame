package algorithms.search;

import java.util.Comparator;

/**
 * Represents a state in a search problem.
 */
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


    /**
     * A state comparator which compares two states based on their cost.
     */
    public static class StateCostComparator implements Comparator<AState> {

        @Override
        public int compare(AState first, AState second) {
            return Integer.compare(first.cost, second.cost);
        }
    }
}
