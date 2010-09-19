package gustafson.marten.election.datasource;

import java.io.InputStream;

import javax.ws.rs.core.EntityTag;

import com.sun.jersey.api.client.ClientResponse;

public final class FetchResponse
{
    public final EntityTag etag;
    public final int status;
    public final InputStream stream;

    public FetchResponse(final int status, final InputStream stream, final EntityTag etag)
    {
        this.status = status;
        this.stream = stream;
        this.etag = etag;
    }

    public FetchResponse(final ClientResponse response)
    {
        this.status = response.getStatus();
        this.etag = response.getEntityTag();
        this.stream = response.getEntityInputStream();
    }
}
