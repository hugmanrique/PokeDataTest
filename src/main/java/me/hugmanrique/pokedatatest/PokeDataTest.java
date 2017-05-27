package me.hugmanrique.pokedatatest;

import me.hugmanrique.pokedata.utils.HeaderNames;
import me.hugmanrique.pokedata.utils.PokeText;
import me.hugmanrique.pokedata.utils.ROMLoader;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Hugmanrique
 * @since 27/05/2017
 */
public class PokeDataTest {
    public static void main(String[] args) {
        new PokeDataTest(args);
    }

    private PokeDataTest(String[] args) {
        HeaderNames.updateNames();

        try {
            PokeText.loadFromJar();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        // TODO implement direct file loading through args

        BaseROM rom = fileLoader();

        if (rom == null) {
            System.exit(1);
        }

        String print = String.format(
            "Loaded %s - %s created by %s",
            rom.getGameCode(),
            rom.getGameText(),
            rom.getGameCreatorId()
        );

        System.out.println(print);

        // Perform tests and data visualization here
    }

    private BaseROM fileLoader() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Don't exit, we can still use Java's UI
            e.printStackTrace();
        }

        FileFilter filter = new FileNameExtensionFilter("GBA ROM", "gba", "bin");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);

        Action details = fileChooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        if (fileChooser.showOpenDialog(new Frame()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File location = fileChooser.getSelectedFile();

        if (location == null) {
            return null;
        }

        BaseROM rom = new BaseROM();

        try {
            ROMLoader.load(rom, location);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rom;
    }
}
