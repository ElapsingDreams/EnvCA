package org.envisioncraft.envisionCarpetAddition;

import carpet.api.settings.Rule;
import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.*;

public class EnvisionCarpetAdditionSettings {

    public static final String ENVISION = "envision";

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, SURVIVAL}
    )
    public static boolean ignoreFakePlayerSleep = false;

    @Rule(
            options = {"false", "true"},
            categories = {ENVISION, SURVIVAL}
    )
    public static boolean respawnDragonNoObsidianSpike = false;
}
