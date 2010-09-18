package gustafson.marten.election.datasource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import nu.xom.Builder;
import nu.xom.Document;

public class UrlLoader implements Loader
{
    private static final Logger log = Logger.getLogger(Loader.class.getName());

    private final URL url;

    public UrlLoader(final int year) throws MalformedURLException
    {
        this.url = new URL("http://www.val.se/val/val" + year + "/valnatt/xml/valnatt_00R.xml");
        log.info("Configured for: " + this.url);
    }

    /*
    < Last-Modified: Tue, 19 Sep 2006 08:48:53 GMT
    < ETag: "117001b-8b992-41dca8f61e740"
     */
    @Override
    public Document load() throws IOException
    {
        HttpURLConnection connection = null;
        try
        {
            connection = (HttpURLConnection)this.url.openConnection();
            connection.setRequestProperty("User-Agent", "valapi-robot/1.0 (Google App Engine) marten.gustafson@gmail.com");
            if(200 == connection.getResponseCode())
            {
                return new Builder(true).build(connection.getInputStream()).getDocument();
            }
            else
            {
                throw new IOException("Failed to get document, staus was: " + connection.getResponseCode() + " for: " + this.url);
            }
        }
        catch(final Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
        finally
        {
            if(connection != null)
            {
                connection.disconnect();
            }
        }
    }
}
