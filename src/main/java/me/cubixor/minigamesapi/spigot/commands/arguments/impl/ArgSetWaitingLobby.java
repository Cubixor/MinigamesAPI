package me.cubixor.minigamesapi.spigot.commands.arguments.impl;

import me.cubixor.minigamesapi.spigot.arena.ArenasManager;
import me.cubixor.minigamesapi.spigot.commands.arguments.LocationArgument;
import me.cubixor.minigamesapi.spigot.config.arenas.BasicConfigField;

public class ArgSetWaitingLobby extends LocationArgument {


    public ArgSetWaitingLobby(ArenasManager arenasManager) {
        super(arenasManager, "setwaitinglobby", "setup.setwaitinglobby", "arena-setup.set-main-lobby", BasicConfigField.WAITING_LOBBY);
    }
}
