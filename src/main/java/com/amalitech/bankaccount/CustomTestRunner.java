package com.amalitech.bankaccount;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class CustomTestRunner {

    public static void runAllTestsInPackage() {
        IO.println("Scanning for tests...\n");

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("com.amalitech.bankaccount"))
                .build();

        executeTests(request);
    }

    private static void executeTests(LauncherDiscoveryRequest request) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        TestExecutionListener listener1 = new TestExecutionListener() {
            @Override
            public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult){
                if(testIdentifier.isTest()){
                    String testName = testIdentifier.getDisplayName();

                    IO.println("TEST: " + testName);

                    int padding = 50 - testName.length();

                    for(int i = 0; i < padding; i++){
                        IO.print(".");
                    }

                    if(testExecutionResult.getStatus() == TestExecutionResult.Status.SUCCESSFUL){
                        IO.println("PASSED");
                    }else{
                        IO.println(("FAILED"));
                        testExecutionResult.getThrowable().ifPresent(t -> IO.println("  Error: " + t.getMessage()));
                    }

                }
            }
        };

        launcher.registerTestExecutionListeners(listener, listener1);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        IO.println("\n========== Test Results ==========");
        IO.println("Tests found: " + summary.getTestsFoundCount());
        IO.println("Tests started: " + summary.getTestsStartedCount());
        IO.println("Tests successful: " + summary.getTestsSucceededCount());
        IO.println("Tests failed: " + summary.getTestsFailedCount());
        IO.println("Tests skipped: " + summary.getTestsSkippedCount());
        IO.println("==================================\n");

        // Print failures if any
        if (!summary.getFailures().isEmpty()) {
            IO.println("Failed tests:");
            summary.getFailures().forEach(failure -> {
                IO.println("  - " + failure.getTestIdentifier().getDisplayName());
                IO.println("    " + failure.getException());
            });
        }
    }
}