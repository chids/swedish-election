package gustafson.marten.election.datasource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public final class FileLoader implements Loader
{
    private static final Logger log = Logger.getLogger(Loader.class.getName());

    private File data;

    public FileLoader(final int year)
    {
        final String path = getClass().getPackage().getName().replace('.', '/');
        final URL url = getClass().getClassLoader().getResource(path + '/' + year + ".valnatt_00R.xml");
        if(url == null)
        {
            throw new IllegalArgumentException(year + " doens't appear to have any data, did an election take place?");
        }
        else
        {
            try
            {
                this.data = new File(url.toURI());
            }
            catch(final URISyntaxException e)
            {
                throw new AssertionError(e.getMessage());
            }
        }
        log.info("Configured for: " + url);
    }

    @Override
    public Document load() throws IOException
    {
        try
        {
            return new Builder(true).build(this.data);
        }
        catch(final ValidityException e)
        {
            throw new IOException(e.getMessage(), e);
        }
        catch(final ParsingException e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }
}
