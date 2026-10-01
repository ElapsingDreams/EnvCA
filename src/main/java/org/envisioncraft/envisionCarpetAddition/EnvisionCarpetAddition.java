package org.envisioncraft.envisionCarpetAddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnvisionCarpetAddition implements CarpetExtension, ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("EnvisionCarpetAddition");
    public static final EnvisionCarpetAddition INSTANCE = new EnvisionCarpetAddition();
    public static SettingsManager settingsManager;

    public static final String MOD_ID = "envision-carpet-addition";
    private static final String MOD_NAME;
    private static final String MOD_VERSION;

    static {
        ModMetadata metadata = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .orElseThrow(RuntimeException::new)
                .getMetadata();
        MOD_NAME = metadata.getName();
        MOD_VERSION = metadata.getVersion().getFriendlyString();
        settingsManager = new SettingsManager(MOD_VERSION, MOD_ID, MOD_NAME);
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public void onInitialize() {
        CarpetServer.manageExtension(INSTANCE);
    }

    @Override
    public SettingsManager extensionSettingsManager() {
        return settingsManager;
    }

    @Override
    public void onGameStarted() {
        LOGGER.info("{} v{} loaded!", MOD_NAME, MOD_VERSION);
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(EnvisionCarpetAdditionSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        InputStream langFile =
                EnvisionCarpetAdditionSettings.class.getClassLoader().getResourceAsStream("assets/" + MOD_ID + "/lang/%s.json".formatted(lang));
        if (langFile == null) {
            // we don't have that language
            return Collections.emptyMap();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Collections.emptyMap();
        }

        // create translation keys for both Carpet and envision-carpet-addition settingsManagers
        Map<String, String> map = new Gson().fromJson(jsonData, new TypeToken<Map<String, String>>() {}.getType());
        Map<String, String> map2 = new HashMap<>();
        map.forEach((key, value) -> {
            map2.put(key, value);
            if (key.startsWith(MOD_ID + ".rule.")) {
                map2.put(key.replace(MOD_ID + ".rule.", "carpet.rule."), value);
            }
        });
        return map2;
    }
}
