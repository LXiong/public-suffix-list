package de.malkusch.whoisServerList.publicSuffixList.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.malkusch.whoisServerList.publicSuffixList.index.Index;
import de.malkusch.whoisServerList.publicSuffixList.index.IndexFactory;
import de.malkusch.whoisServerList.publicSuffixList.rule.Rule;
import de.malkusch.whoisServerList.publicSuffixList.test.util.TestUtil;

@RunWith(Parameterized.class)
public class IndexTest {

    @Parameter
    public IndexFactory factory;

    @Parameters(name = "{0}")
    public static Collection<IndexFactory[]> getIndexes() {
        return TestUtil.getTestIndexFactories();
    }

    @Test
    public void testFindRuleFromSubtreeMatch() {
        List<Rule> rules = TestUtil.convertRules(
                 "b.c.d",
                 "a.*.d");
        Index index = factory.build(rules);

        assertEquals("b.c.d", index.findRule("b.c.d").getPattern());
        assertEquals("a.*.d", index.findRule("a.c.d").getPattern());
    }

    @Test
    public void testFindRule() {
        List<Rule> rules = TestUtil.convertRules(
                "net",
                "com",
                "a.com",
                "a.org",
                "org",
                "*.ck",
                "!www.ck");
        Index index = factory.build(rules);

        assertNull(index.findRule(null));
        assertNull(index.findRule(""));
        assertNull(index.findRule("invalid"));
        assertNull(index.findRule("x.invalid"));

        assertEquals("net", index.findRule("net").getPattern());
        assertEquals("net", index.findRule("x.net").getPattern());
        assertEquals("net", index.findRule("x.x.net").getPattern());
        assertEquals("com", index.findRule("x.com").getPattern());
        assertEquals("com", index.findRule("com").getPattern());
        assertEquals("com", index.findRule("x.com").getPattern());
        assertEquals("a.com", index.findRule("a.com").getPattern());
        assertEquals("a.com", index.findRule("x.a.com").getPattern());
        assertEquals("*.ck", index.findRule("a.ck").getPattern());
        assertEquals("*.ck", index.findRule("x.a.ck").getPattern());
        assertEquals("www.ck", index.findRule("www.ck").getPattern());
        assertEquals("www.ck", index.findRule("x.www.ck").getPattern());
    }

}
