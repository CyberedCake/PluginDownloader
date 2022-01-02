package net.cybercake.plugindownloader;

import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.Log;
import net.cybercake.cyberapi.Time;
import net.cybercake.cyberapi.instances.Spigot;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Main extends Spigot {

    @Override
    public void onEnable() {
        CyberAPI.initSpigot(this, false);

        Log.info("Loading plugin (PluginDownloader)");
        Log.info(ChatColor.YELLOW + "Checking for updates with plugins specified, please wait...");

        // generate configuration file for user
        saveDefaultConfig();
        reloadConfig();

        // if baseDirectory in config is not set (config generation issue?)
        if(getConfig().getString("baseDirectory") == null) {
            disablePlugin("Cannot check for updates because the main configuration file 'baseDirectory' is not found!\n" +
                    "Try checking the GitHub for new releases or resetting the plugin.");
            return;
        }

        File baseDirectory = new File(getConfig().getString("baseDirectory"));
        File pluginsDirectory = getDataFolder().getParentFile();

        // check to make sure base directory actually has files to loop through
        File[] files = baseDirectory.listFiles();
        if(files == null) {
            disablePlugin("The given baseDirectory has no files to get from!"); return;
        }

        // list of files in plugins directory for server
        ArrayList<File> plugins = new ArrayList<>(List.of(getDataFolder().getParentFile().listFiles()));

        Log.info("Starting update process! (copying to: " + pluginsDirectory.getAbsolutePath() + ") (copying from: " + baseDirectory.getAbsolutePath() + ")");

        short index = 0;
        for(File file : files) {
            // check if 'onlyCopyJarFiles' is false OR if 'onlyCopyJarFiles' is true AND file is a jar file
            if((!getConfig().getBoolean("onlyCopyJarFiles")) ||
                    (getConfig().getBoolean("onlyCopyJarFiles") && file.getName().endsWith(".jar"))) {

                for(File plFile : pluginsDirectory.listFiles()) {
                    //
                    //
                    //
                    //
                    // NEED TO DO
                    //
                    //
                    //
                    //
                }

                try {
                    if(!file.isDirectory()) FileUtils.copyFile(file, new File(pluginsDirectory + File.separator + file.getName().substring(0, file.getName().length()-4) + "-auto.jar"));
                    if(file.isDirectory()) {
                        File dir = new File(pluginsDirectory + File.separator + file.getName());
                        FileUtils.copyDirectory(file, dir);

                        File newFile = new File(dir.getAbsolutePath() + File.separator + "Updated by " + getDescription().getName() + " (DO NOT REMOVE)");
                        newFile.createNewFile();
                        FileWriter writer = new FileWriter(newFile);
                        writer.write("(lastmodified=" + Time.getUnix() + ",id=" + UUID.randomUUID() + ")");
                        writer.close();
                    }

                    Log.info(
                            ChatColor.GOLD + "Updated " + file.getName() + " to new file! " +
                                    ChatColor.RED + "[Old Last Modified: " + plugins.get(index).lastModified() + "] " +
                                    ChatColor.GREEN + "[New Last Modified: " + file.lastModified() + "]"
                    );
                } catch (IOException exception) {
                    Log.error("An error occurred whilst updating " + file.getName() + ": " + exception);
                }
            }
            index++;
        }

        Log.info("The plugin has finished loading! [this message is temp... please report this if you see it!]");
    }

    @Override
    public void onDisable() {
    }

    public void disablePlugin(String why) {
        Log.error("<><><><><><><><><><><><><><><><><><><><><><><><>");
        for(String str : why.split("\n")) {
            Log.error(str);
        }
        Log.error("The plugin will now disable!");
        Log.error("<><><><><><><><><><><><><><><><><><><><><><><><>");
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
