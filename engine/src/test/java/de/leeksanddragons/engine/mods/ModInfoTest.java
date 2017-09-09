package de.leeksanddragons.engine.mods;

import de.leeksanddragons.engine.exception.InvalideModJSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Justin on 09.09.2017.
 */
public class ModInfoTest {

    @Test (expected = FileNotFoundException.class)
    public void testDirectoryNotExists () throws IOException, InvalideModJSONException {
        ModInfo.create(new File("../test-dir").getAbsolutePath());
    }

    @Test (expected = FileNotFoundException.class)
    public void testFile () throws IOException, InvalideModJSONException {
        ModInfo.create(new File("../junit-tests/test-file.txt").getAbsolutePath());
    }

    @Test (expected = FileNotFoundException.class)
    public void testNotAnModDir () throws IOException, InvalideModJSONException {
        ModInfo.create(new File("../data").getAbsolutePath());
    }

    @Test (expected = InvalideModJSONException.class)
    public void testInvalideModJSON () throws IOException, InvalideModJSONException {
        ModInfo mod = ModInfo.create("../junit-tests/mods/invalide-mod");
    }

    @Test
    public void testCreate () throws IOException, InvalideModJSONException {
        ModInfo mod = ModInfo.create("../junit-tests/mods/mod1");
    }

}
