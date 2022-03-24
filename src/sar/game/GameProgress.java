package sar.game;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yyyy");

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    //Сериализация
    void saving(String rootDirectory) {
        String dateSaving = DEFAULT_TIME_FORMATTER.format(LocalDateTime.now());
        try (FileOutputStream fos = new FileOutputStream(rootDirectory + "save" + dateSaving + ".dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Архивация
    static void archiving(String rootDirectory) throws IOException {
        File filesDir = new File(rootDirectory);
        String[] fileList = filesDir.list();
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(rootDirectory + "zip_output.zip"));
        for (String file : fileList) {
            String fileName = new File(file).getPath();
            try (FileInputStream fis = new FileInputStream(rootDirectory + fileName)) {
                ZipEntry entry = new ZipEntry(file);
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        zout.close();

        //Удаление
        for (String file : fileList) {
            String fileName = new File(file).getPath();
            try {
                Files.delete(Paths.get(rootDirectory + fileName));
            } catch (IOException x) {
                System.err.println(x.getMessage());
            }
        }
    }

    static void openZip(String zipFile, String directory) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(directory + zipFile))) {
            ZipEntry entry;
            String nameFile;
            while ((entry = zin.getNextEntry()) != null) {
                nameFile = entry.getName();
                FileOutputStream fout = new FileOutputStream(directory + nameFile);
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

    static GameProgress openProgress(String directory) {
        GameProgress gameProgress;
        File fileDir = new File(directory);
        String[] fileList = fileDir.list();
        String fileName = new File(fileList[0]).getPath();
        try (FileInputStream fis = new FileInputStream(directory + fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
            return gameProgress;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}