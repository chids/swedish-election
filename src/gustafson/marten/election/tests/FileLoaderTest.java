package gustafson.marten.election.tests;

import gustafson.marten.election.datasource.FileLoader;

import org.junit.Test;

public class FileLoaderTest
{

    @Test(expected = IllegalArgumentException.class)
    public void creartingALoaderForAYearWithNoElectionFails()
    {
        new FileLoader(2001).hashCode();
    }

    @Test
    public void creartingALoaderForAYearWithElectionSucceeds()
    {
        new FileLoader(2006).hashCode();
    }
}
