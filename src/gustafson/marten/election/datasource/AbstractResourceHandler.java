package gustafson.marten.election.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public abstract class AbstractResourceHandler
{
    protected static final Logger log = Logger.getLogger(AbstractResourceHandler.class.getName());
    public static final EntityTag EMPTY_ETAG = new EntityTag("");

    protected final WebResource baseResource;

    public AbstractResourceHandler(final URI baseURI)
    {
        this.baseResource = Client.create().resource(baseURI);
    }

    protected final Builder createResource(final String file)
    {
        final EntityTag etag = getEtag(file);
        Builder builder = this.baseResource.path(file).getRequestBuilder();
        builder = builder.header("If-None-Match", etag);
        builder = builder.header("User-Agent", UrlLoader.USER_AGENT);
        builder = builder.accept(MediaType.WILDCARD);
        log.info("Creating resource for " + file + " with" + ((etag.getValue().length() == 0) ? "out etag" : " etag " + etag));
        return builder;
    }

    public abstract EntityTag getEtag(final String file);
    public abstract FetchResponse fetch(final String file) throws IOException;
    public abstract Document parseResponse(final InputStream stream) throws ValidityException, ParsingException, IOException;
    public abstract ClientResponse check(final String file);

}
