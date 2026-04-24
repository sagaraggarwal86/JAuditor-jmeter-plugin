package io.github.sagaraggarwal86.jmeter.jauditor.rules.realism;

import io.github.sagaraggarwal86.jmeter.jauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.protocol.http.control.CookieManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;

import java.util.List;
import java.util.Set;

public final class MissingCookieManagerRule extends AbstractRule {
    @Override
    public String id() {
        return "MISSING_COOKIE_MANAGER";
    }

    @Override
    public Category category() {
        return Category.REALISM;
    }

    @Override
    public Severity severity() {
        return Severity.INFO;
    }

    @Override
    public String description() {
        return "Test plan has HTTP samplers but no HTTP Cookie Manager.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(TestPlan.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        boolean hasHttp = ctx.memoize("anyHttpSampler",
                () -> allNodes(ctx.tree()).stream().anyMatch(n -> n.getTestElement() instanceof HTTPSamplerBase));
        if (!hasHttp) return List.of();
        boolean hasCookie = ctx.memoize("anyCookieManager",
                () -> allNodes(ctx.tree()).stream().anyMatch(n -> n.getTestElement() instanceof CookieManager));
        if (hasCookie) return List.of();
        return List.of(make(ctx.pathFor(node),
                "No HTTP Cookie Manager",
                "The test plan makes HTTP requests but has no HTTP Cookie Manager anywhere in the tree. That means JMeter doesn't store cookies between requests — every sampler acts like a brand-new browser that's never been to the site before. If the application relies on session cookies for login, shopping carts, CSRF tokens, or sticky load-balancer routing, the test isn't actually exercising real user flows; it's exercising a series of unauthenticated first-visits.",
                "Add an HTTP Cookie Manager element to the test tree. Putting it directly under the Test Plan makes it apply to every Thread Group; putting it inside a specific Thread Group scopes it to that group only. The default settings (clear cookies each iteration = true, CookieManager.save.cookies = false) work for most cases — JMeter will accept, store, and replay cookies across a single thread's iterations, which is how a real browser behaves."));
    }
}
