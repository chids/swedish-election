package gustafson.marten.election.model;

import java.util.ArrayList;
import java.util.List;

public final class Block
{

    private final String[] members;
    private final String name;

    public Block(final String name, final String... members)
    {
        this.name = name;
        this.members = members;
    }

    public Block(final String name, final List<String> members)
    {
        this(name, members.toArray(new String[members.size()]));
    }

    public String[] getMembers()
    {
        return this.members;
    }

    public String getName()
    {
        return this.name;
    }

    public static Block valueOf(final String string)
    {
        if(string != null && string.lastIndexOf(':') < string.length())
        {
            final String name = string.substring(0, string.indexOf(':'));
            final String[] members = string.substring(string.indexOf(':') + 1).split(",");
            return new Block(name, filterMembers(members));
        }
        else
        {
            return null;
        }
    }

    private static List<String> filterMembers(final String[] members)
    {
        final List<String> result = new ArrayList<String>();
        for(final String member : members)
        {
            if(member.trim().length() > 0)
            {
                result.add(member);
            }
        }
        return result;
    }
}
