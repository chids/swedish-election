package election.api.model;

import java.util.Comparator;

public final class NameComparator implements Comparator<Party>
{
    public static final Comparator<Party> Ascending = new NameComparator(true);
    public static final Comparator<Party> Descending = new NameComparator(false);
    private final boolean ascending;

    NameComparator(final boolean ascending)
    {
        this.ascending = ascending;
    }

    @Override
    public int compare(final Party one, final Party two)
    {
        final int abbreviation = one.getAbbreviation().compareTo(two.getAbbreviation());
        final int comparison = ((abbreviation == 0) ? one.getName().compareTo(two.getName()) : abbreviation);
        return this.ascending ? comparison : -comparison;
    }
}
