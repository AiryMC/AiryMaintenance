package dev.airyy.airymaintenance.maintenance;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.config.WhitelistConfig;
import dev.airyy.airymaintenance.utils.Players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Maintenance {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private MainConfig config = plugin.getMainConfig();

    private boolean enabled = false;
    private List<UUID> whitelist = new ArrayList<>();

    public boolean isWhitelisted(Player player) {
        return whitelist.contains(player.getUniqueId());
    }

    public boolean addToWhitelist(String playerName) {
        return modifyWhitelist(playerName, Action.ADD);
    }

    public boolean removeFromWhitelist(String playerName) {
        return modifyWhitelist(playerName, Action.REMOVE);
    }

    private boolean modifyWhitelist(String playerName, Action action) {
        try {
            UUID uuid = Players.getUUIDFromName(playerName);

            if (action == Action.ADD && !whitelist.contains(uuid)) {
                whitelist.add(uuid);
            } else if (action == Action.REMOVE) {
                whitelist.remove(uuid);
            }

            WhitelistConfig.getInstance().save();

            return true;
        } catch (Exception e) {
            plugin.getLogger().error("Failed to {} player to/from whitelist. Reason:", action == Action.ADD ? "add" : "remove");
            plugin.getLogger().error("Could not find player with name {}.", playerName);
            e.printStackTrace();
            return false;
        }
    }

    public void kickPlayer(Player player) {
        ProxyServer server = plugin.getServer();

        // Ensure config is not null
        if (config == null) {
            config = plugin.getMainConfig();
        }

        if (isWhitelisted(player))
            return;

        if (config.getFallbackServers().isEmpty()) {
            player.disconnect(config.getKickMessage());
            return;
        }

        for (RegisteredServer fallbackServer : config.getFallbackServers()) {
            player.createConnectionRequest(fallbackServer).connect();
        }
    }

    public List<UUID> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<UUID> whitelist) {
        this.whitelist = whitelist;
    }

    public void save() {
        WhitelistConfig.getInstance().save();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    private enum Action {
        ADD, REMOVE
    }
}
