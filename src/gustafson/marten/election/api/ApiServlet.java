package gustafson.marten.election.api;

import gustafson.marten.election.datasource.UrlLoader;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.model.Party;
import gustafson.marten.election.util.XmlToPartyMapper;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import nu.xom.Document;

import com.sun.jersey.spi.resource.Singleton;

@Path("api")
@Singleton
public final class ApiServlet
{
    private static final Logger log = Logger.getLogger(ApiServlet.class.getName());

    private final Parties parties;

    public ApiServlet()
    {
        Parties parties;
        try
        {
            final Document data = new UrlLoader(2006).load();
            final XmlToPartyMapper mapper = new XmlToPartyMapper();
            parties = mapper.parseElectedParties(data);
        }
        catch(final Exception e)
        {
            log.severe(e.getMessage());
            parties = new Parties(new ArrayList<Party>());
        }
        this.parties = parties;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN + ";encoding=UTF-8")
    public String x()
    {
        return this.parties.toString();
    }
}
