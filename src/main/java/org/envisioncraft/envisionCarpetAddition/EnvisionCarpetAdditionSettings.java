package org.envisioncraft.envisionCarpetAddition;

import carpet.api.settings.Rule;

public class EnvisionCarpetAdditionSettings {

    public static final String ENVISION = "envision";
    public static final String ENTITY = "entity";
    public static final String FAKEPLAYER = "fakeplayer";
    public static final String WORLD = "world";

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, FAKEPLAYER}
    )
    public static boolean fakePlayerNoSleepCount = false;

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, WORLD}
    )
    public static boolean respawnDragonNoObsidianSpike = false;

    @Rule(
            options = {"true", "false"},
            categories = {ENVISION, ENTITY}
    )
    public static boolean canEndermanPickUpMushroom = true;

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, FAKEPLAYER}
    )
    public static boolean fakePlayerNotAsPhantomGoal = false;

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, FAKEPLAYER}
    )
    public static boolean fakePlayerNotGeneratePhantom = false;
}
