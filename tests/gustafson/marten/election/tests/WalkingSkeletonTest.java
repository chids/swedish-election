package gustafson.marten.election.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gustafson.marten.election.api.ApiServlet;
import gustafson.marten.election.datasource.UrlLoader;
import gustafson.marten.election.datasource.XmlResourceHandler;
import gustafson.marten.election.datasource.ZipResourceHandler;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.model.Party;
import gustafson.marten.election.model.PercentComparator;
import gustafson.marten.election.util.XmlToPartyMapper;

import java.io.IOException;

import org.junit.Test;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class WalkingSkeletonTest
{

    @Test
    public void walkWith2006Election() throws IOException, ValidityException, ParsingException
    {
        walk(26.1, new UrlLoader(new XmlResourceHandler(ApiServlet.ELECTION_2006)));
    }

    @Test
    public void walkWith2010Election() throws IOException, ValidityException, ParsingException
    {
        walk(0, new UrlLoader(new ZipResourceHandler(ApiServlet.ELECTION_2010, ApiServlet.COMPLETE_ZIP, ApiServlet.COMPLETE_XML)));
    }

    private void walk(final double expectedPercent, final UrlLoader loader) throws IOException, ValidityException, ParsingException
    {
        final XmlToPartyMapper mapper = new XmlToPartyMapper();

        final Document document = loader.get(ApiServlet.COMPLETE_XML);
        final Parties elected = mapper.parseElectedParties(document);

        assertTrue("Data for M: '" + elected + "'", elected.has("M"));

        final Party moderaterna = elected.get("M");
        assertEquals("Moderata Samlingspartiet", moderaterna.getName());
        assertEquals("Percent", expectedPercent, moderaterna.getPercent(), 0);

        for(Party party : elected.sort(PercentComparator.Descending))
        {
            System.err.println(party);
        }
    }
}
