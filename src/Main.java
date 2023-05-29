import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static String gameDirectory = "D://GamesNET";

    public static String[] dirs = {
            "src", "res", "savegames", "temp",
            "src//main", "src//test",
            "res//drawables", "res//vectors", "res//icons"};
    public static String[] files = {"src//main//Main.java", "src//main//Utils.java"};
    public static String tempFile = "temp//temp.txt";
    public static StringBuilder logMessage = new StringBuilder();

    public static void main(String[] args) {
        prepareToInstall();

        GameProgress game1 = new GameProgress(100, 15, 23, 60);
        GameProgress game2 = new GameProgress(120, 25, 26, 45);
        GameProgress game3 = new GameProgress(80, 10, 18, 90);
        saveGame("save01", game1);
        saveGame("save02", game2);
        saveGame("save03", game3);
        zipFiles("zip.zip", Arrays.asList("save01", "save02", "save03"));
        openZip("zip.zip", Paths.get(gameDirectory, "savegames").toString());
        System.out.println(openProgress(
                Paths.get(gameDirectory, "savegames", "save01").toString()));
    }

    private static GameProgress openProgress(String fileName) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

    private static void openZip(String zipFile, String dstDir) {
        try (ZipInputStream zin = new ZipInputStream(
                Files.newInputStream(Paths.get(gameDirectory, "savegames", zipFile)))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(
                        Paths.get(dstDir, name).toString());
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles(String fileName, List<String> listFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(
                Files.newOutputStream(Paths.get(gameDirectory, "savegames", fileName)))) {
            for (String file : listFiles) {
                Path path = Paths.get(gameDirectory, "savegames", file);
                zout.putNextEntry(new ZipEntry(path.getFileName().toString()));
                Files.copy(path, zout);
                zout.closeEntry();
                Files.delete(path);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void saveGame(String fileName, GameProgress game) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(
                             Files.newOutputStream(Paths.get(gameDirectory, "savegames", fileName)))) {
            oos.writeObject(game);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepareToInstall() {
        createMainDir();
        createSubDirs();
        createFiles();
        writeToLog(logMessage.toString());
    }

    private static void createFiles() {
        for (String file : files) {
            try {
                Path path = Paths.get(gameDirectory, file);
                Files.write(path, "".getBytes());
                logMessage.append("Создан файл: " + path.toAbsolutePath() + "\n");
            } catch (IOException e) {
                logMessage.append("Ошибка создания файла: " + file + "\n");
            }
        }
    }

    private static void createSubDirs() {
        for (String dir : dirs) {
            try {
                Path path = Paths.get(gameDirectory, dir);
                Files.createDirectories(path);
                logMessage.append("Создана папка: " + path.toAbsolutePath() + "\n");
            } catch (IOException e) {
                logMessage.append("Ошибка создания папки: " + dir + "\n");
            }
        }
    }

    private static void createMainDir() {
        try {
            Path path = Paths.get(gameDirectory);
            Files.createDirectories(path);
            logMessage.append("Создана папка: " + path.toAbsolutePath() + "\n");
        } catch (IOException e) {
            logMessage.append("Ошибка создания папки: " + gameDirectory + "\n");
        }
    }


    static void writeToLog(String message) {
        Path path = Paths.get(gameDirectory, tempFile);
        try {
            Files.write(path, message.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}