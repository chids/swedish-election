package gustafson.marten.election.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.EntityTag;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public final class XmlResourceHandler extends AbstractResourceHandler
{
    private final Map<String, EntityTag> etags = new HashMap<String, EntityTag>();

    public XmlResourceHandler(final URI baseURI)
    {
        super(baseURI);
    }

    @Override
    public EntityTag getEtag(final String file)
    {
        return this.etags.containsKey(file) ? this.etags.get(file) : EMPTY_ETAG;
    }

    @Override
    public FetchResponse fetch(final String file)
    {
        final Builder resource = createResource(file);
        final ClientResponse response = resource.get(ClientResponse.class);
        this.etags.put(file, response.getEntityTag());
        return new FetchResponse(response);
    }

    @Override
    public ClientResponse check(final String file)
    {
        return createResource(file).head();
    }

    @Override
    public Document parseResponse(final InputStream stream) throws ValidityException, ParsingException, IOException
    {
        try
        {
            return new nu.xom.Builder(true).build(stream).getDocument();
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
    }
}