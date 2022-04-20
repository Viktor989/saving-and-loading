package ViktorGolikov;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args){
//       
        // сериализация
        GameProgress[] gameProgress = new GameProgress[3];
        gameProgress[0] = new GameProgress(100, 5, 2, 3);
        gameProgress[1] = new GameProgress(85, 4, 4, 5);
        gameProgress[2] = new GameProgress(62, 2, 7, 11);
        for (int i = 0; i < 3; i++) {
            try (FileOutputStream saveFos = new FileOutputStream("c://Games/savegames/save" + i + ".dat");
                 ObjectOutputStream saveOos = new ObjectOutputStream(saveFos)) {
                saveOos.writeObject(gameProgress[i]);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        // архивирование файлов
        String zipFile = "c://Games/savegames/save.zip";
        String[] saveFiles = {"c://Games/savegames/save0.dat", "c://Games/savegames/save1.dat", "c://Games/savegames/save2.dat"};
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (int i = 0; i < saveFiles.length; i++) {
                File srcFile = new File(saveFiles[i]);
                ZipEntry entry = new ZipEntry(srcFile.getName());
                zos.putNextEntry(entry);
                zos.write(Files.readAllBytes(srcFile.toPath()));
                zos.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        try {
            for (int i = 0; i < saveFiles.length; i++) {
                Files.delete(Path.of(saveFiles[i]));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
//разархивируем архив zip
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            String name;
            long size;
            while ((entry = zin.getNextEntry()) != null) {

                name = entry.getName(); // получим название файла
                size = entry.getSize();  // получим его размер в байтах
                System.out.printf("File name: %s \t File size: %d \n", name, size);

                // распаковка
                FileOutputStream fout = new FileOutputStream(zipFile + name);
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
        // чтение сериализованных файлов
        GameProgress gameProgress1 = null;
        for (int i = 0; i < 3; i++) {

            try (FileInputStream saveFis = new FileInputStream("c://Games/savegames/save.zipsave" + i + ".dat");
                 ObjectInputStream saveOis = new ObjectInputStream(saveFis)) {
                gameProgress1 = (GameProgress) saveOis.readObject();
                System.out.println(gameProgress1.toString());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

