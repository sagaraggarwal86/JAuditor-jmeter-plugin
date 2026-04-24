package io.github.sagaraggarwal86.jmeter.jauditor.rules.scalability;

import io.github.sagaraggarwal86.jmeter.jauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jauditor.rules.AbstractRule;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.testelement.TestElement;

import java.util.List;
import java.util.Set;

public final class SaveResponseDataEnabledRule extends AbstractRule {
    @Override
    public String id() {
        return "SAVE_RESPONSE_DATA_ENABLED";
    }

    @Override
    public Category category() {
        return Category.SCALABILITY;
    }

    @Override
    public Severity severity() {
        return Severity.WARN;
    }

    @Override
    public String description() {
        return "HTTP sampler configured to save full response data.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(HTTPSamplerBase.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        TestElement te = node.getTestElement();
        boolean save = propBool(te, "HTTPSampler.save_response_as_md5") || propBool(te, "WebServiceSampler.save_response");
        if (!save) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Save Response Data enabled",
                "This HTTP sampler is configured to save the full response body for every request into the results file. On a serious load test at thousands of requests per second, each potentially hundreds of kilobytes in size, the JTL file grows by gigabytes per minute, and JMeter buffers chunks of that in memory along the way. Disk fills up, heap pressure spikes, and the extra I/O slows the actual test down to where the reported response times aren't even representative of the system under test anymore.",
                "Turn off the save-response-data option on the sampler unless you specifically need the body for later inspection. If you only need bodies for failed requests (a reasonable debugging compromise), configure that via the jmeter.save.saveservice.response_data.on_error property in jmeter.properties — JMeter will then save bodies only when a sample fails. For full-body captures, run a targeted smoke test with a handful of iterations rather than saving every response on a 10,000-thread run."));
    }
}
