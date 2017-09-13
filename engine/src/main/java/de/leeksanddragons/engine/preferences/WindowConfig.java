package de.leeksanddragons.engine.preferences;

import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 13.09.2017.
 */
public class WindowConfig {

    public static final String CONFIG_FILE_NAME = "window.cfg";

    //ini configuration file
    protected Ini ini = null;

    //[Window] section
    protected Profile.Section section = null;

    public WindowConfig (String prefDir) {
        //create config directory if neccessary
        if (!new File(prefDir).exists()) {
            new File(prefDir).mkdirs();
        }

        try {
            this.load(prefDir + CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected void createDefaultConfig(String cfgFile) throws IOException {
        //create new instance of ini
        this.ini = new Ini();

        //add section [Window]
        this.section = ini.add("Window");

        //set default values
        this.section.put("prefWidth", 800);
        this.section.put("prefHeight", 600);
        this.section.put("resizeable", true);
        this.section.put("fullscreen", true);

        //save config
        this.ini.store(new File(cfgFile));
    }

    protected void load (String cfgFile) throws IOException {
        File file = new File(cfgFile);

        //check, if file exists
        if (!file.exists()) {
            System.out.println("window configuration doesnt exists, createDefaultConfig new config now: " + cfgFile);

            //create default config
            this.createDefaultConfig(cfgFile);

            return;
        }

        //load ini file
        this.ini = new Ini(new File(cfgFile));

        //load section
        this.section = this.ini.get("Window");
    }

    public int getWidth () {
        return this.section.get("prefWidth", Integer.class, 800);
    }

    public int getHeight () {
        return this.section.get("prefHeight", Integer.class, 800);
    }

    public boolean isResizeable () {
        return this.section.get("resizeable", Boolean.class, true);
    }

    public boolean isFullscreen () {
        return this.section.get("fullscreen", Boolean.class, false);
    }

}
