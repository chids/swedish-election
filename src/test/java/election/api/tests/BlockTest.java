package election.api.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import election.api.model.Block;

import org.junit.Test;

public class BlockTest
{

    @Test
    public void valueOfWorksForDefinedFormat()
    {
        final Block block = Block.valueOf("Cool and the gang:A,B,C,D");
        assertEquals("Cool and the gang", block.getName());
        assertArrayEquals(new String[] { "A", "B", "C", "D" }, block.getMembers());
    }

    @Test
    public void valueOfGracefullyHandlesEmptyDelimsInMembers()
    {
        final Block block = Block.valueOf("Cool and the gang:A,,B,C,,");
        assertEquals("Cool and the gang", block.getName());
        assertArrayEquals(new String[] { "A", "B", "C" }, block.getMembers());
    }

}
