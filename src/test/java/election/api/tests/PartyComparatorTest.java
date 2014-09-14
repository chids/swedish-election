package election.api.tests;

import static org.junit.Assert.assertEquals;

import election.api.model.PartyComparator;
import election.api.model.PercentComparator;

import org.junit.Test;

public class PartyComparatorTest
{

    @Test
    public void valueOfParsesAllVariationsForAscending()
    {
        assertEquals(PercentComparator.Ascending, PartyComparator.valueOf("percent", "asc"));
        assertEquals(PercentComparator.Ascending, PartyComparator.valueOf("percent", "ASC"));
        assertEquals(PercentComparator.Ascending, PartyComparator.valueOf("percent", "ascending"));
        assertEquals(PercentComparator.Ascending, PartyComparator.valueOf("percent", "AsceNdiNg"));
        assertEquals(PercentComparator.Ascending, PartyComparator.valueOf("percent", "-"));
    }

    @Test
    public void valueOfParsesAllVariationsForDescending()
    {
        assertEquals(PercentComparator.Descending, PartyComparator.valueOf("percent", "desc"));
        assertEquals(PercentComparator.Descending, PartyComparator.valueOf("percent", "DESC"));
        assertEquals(PercentComparator.Descending, PartyComparator.valueOf("percent", "descending"));
        assertEquals(PercentComparator.Descending, PartyComparator.valueOf("percent", "DeScendIng"));
        assertEquals(PercentComparator.Descending, PartyComparator.valueOf("percent", "+"));
    }
}
