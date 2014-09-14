package election.api;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;
import static java.lang.System.getenv;
import static java.util.stream.Collectors.joining;

import election.api.model.Parties;
import election.api.model.PercentComparator;
import election.api.util.XmlToPartyMapper;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.hash.Hashing;
import com.sun.jersey.api.client.Client;

public class Mainer {

    private static final String FILE = "valnatt_00R.xml";
    private static final int BUFFER_SIZE = 500;
    private String checksum = "b0d7aa0df7514c18814a5136298e8b3d3d135a6f"; // Empty 2014 hash

    public static void main(final String[] args) throws InterruptedException {
        final Mainer robo = new Mainer();
        while(true) {
            try {
                robo.zap();
            }
            catch(final Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(TimeUnit.MINUTES.toMillis(1));
        }
    }

    public void zap() throws IOException, ValidityException, ParsingException {
        try(InputStream stream = Client.create()
                .resource("http://www.val.se/val/val2014/valnatt/valnatt.zip")
                .get(InputStream.class)) {
            final byte[] xml = parseResponse(stream);
            final String checksum = Hashing.sha1().hashBytes(xml).toString();
            if(this.checksum.equals(checksum)) {
                System.err.println("Checksum unchanged, no lol.");
                return;
            }
            System.err.println("Checksum CHANGED, LOL'ing it!");
            this.checksum = checksum;
            final InputStream source = new ByteArrayInputStream(xml);
            final Document document = new nu.xom.Builder(true).build(source).getDocument();
            final Parties parties = XmlToPartyMapper.parseElectedParties(document);
            if(StreamSupport
                    .stream(parties.spliterator(), true)
                    .filter((party) -> party.getPercent() > 0)
                    .count() > 0) {
                final String tweet = StreamSupport
                        .stream(parties.sort(PercentComparator.Descending).spliterator(), true)
                        .map((party) -> party.getAbbreviation() + ": " + party.getPercent())
                        .collect(joining(" "));
                tweet(tweet);
            }
        }
    }

    public void tweet(final String tweet) {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(env("TWITTER_KEY"))
                .setOAuthConsumerSecret(env("TWITTER_SECRET"))
                .setOAuthAccessToken(env("TWITTER_ACCESS_TOKEN"))
                .setOAuthAccessTokenSecret(env("TWITTER_ACCESS_SECRET"));
        final TwitterFactory tf = new TwitterFactory(cb.build());
        final Twitter twitter = tf.getInstance();
        try {
            twitter.updateStatus(tweet);
        }
        catch(final TwitterException e) {
            e.printStackTrace();
        }
    }

    private static String env(final String name) {
        return checkNotNull(emptyToNull(getenv(name)), "No value for " + name);
    }

    public byte[] parseResponse(final InputStream stream) throws IOException {
        try(ZipInputStream zip = new ZipInputStream(stream)) {
            ZipEntry entry;
            while((entry = zip.getNextEntry()) != null) {
                final String name = entry.getName();
                if(FILE.equals(name)) {
                    final ByteArrayOutputStream target = new ByteArrayOutputStream(BUFFER_SIZE);
                    try(BufferedOutputStream bos = new BufferedOutputStream(target, BUFFER_SIZE)) {
                        final byte[] buffer = new byte[BUFFER_SIZE];
                        int size;
                        while((size = zip.read(buffer, 0, buffer.length)) != -1) {
                            bos.write(buffer, 0, size);
                        }
                    }
                    zip.closeEntry();
                    return target.toByteArray();
                }
            }
        }
        throw new FileNotFoundException(" wasn't present in archive");
    }
}
