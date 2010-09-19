package gustafson.marten.election.tests;

import static org.junit.Assert.assertEquals;
import gustafson.marten.election.model.Party;
import gustafson.marten.election.model.PercentComparator;

import org.junit.Test;

public class PercentComparatorTest
{
    @Test
    public void testEquality()
    {
        assertEquals(0, PercentComparator.Ascending.compare(new Party("", "", 1), new Party("", "", 1)));
        assertEquals(0, PercentComparator.Descending.compare(new Party("", "", 1), new Party("", "", 1)));
    }

    @Test
    public void testEqualityWithFallbackOnAbbreviation()
    {
        assertEquals(-1, PercentComparator.Ascending.compare(new Party("A", "", 1), new Party("B", "", 1)));
        assertEquals(-1, PercentComparator.Descending.compare(new Party("A", "", 1), new Party("B", "", 1)));
    }

    @Test
    public void firstPartyPercentIsLessThanSecondPartyPercent()
    {
        assertEquals(-1, PercentComparator.Ascending.compare(new Party("", "", 0), new Party("", "", 9)));
        assertEquals(1, PercentComparator.Descending.compare(new Party("", "", 0), new Party("", "", 9)));
    }

    @Test
    public void firstPartyPercentIsGreaterThanSecondPartyPercent()
    {
        assertEquals(1, PercentComparator.Ascending.compare(new Party("", "", 9), new Party("", "", 0)));
        assertEquals(-1, PercentComparator.Descending.compare(new Party("", "", 9), new Party("", "", 0)));
    }

}
