package election.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public final class Parties implements Iterable<Party> {
    private final Map<String, Party> parties = new LinkedHashMap<String, Party>();

    public Parties(final Iterable<Party> parties) {
        for(final Party party : parties) {
            this.parties.put(party.getAbbreviation(), party);
        }
    }

    public Parties(final Party... parties) {
        for(final Party party : parties) {
            this.parties.put(party.getAbbreviation(), party);
        }
    }

    public Parties sort(final Comparator<Party> comparator) {
        final TreeSet<Party> sorted = new TreeSet<Party>(comparator);
        sorted.addAll(this.parties.values());
        return new Parties(sorted);
    }

    public boolean has(final String abbreviation) {
        return this.parties.containsKey(abbreviation);
    }

    private boolean has(final Party party) {
        return has(party.getAbbreviation());
    }

    public Party get(final String abbreviation) {
        return this.parties.get(abbreviation);
    }

    public Party get(final Party party) {
        return this.parties.get(party.getAbbreviation());
    }

    public Parties group(final Block... blocks) {
        return group(Arrays.asList(blocks));
    }

    public Parties group(final Iterable<Block> blocks) {
        final List<Party> grouped = new ArrayList<Party>();
        for(final Block block : blocks)
        {
            final StringBuilder members = new StringBuilder();
            double total = 0;
            for(final String member : block.getMembers())
            {
                final Party party = this.get(member);
                if(party != null)
                {
                    if(members.length() > 0)
                    {
                        members.append(", ");
                    }
                    members.append(party.getAbbreviation());
                    total += party.getPercent();
                }
            }
            if(members.length() > 0) {
                grouped.add(new Party(block.getName(), members.toString(), Math.round(total * 100.0) / 100.0));
            }
        }
        return new Parties(grouped);
    }

    public Parties compare(final Parties that) {
        final List<Party> result = new ArrayList<Party>();
        for(final Party party : this)
        {
            if(that.has(party)) {
                final double differance = that.get(party).getPercent() - party.getPercent();
                result.add(new Party(party.getAbbreviation(), party.getName(), differance));
            }
            else {
                result.add(party);
            }
        }
        return new Parties(result);
    }

    @Override
    public Iterator<Party> iterator() {
        return this.parties.values().iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for(final Party party : this) {
            sb.append(party);
        }
        return sb.toString();
    }
}