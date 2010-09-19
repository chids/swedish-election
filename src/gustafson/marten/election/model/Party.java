package gustafson.marten.election.model;


public final class Party
{
    /*
    private static final Charset isoLatin = Charset.forName("ISO-8859-1");
    private static final CharsetEncoder isoLatinEncoder = isoLatin.newEncoder();
    private static final CharsetDecoder isoLatinDecoder = isoLatin.newDecoder();

    private static final Charset utf8 = Charset.forName("UTF-8");
    private static final CharsetEncoder utf8Encoder = utf8.newEncoder();
    private static final CharsetDecoder utf8Decoder = utf8.newDecoder();

                final ByteArrayOutputStream target = new ByteArrayOutputStream();
                final OutputStreamWriter writer = new OutputStreamWriter(target, utf8Encoder);
                final InputStreamReader inputReader = new InputStreamReader(stream, isoLatinDecoder);
                IOUtils.copy(inputReader, writer);
                writer.flush();
                return new nu.xom.Builder(true).build(new ByteArrayInputStream(target.toByteArray())).getDocument();

     */
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
        return this.abbreviation + '\t' + this.percent + "%\t" + this.name;
    }
}