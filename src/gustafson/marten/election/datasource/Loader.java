package gustafson.marten.election.datasource;

import java.io.IOException;

import nu.xom.Document;

public interface Loader
{
    Document load() throws IOException;
}