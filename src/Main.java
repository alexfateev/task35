import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static File gameDirectory = new File("D://Games");

    public static String[] dirs = {"src", "res", "savegames", "temp"};
    public static String[] srcDir = {"main", "test"};
    public static String[] mainFiles = {"Main.java", "Utils.java"};
    public static String[] resDir = {"drawables", "vectors", "icons"};
    public static File tempFile = new File(new File(gameDirectory, "temp"), "temp.txt");
    public static StringBuilder logMessage = new StringBuilder();

    public static void main(String[] args) {
        createDirs(gameDirectory, dirs);
        createDirs(new File(gameDirectory, "src"), srcDir);
        createFiles(new File(new File(gameDirectory, "src"), "main"), mainFiles);
        createDirs(new File(gameDirectory, "res"), resDir);
        System.out.println(logMessage.toString());
        writeToLog(logMessage.toString());
    }

    static void createDirs(File dir, String[] dirs) {
        for (String d : dirs) {
            if (new File(dir, d).mkdir()) {
                logMessage.append("Create folder: " + d + "\n");
            }
        }
    }

    static void createFiles(File dir, String[] files) {
        for (String file : files) {
            try {
                if (new File(dir, file).createNewFile()) {
                    logMessage.append("Create file: " + file + "\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    static void writeToLog(String message) {
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try (FileWriter fw = new FileWriter(tempFile, false)) {
            fw.write(message);
            fw.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}