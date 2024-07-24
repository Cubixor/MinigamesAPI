package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.game.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.LocationArgument;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;

public class ArgSetMainLobby extends LocationArgument {

    public ArgSetMainLobby(ArenasManager arenasManager) {
        super(arenasManager, "setmainlobby", "setup.setmainlobby", "arena-setup.set-main-lobby", BasicConfigField.MAIN_LOBBY);
    }
}
