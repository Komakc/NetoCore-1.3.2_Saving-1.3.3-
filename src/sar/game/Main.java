package sar.game;

import java.io.IOException;
import static java.lang.Thread.sleep;
import static sar.game.GameProgress.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String rootDirectory = "/home/maksim/Документы/Курс Java/Games/savegames/";

        GameProgress gameProgress1 = new GameProgress(100, 100, 1, 2.5);
        GameProgress gameProgress2 = new GameProgress(80, 50, 4, 10.35);
        GameProgress gameProgress3 = new GameProgress(20, 5, 8, 28.57);

        gameProgress1.saving(rootDirectory);
        sleep(1000);
        gameProgress2.saving(rootDirectory);
        sleep(1000);
        gameProgress3.saving(rootDirectory);

        archiving(rootDirectory);

        String zipFile = "zip_output.zip";
        openZip(zipFile, rootDirectory);

        GameProgress gameProgressOut = openProgress(rootDirectory);
        System.out.println(gameProgressOut);
    }
}
