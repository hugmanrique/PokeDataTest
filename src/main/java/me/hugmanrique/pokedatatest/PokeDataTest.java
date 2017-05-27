package me.hugmanrique.pokedatatest;

import me.hugmanrique.pokedata.utils.HeaderNames;
import me.hugmanrique.pokedata.utils.ROM;
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

        // TODO implement direct file loading through args

        fileLoader();
    }

    private BaseROM fileLoader() {
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
