package gustafson.marten.election.tests;

import gustafson.marten.election.datasource.UrlLoader;

import java.io.IOException;

import org.junit.Test;

public class UrlLoaderTest
{

    @Test(expected = IOException.class)
    public void loadingAYearWithNoElectionFails() throws IOException
    {
        new UrlLoader(2001).load();
    }

    @Test
    public void loadingAYearWithAnElectionSucceeds() throws IOException
    {
        new UrlLoader(2006).load();
    }
}
