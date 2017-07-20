package me.hugmanrique.pokedatatest;

import lombok.Getter;
import me.hugmanrique.pokedata.roms.ROMLoader;
import me.hugmanrique.pokedata.roms.ReadableROM;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author Hugmanrique
 * @since 28/05/2017
 */
public class FileLoader {
    public static final File JAR_FOLDER = getJarFolder();

    public static ReadableROM load() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Don't exit, we can still use Java's UI
            e.printStackTrace();
        }

        FileFilter filter = new FileNameExtensionFilter("GBA ROM", "gba", "bin");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(JAR_FOLDER);

        Action details = fileChooser.getActionMap().get("viewTypeDetails");
        details.actionPerformed(null);

        if (fileChooser.showOpenDialog(new Frame()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File location = fileChooser.getSelectedFile();

        if (location == null) {
            return null;
        }

        ReadableROM rom = new ReadableROM();

        try {
            ROMLoader.load(rom, location);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return rom;
    }

    private static File getJarFolder() {
        ProtectionDomain domain = FileLoader.class.getProtectionDomain();
        CodeSource source = domain.getCodeSource();

        return new File(source.getLocation().getPath());
    }
}
