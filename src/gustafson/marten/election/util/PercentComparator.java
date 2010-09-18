package gustafson.marten.election.util;

import gustafson.marten.election.model.Party;

import java.util.Comparator;

public final class PercentComparator implements Comparator<Party>
{
    public static PercentComparator Ascending = new PercentComparator(true);
    public static PercentComparator Descending = new PercentComparator(false);
    
    private final int lessThan;
    private final int greaterThan;

    private PercentComparator(final boolean ascending)
    {
        this.lessThan = ascending ? -1 : 1;
        this.greaterThan = -this.lessThan;
    }

    @Override
    public int compare(final Party one, final Party two)
    {
        if(one.getPercent() == two.getPercent())
        {
            return one.getAbbreviation().compareTo(two.getAbbreviation());
        }
        else
        {
            return (one.getPercent() > two.getPercent()) ? this.greaterThan : this.lessThan;
        }
    }

}
