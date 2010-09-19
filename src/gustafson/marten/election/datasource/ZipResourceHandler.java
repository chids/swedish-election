package gustafson.marten.election.datasource;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.ws.rs.core.EntityTag;

import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public final class ZipResourceHandler extends AbstractResourceHandler
{
    private static final int GAE_MAX_DOWNLOADABLE_SIZE = 1024 * 1000;
    private static final int BUFFER_SIZE = 2048;
    private EntityTag etag = new EntityTag("");
    private final String fileToFetchFromZip;
    private final String zipFile;

    public ZipResourceHandler(final URI baseUri, final String zipFile, final String fileToFetchFromZip)
    {
        super(baseUri);
        this.zipFile = zipFile;
        this.fileToFetchFromZip = fileToFetchFromZip;
    }

    @Override
    public EntityTag getEtag(final String file)
    {
        return this.etag;
    }

    @Override
    public FetchResponse fetch(final String ignored) throws IOException
    {
        final Builder resource = createResource(this.zipFile);
        final ClientResponse head = resource.head();
        this.etag = head.getEntityTag();
        final int size = head.getLength();
        if(head.getStatus() == 200 && size > GAE_MAX_DOWNLOADABLE_SIZE)
        {
            log.info("Attempting to download zip archive with size: " + size);
            final ByteArrayOutputStream target = new ByteArrayOutputStream(size);
            int startPosition = 0;
            while(startPosition < size)
            {
                int endPosition = startPosition + GAE_MAX_DOWNLOADABLE_SIZE;
                resource.header("Range", "bytes=" + startPosition + '-' + endPosition);
                final ClientResponse chunk = resource.get(ClientResponse.class);
                final InputStream stream = chunk.getEntityInputStream();
                startPosition += IOUtils.copy(stream, target);
                IOUtils.closeQuietly(stream);
                log.info("Chunked download completed chunk, total downloaded so far is: " + startPosition);
            }
            return new FetchResponse(head.getStatus(), new ByteArrayInputStream(target.toByteArray()), head.getEntityTag());
        }
        else
        {
            return new FetchResponse(resource.get(ClientResponse.class));
        }
    }

    @Override
    public ClientResponse check(final String ignored)
    {
        return createResource(this.zipFile).head();
    }

    @Override
    public Document parseResponse(final InputStream stream) throws IOException, ValidityException, ParsingException
    {
        final ZipInputStream zip = new ZipInputStream(stream);
        try
        {
            ZipEntry entry;
            while((entry = zip.getNextEntry()) != null)
            {
                final String name = entry.getName();
                if(this.fileToFetchFromZip.equals(name))
                {
                    log.info("Unzipping: " + name);
                    final ByteArrayOutputStream target = new ByteArrayOutputStream(BUFFER_SIZE);
                    final BufferedOutputStream bos = new BufferedOutputStream(target, BUFFER_SIZE);
                    final byte[] buffer = new byte[BUFFER_SIZE];
                    int size;
                    while((size = zip.read(buffer, 0, buffer.length)) != -1)
                    {
                        bos.write(buffer, 0, size);
                    }
                    bos.flush();
                    bos.close();
                    zip.closeEntry();
                    return new nu.xom.Builder(true).build(new ByteArrayInputStream(target.toByteArray())).getDocument();
                }
                else
                {
                    log.info("Skipping " + name);
                }
            }
        }
        finally
        {
            zip.close();
        }
        throw new FileNotFoundException(this.fileToFetchFromZip + " wasn't present in archive");
    }
}
