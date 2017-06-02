package me.hugmanrique.pokedatatest;

import me.hugmanrique.pokedata.attacks.Attack;
import me.hugmanrique.pokedata.graphics.ROMImage;
import me.hugmanrique.pokedata.items.Item;
import me.hugmanrique.pokedata.loaders.ROMData;
import me.hugmanrique.pokedata.pokedex.Pokedex;
import me.hugmanrique.pokedata.pokedex.PokemonBaseStats;
import me.hugmanrique.pokedata.pokedex.ev.Evolution;
import me.hugmanrique.pokedata.pokedex.ev.EvolutionData;
import me.hugmanrique.pokedata.roms.ReadableROM;
import me.hugmanrique.pokedata.utils.HeaderNames;
import me.hugmanrique.pokedata.utils.PokeText;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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

    private ReadableROM rom;
    private ROMData data;

    private PokeDataTest(String[] args) {
        HeaderNames.updateNames();

        try {
            PokeText.loadFromJar();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }


        // TODO implement direct file loading through args

        rom = FileLoader.load();

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

        try {
            data = new ROMData(rom);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        performTests();

        System.exit(0);
    }

    // Perform tests and data visualization here
    private void performTests() {
        savePokeImages();
    }

    private void printPokedex() {
        for (int i = 0; i < data.getDexEntriesNumber(); i++) {
            Pokedex pokedex = Pokedex.load(rom, data, i);

            System.out.println(pokedex);
            System.out.println(pokedex.getDesc1(rom));
        }
    }

    private void printItems() {
        for (int i = 0; i < data.getItemsNumber(); i++) {
            Item item = Item.load(rom, data, i);

            System.out.println(item);
            System.out.println(item.getDescription(rom));
        }
    }

    private void printMoves() {
        for (int i = 0; i < data.getAttacksNumber(); i++) {
            Attack attack = Attack.load(rom, data, i);

            System.out.println(attack);
        }
    }

    private void printBaseStats() {
        for (int i = 0; i < data.getPokemonsNumber(); i++) {
            PokemonBaseStats stats = PokemonBaseStats.load(rom, data, i);

            System.out.println(stats);
        }
    }

    private void printEvolutionData() {
        for (int i = 0; i < data.getPokemonsNumber(); i++) {
            EvolutionData evolutionData = EvolutionData.load(rom, data, i);
            Evolution[] evolutions = evolutionData.getEvolutions();

            for (int k = 0; k < evolutions.length; k++) {
                System.out.println(evolutions[k]);
            }

            System.out.println();
        }
    }

    private void saveItemImage() {
        // Good rod
        Item item = Item.load(rom, data, 263);

        ROMImage image = item.getImage(rom, data);

        BufferedImage bufferedImage = image.toBufferedImage();
        saveImage(bufferedImage, "item");
    }

    private void savePokeImages() {
        // Bulbasaur
        Pokedex pokedex = Pokedex.load(rom, data, 1);

        ROMImage frontNormal = pokedex.getFrontImage(rom, data, false);
        ROMImage frontShiny = pokedex.getFrontImage(rom, data, true);
        ROMImage backNormal = pokedex.getBackImage(rom, data, false);
        ROMImage backShiny = pokedex.getBackImage(rom, data, true);

        saveImage(frontNormal.toBufferedImage(), "frontNormal");
        saveImage(frontShiny.toBufferedImage(), "frontShiny");
        saveImage(backNormal.toBufferedImage(), "backNormal");
        saveImage(backShiny.toBufferedImage(), "backShiny");
    }

    private void saveImage(BufferedImage image, String filename) {
        File file = new File(FileLoader.JAR_FOLDER, filename + ".png");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
