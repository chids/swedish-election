package gustafson.marten.election.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public final class ZipFileLoader implements Loader
{
    private static final Logger log = Logger.getLogger(Loader.class.getName());

    private File data;

    public ZipFileLoader(final int year)
    {
        final String path = getClass().getPackage().getName().replace('.', '/');
        final URL url = getClass().getClassLoader().getResource(path + "/.valnatt.zip");
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

    public static void main(String args[]) throws URISyntaxException, ZipException, IOException
    {
        final URL url = ZipFileLoader.class.getClassLoader().getResource("gustafson/marten/val/datasource/valnatt.zip");
        final ZipFile zf = new ZipFile(new File(url.toURI()));
        final ZipEntry entry = zf.getEntry("valnatt_00R.xml");
        long size = entry.getSize();
        if(size > 0)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(entry)));
            String line;
            while((line = br.readLine()) != null)
            {
                System.out.println(line);
            }
            br.close();
        }
    }
}
