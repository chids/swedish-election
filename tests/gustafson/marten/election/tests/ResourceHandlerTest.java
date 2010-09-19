package gustafson.marten.election.tests;

import static org.junit.Assert.assertEquals;
import gustafson.marten.election.api.ApiServlet;
import gustafson.marten.election.datasource.XmlResourceHandler;
import gustafson.marten.election.datasource.ZipResourceHandler;

import java.io.IOException;
import java.net.URISyntaxException;

import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.junit.Test;

public class ResourceHandlerTest
{

    @Test
    public void zipResourceHandler() throws IOException, ValidityException, ParsingException, URISyntaxException
    {
        final ZipResourceHandler handler = new ZipResourceHandler(ApiServlet.ELECTION_2010, ApiServlet.COMPLETE_ZIP,
                ApiServlet.COMPLETE_XML);
        assertEquals(200, handler.check("").getStatus());
        assertEquals(200, handler.fetch("").status);
        assertEquals(304, handler.check("").getStatus());
    }

    @Test
    public void xmlResourceHandler() throws IOException, ValidityException, ParsingException, URISyntaxException
    {
        final XmlResourceHandler handler = new XmlResourceHandler(ApiServlet.ELECTION_2006);
        assertEquals(200, handler.check(ApiServlet.COMPLETE_XML).getStatus());
        assertEquals(200, handler.fetch(ApiServlet.COMPLETE_XML).status);
        assertEquals(304, handler.check(ApiServlet.COMPLETE_XML).getStatus());
    }
}