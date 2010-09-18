package gustafson.marten.election.model;

public final class Block
{

    private final String[] members;
    private final String name;

    public Block(final String name, final String... members)
    {
        this.name = name;
        this.members = members;
    }

    public String[] getMembers()
    {
        return this.members;
    }

    public String getName()
    {
        return this.name;
    }
}
