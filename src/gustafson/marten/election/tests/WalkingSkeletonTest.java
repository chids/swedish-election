package gustafson.marten.election.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gustafson.marten.election.datasource.FileLoader;
import gustafson.marten.election.datasource.Loader;
import gustafson.marten.election.datasource.UrlLoader;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.model.Party;
import gustafson.marten.election.util.PercentComparator;
import gustafson.marten.election.util.XmlToPartyMapper;

import java.io.IOException;

public class WalkingSkeletonTest
{

    public void walkWithFileLoader() throws IOException
    {
        walk(new FileLoader(2006));
    }

    public void walkWithUrlLoader() throws IOException
    {
        walk(new UrlLoader(2006));
    }

    private void walk(final Loader loader) throws IOException
    {
        final XmlToPartyMapper mapper = new XmlToPartyMapper();

        final Parties elected = mapper.parseElectedParties(loader.load());

        assertTrue("Data for M: " + elected, elected.has("M"));

        final Party moderaterna = elected.get("M");
        assertEquals("Moderata Samlingspartiet", moderaterna.getName());
        assertEquals("Percent", 26.1, moderaterna.getPercent(), 0);

        for(Party party : elected.sort(PercentComparator.Descending))
        {
            System.err.println(party);
        }
    }
}
