package gustafson.marten.election.api;

import gustafson.marten.election.datasource.UrlLoader;
import gustafson.marten.election.datasource.XmlResourceHandler;
import gustafson.marten.election.datasource.ZipResourceHandler;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.util.XmlToPartyMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import com.google.appengine.api.urlfetch.ResponseTooLargeException;
import com.sun.jersey.spi.resource.Singleton;

@Path("")
@Singleton
public final class ApiServlet
{
    private static final Logger log = Logger.getLogger(ApiServlet.class.getName());
    private static final XmlToPartyMapper mapper = new XmlToPartyMapper();

    private final Map<String, Parties> elections = new HashMap<String, Parties>();

    public static final String COMPLETE_ZIP = "valnatt.zip";
    public static final String COMPLETE_XML = "valnatt_00R.xml";
    public static final URI ELECTION_2010;
    public static final URI ELECTION_2006;

    static
    {
        try
        {
            ELECTION_2010 = new URI("http://www.val.se/val/val2010/valnatt");
            ELECTION_2006 = new URI("http://www.val.se/val/val2006/valnatt/xml");
        }
        catch(final URISyntaxException e)
        {
            throw new AssertionError(e.getMessage());
        }
    }

    public ApiServlet()
    {
        final Map<String, UrlLoader> loaders = new HashMap<String, UrlLoader>();
        loaders.put("2006", new UrlLoader(new XmlResourceHandler(ApiServlet.ELECTION_2006)));
        loaders.put("2010", new UrlLoader(
                new ZipResourceHandler(ApiServlet.ELECTION_2010, ApiServlet.COMPLETE_ZIP, ApiServlet.COMPLETE_XML)));
        reload(loaders);
    }

    public void reload(final Map<String, UrlLoader> loaders)
    {
        this.elections.clear();
        for(final Entry<String, UrlLoader> entry : loaders.entrySet())
        {
            try
            {
                this.elections.put(entry.getKey(), loadParties(entry.getValue()));
            }
            catch(final ResponseTooLargeException e)
            {
                log.severe("Response was too large: " + e.getMessage());
            }
            catch(final Exception e)
            {
                log.severe(e.getMessage());
            }
        }
    }

    public Parties loadParties(final UrlLoader loader) throws ValidityException, IOException, ParsingException
    {
        final Document electionSummary = loader.get(COMPLETE_XML);
        return mapper.parseElectedParties(electionSummary);
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";encoding=UTF-8")
    public String listElections()
    {
        final StringBuilder html = new StringBuilder();
        html.append("<html><body><pre>");
        for(final String year : this.elections.keySet())
        {
            html.append("<a href='election/");
            html.append(year);
            html.append("'>");
            html.append(year);
            html.append("</a><br/>");
        }
        html.append("</pre></body></html>");
        return html.toString();
    }

    @GET
    @Path("election/{year}")
    @Produces(MediaType.TEXT_HTML + ";encoding=UTF-8")
    public String election(@PathParam("year") final String year)
    {
        if(this.elections.containsKey(year))
        {
            return this.elections.get(year).toString();
        }
        else
        {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }
}
