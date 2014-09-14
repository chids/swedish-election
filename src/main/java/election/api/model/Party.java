package election.api.model;

public final class Party
{
    private final String name;
    private final String abbreviation;
    private final double percent;

    public Party(final String abbreviation, final String name, final double percent)
    {
        this.abbreviation = abbreviation;
        this.name = name;
        this.percent = percent;
    }

    public String getName()
    {
        return this.name;
    }

    public double getPercent()
    {
        return this.percent;
    }

    public String getAbbreviation()
    {
        return this.abbreviation;
    }

    @Override
    public String toString()
    {
        return this.abbreviation + ' ' + this.percent + "% " + this.name;
    }
}