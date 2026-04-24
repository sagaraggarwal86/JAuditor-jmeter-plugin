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

public final class UnnamedTransactionControllerRule extends AbstractRule {
    @Override
    public String id() {
        return "UNNAMED_TRANSACTION_CONTROLLER";
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
        return "Transaction Controller left with its default name.";
    }

    @Override
    public Set<Class<? extends TestElement>> appliesTo() {
        return Set.of(TransactionController.class);
    }

    @Override
    public List<Finding> check(JMeterTreeNode node, ScanContext ctx) {
        String name = node.getName();
        if (name == null) return List.of();
        if (!"Transaction Controller".equals(name.trim())) return List.of();
        return List.of(make(ctx.pathFor(node),
                "Transaction Controller unnamed",
                "This Transaction Controller is still named 'Transaction Controller' — the default. In aggregated results the controller appears as a row with exactly that label, and if the test has more than one such controller (which is common), every row reads 'Transaction Controller' with no way to tell them apart. The grouped metrics the controller is there to produce become unreadable.",
                "Rename the controller to describe the business flow it wraps: 'Checkout Flow', 'User Registration', 'Search And Filter'. The name shows up verbatim in every report, so pick something that reads naturally when a stakeholder skims the summary. If several controllers represent variants of the same flow (e.g., guest vs logged-in checkout), include the variant in the name so they sort together and stay distinguishable."));
    }
}
