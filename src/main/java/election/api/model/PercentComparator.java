package election.api.model;

import java.util.Comparator;

public final class PercentComparator implements Comparator<Party> {
    public static final Comparator<Party> Ascending = new PercentComparator(true);
    public static final Comparator<Party> Descending = new PercentComparator(false);

    private final int lessThan;
    private final int greaterThan;

    PercentComparator(final boolean ascending) {
        this.lessThan = ascending ? -1 : 1;
        this.greaterThan = -this.lessThan;
    }

    @Override
    public int compare(final Party one, final Party two) {
        if(one.getPercent() == two.getPercent()) {
            return one.getAbbreviation().compareTo(two.getAbbreviation());
        }
        return (one.getPercent() > two.getPercent()) ? this.greaterThan : this.lessThan;
    }
}
