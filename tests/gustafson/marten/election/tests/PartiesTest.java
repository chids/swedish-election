package gustafson.marten.election.tests;

import static org.junit.Assert.assertEquals;
import gustafson.marten.election.model.Block;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.model.Party;
import gustafson.marten.election.util.PercentComparator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class PartiesTest
{
    public List<Party> mockParties()
    {
        final List<Party> parties = new ArrayList<Party>();
        parties.add(new Party("A", "A-Team", 5));
        parties.add(new Party("B", "B. A. Baracus", 10));
        parties.add(new Party("C", "Caol Ila", 20));
        parties.add(new Party("D", "Dufftown", 40));
        return parties;
    }

    @Test
    public void groupPreservesOrderAndCalculatesPercentCorrect() throws IOException
    {
        final Parties parties = new Parties(mockParties());
        final Parties grouped = parties.group(new Block("The A-Team!", "A", "B"), new Block("Single Malts", "C", "D"));
        final Iterator<Party> iterator = grouped.iterator();
        verifyParty(iterator.next(), "A, B", 15);
        verifyParty(iterator.next(), "C, D", 60);
    }

    @Test
    public void sort() throws IOException
    {
        final Parties sorted = new Parties(mockParties()).sort(PercentComparator.Descending);
        final Iterator<Party> iterator = sorted.iterator();
        assertEquals("Dufftown", iterator.next().getName());
        assertEquals("Caol Ila", iterator.next().getName());
        assertEquals("B. A. Baracus", iterator.next().getName());
        assertEquals("A-Team", iterator.next().getName());
    }

    @Test
    public void comparePreservesOrderAndCalculatesDifferenceCorrect() throws IOException
    {
        final Parties first = new Parties(mockParties());
        final Parties second = new Parties(new Party[] {new Party("A", "A-Team", 40), new Party("C", "Caol Ila", 5)});
        final Parties diff = first.compare(second);
        final Iterator<Party> iterator = diff.iterator();
        verifyParty(iterator.next(), "A-Team", 35);
        verifyParty(iterator.next(), "B. A. Baracus", 10);
        verifyParty(iterator.next(), "Caol Ila", -15);
        verifyParty(iterator.next(), "Dufftown", 40);
    }

    public void verifyParty(final Party party, final String expectedName, final double expectedPercent)
    {
        assertEquals(expectedName, party.getName());
        assertEquals("Percent", expectedPercent, party.getPercent(), 0);
    }
}
