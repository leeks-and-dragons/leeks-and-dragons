package de.leeksanddragons.engine.mods;

import de.leeksanddragons.engine.exception.InvalideModJSONException;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

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

        //test attributes
        assertEquals("mod name isnt 'mod1'.", "mod1", mod.getName());
        assertEquals("title isnt the same.", "Leeks & Dragons - Mod 1", mod.getTitle());
        assertEquals("mod version isnt equals.", "1.0.0", mod.getVersion());
        assertEquals("mod version number", 1, mod.getVersionNumber());
        assertEquals("author isnt equals.", "Leeks & Dragons Team", mod.getAuthor());
        assertEquals("credits exists, but wasnt detected.", true, mod.hasCredits());
        assertEquals("credits arent equals.", "CONTRIBUTORS.md", mod.getCreditsFile());
        assertEquals("isLoadable() isnt equals.", true, mod.isLoadable());
        assertEquals("min engine version isnt equals.", "1.0.0", mod.getMinEngineVersion());
        assertEquals("min engine version number isnt equals.", 1, mod.getMinEngineVersionNumber());
        assertArrayEquals("supported languages arent equals.", new String[] {"de", "en"}, mod.getSupportedLanguages());
        assertEquals("load_priority isnt 10.", 10, mod.getLoadPriority());
    }

}
