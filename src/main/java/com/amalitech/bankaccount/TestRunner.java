package com.amalitech.bankaccount;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class TestRunner {

    public static void runAllTestsInPackage() {
        System.out.println("Scanning for tests...\n");

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                // Scan from the root package to find all sub-packages
                .selectors(selectPackage("com.amalitech.bankaccount"))
                .filters(includeClassNamePatterns(".*Test"))
                .build();

        executeTests(request);
    }

    private static void executeTests(LauncherDiscoveryRequest request) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        System.out.println("\n========== Test Results ==========");
        System.out.println("Tests found: " + summary.getTestsFoundCount());
        System.out.println("Tests started: " + summary.getTestsStartedCount());
        System.out.println("Tests successful: " + summary.getTestsSucceededCount());
        System.out.println("Tests failed: " + summary.getTestsFailedCount());
        System.out.println("Tests skipped: " + summary.getTestsSkippedCount());
        System.out.println("==================================\n");

        // Print failures if any
        if (summary.getFailures().size() > 0) {
            System.out.println("Failed tests:");
            summary.getFailures().forEach(failure -> {
                System.out.println("  - " + failure.getTestIdentifier().getDisplayName());
                System.out.println("    " + failure.getException());
            });
        }
    }
}