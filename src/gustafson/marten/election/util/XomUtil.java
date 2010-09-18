package gustafson.marten.election.util;

import java.util.Iterator;

import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;

public final class XomUtil
{
    public static Iterable<Element> iterable(final Elements elements)
    {
        return new Iterable<Element>()
        {
            @Override
            public Iterator<Element> iterator()
            {
                return new Iterator<Element>()
                {
                    private int current = 0;

                    @Override
                    public boolean hasNext()
                    {
                        return elements.size() > this.current;
                    }

                    @Override
                    public Element next()
                    {
                        return elements.get(this.current++);
                    }

                    @Override
                    public void remove()
                    {
                        elements.get(this.current).detach();
                    }
                };
            }
        };
    }

    public static Iterable<Node> iterable(final Nodes nodes)
    {
        return new Iterable<Node>()
        {
            @Override
            public Iterator<Node> iterator()
            {
                return new Iterator<Node>()
                {
                    private int current = 0;

                    @Override
                    public boolean hasNext()
                    {
                        return nodes.size() > this.current;
                    }

                    @Override
                    public Node next()
                    {
                        return nodes.get(this.current++);
                    }

                    @Override
                    public void remove()
                    {
                        nodes.get(this.current).detach();
                    }
                };
            }
        };
    }
}
