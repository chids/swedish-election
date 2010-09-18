package gustafson.marten.election.util;

import static gustafson.marten.election.util.XomUtil.iterable;
import gustafson.marten.election.model.Parties;
import gustafson.marten.election.model.Party;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Nodes;

public final class XmlToPartyMapper
{
    private static final NumberFormat SWEDISH_NUMBER_FORMAT = NumberFormat.getNumberInstance(new Locale("sv", "se"));

    public Parties parseElectedParties(final Document document) throws IOException
    {
        final Set<Party> result = new HashSet<Party>();
        final Element root = document.getRootElement();
        final Elements parties = root.getChildElements("PARTI");
        for(Element element : iterable(parties))
        {
            final String abbreviation = element.getAttributeValue("F�RKORTNING");
            final String name = element.getAttributeValue("BETECKNING");
            final double percent = getPercent(root, abbreviation);
            final Party party = new Party(abbreviation, name, percent);
            result.add(party);
        }
        return new Parties(result);
    }

    public double getPercent(final Element root, final String abbreviation)
    {
        final double percent;
        final Nodes elected = root.getFirstChildElement("NATION").query("GILTIGA[@PARTI='" + abbreviation + "']");
        if(elected.size() == 0)
        {
            percent = 0;
        }
        else if(elected.size() == 1 && elected.get(0) instanceof Element)
        {
            try
            {
                final Element element = (Element)elected.get(0);
                final Number number = SWEDISH_NUMBER_FORMAT.parse(element.getAttributeValue("PROCENT"));
                percent = number.doubleValue();
            }
            catch(final ParseException e)
            {
                throw new AssertionError("Failed to parse percent: " + e.getMessage());
            }
        }
        else
        {
            throw new IllegalStateException("Document appears to be invalid, got multiple elements for: " + abbreviation);
        }
        return percent;
    }
}