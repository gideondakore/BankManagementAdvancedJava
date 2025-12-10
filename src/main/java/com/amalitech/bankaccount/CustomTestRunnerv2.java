package com.amalitech.bankaccount;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CustomTestRunnerv2 {

    private CustomTestRunnerv2(){}

    public static void runAllTests(){
        IO.println("\n" + "=".repeat(50));
        IO.println("  RUNNING TEST SUITE");
        IO.println("=".repeat(50));
        IO.println();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "test");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                IO.println(line);
            }

            int exitCode = process.waitFor();
            IO.println();
            IO.println("=".repeat(50));
            if (exitCode == 0) {
                IO.println("  ✓ All tests completed successfully!");
            } else {
                IO.println("  ✗ Some tests failed. Check output above.");
            }
            IO.println("=".repeat(50));

        }catch(InterruptedException _){

            IO.println("Thread was interrupted");
            Thread.currentThread().interrupt();

        } catch (Exception e) {
            IO.println("Error running tests: " + e.getMessage());
            IO.println();
            IO.println("To run tests manually, execute:");
            IO.println("  mvn test");
        }
    }



}


