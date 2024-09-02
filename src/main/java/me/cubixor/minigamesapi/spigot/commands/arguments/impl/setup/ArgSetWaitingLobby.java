package me.cubixor.minigamesapi.spigot.commands.arguments.impl.setup;

import me.cubixor.minigamesapi.spigot.commands.arguments.LocationArgument;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;
import me.cubixor.minigamesapi.spigot.game.ArenasManager;

public class ArgSetWaitingLobby extends LocationArgument {


    public ArgSetWaitingLobby(ArenasManager arenasManager) {
        super(arenasManager, "setwaitinglobby", "setup.setwaitinglobby", "arena-setup.set-waiting-lobby", BasicConfigField.WAITING_LOBBY);
    }
}
