package me.cubixor.minigamesapi.spigot.config.arenas;

public enum BasicConfigField implements ConfigField {
    ACTIVE, VIP, MIN_PLAYERS, MAX_PLAYERS, MAIN_LOBBY, WAITING_LOBBY,
    ;

    @Override
    public String toString() {
        return super.toString().toLowerCase().replace('_', '-');
    }
}
