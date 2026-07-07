package utils;

public class DatabaseServiceManager {

    // Change this to match your exact Windows service name
    private static final String SERVICE_NAME = "MySQL80";

    public static void ensureRunning() {
        if (!isServiceRunning()) {
            startService();
        }
    }

    private static boolean isServiceRunning() {
        try {
            ProcessBuilder builder = new ProcessBuilder("sc", "query", SERVICE_NAME);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );

            String line;
            boolean running = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains("RUNNING")) {
                    running = true;
                }
            }

            process.waitFor();
            return running;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void startService() {
        try {
            ProcessBuilder builder = new ProcessBuilder("net", "start", SERVICE_NAME);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[DB Service] " + line);
            }

            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}