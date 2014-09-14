package election.api.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class PartyComparator implements Comparator<Party>
{
    private static final Map<String, Comparator<Party>> Ascending = new HashMap<String, Comparator<Party>>();
    private static final Map<String, Comparator<Party>> Descending = new HashMap<String, Comparator<Party>>();

    static
    {
        Ascending.put("percent", PercentComparator.Ascending);
        Descending.put("percent", PercentComparator.Descending);

        Ascending.put("name", NameComparator.Ascending);
        Descending.put("name", NameComparator.Descending);
    }

    public static Comparator<Party> valueOf(final String comparatorName, final String order) {
        if(Ascending.containsKey(comparatorName.trim().toLowerCase())) {
            final String o = order.trim().toLowerCase();
            return (o.startsWith("asc") || o.equals("-")) ? Ascending.get(comparatorName) : Descending.get(comparatorName);
        }
        throw new IllegalArgumentException("Can't find comparator for: " + comparatorName);
    }
}
