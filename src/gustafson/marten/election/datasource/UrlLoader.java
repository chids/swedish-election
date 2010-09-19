package gustafson.marten.election.datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import com.sun.jersey.api.client.ClientResponse;

public final class UrlLoader
{
    public static final String USER_AGENT = "valapi-robot/1.0 (Google App Engine) marten.gustafson@gmail.com";

    private static final Logger log = Logger.getLogger(UrlLoader.class.getName());

    private final Map<String, Document> documents = new HashMap<String, Document>();
    private final AbstractResourceHandler handler;

    public UrlLoader(final AbstractResourceHandler handler)
    {
        this.handler = handler;
    }

    public Document get(final String file) throws ValidityException, ParsingException, IOException
    {
        loadFile(file);
        return this.documents.get(file);
    }

    public void loadFile(final String file) throws IOException
    {
        final FetchResponse response = this.handler.fetch(file);
        try
        {
            if(200 == response.status)
            {
                final Document parsed = this.handler.parseResponse(response.stream);
                this.documents.put(file, parsed);
                log.info("Got and parsed file " + file + " storing with etag: " + response.etag);
            }
            else if(304 == response.status)
            {
                log.info(file + " not modified");
            }
            else
            {
                throw new IOException("Failed to get document, staus was: " + response.status + " for: " + file);
            }
        }
        catch(final Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly(response.stream);
        }
    }

    public boolean hasChanged(final String file)
    {
        if(has(file))
        {
            final ClientResponse response = this.handler.check(file);
            try
            {
                log.info("Got " + response.getStatus() + " - " + response.getClientResponseStatus() + " when trying to get: " + file);
                return response.getStatus() != 304;
            }
            finally
            {
                response.close();
            }
        }
        else
        {
            log.info(file + " is not familiar, returing true for hasChanged");
            return true;
        }
    }

    public boolean has(final String file)
    {
        return this.documents.containsKey(file);
    }
}