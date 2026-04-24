package io.github.sagaraggarwal86.jmeter.jauditor.rules.observability;

import io.github.sagaraggarwal86.jmeter.jauditor.engine.ScanContext;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Category;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Finding;
import io.github.sagaraggarwal86.jmeter.jauditor.model.Severity;
import io.github.sagaraggarwal86.jmeter.jauditor.rules.AbstractRule;
import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.testelement.TestElement;

import java.util.List;
import java.util.Set;

public final class TransactionParentSampleRule extends AbstractRule {
    @Override
    public String id() {
        return "TRANSACTION_PARENT_SAMPLE";
    }

    @Override
    public Category category() {
        return Category.OBSERVABILITY;
    }

    @Override
    public Severity severity() {
        return Severity.INFO;
    }

    @Override
    public String description() {
        return "Transaction Controller without 'Generate Parent Sample' enabled.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(TransactionController.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        boolean parent = propBool(node.getTestElement(), "TransactionController.parent");
        if (parent) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Transaction Controller not generating parent sample",
                "This Transaction Controller has 'Generate Parent Sample' turned off. That means the controller's total duration (first sampler start to last sampler end) doesn't get its own row in reports — only the individual child samplers do. You can still see the pieces, but you can't easily answer 'how long did the full checkout take?' without manually summing child rows, and parallel samples don't add the way sequential ones do anyway.",
                "Open the Transaction Controller and check the 'Generate parent sample' box. After the change, the controller appears as a single aggregated row in summary reports, alongside the child sampler rows. If you want the total to include time spent in timers and pre-/post-processors as well, also enable 'Include duration of timer and pre-post processors in generated sample'. For most load tests, switching the parent sample on gives clean per-flow metrics without losing the detailed per-sampler view."));
    }
}
