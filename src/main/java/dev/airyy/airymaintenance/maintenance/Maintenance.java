package dev.airyy.airymaintenance.maintenance;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.config.WhitelistConfig;
import dev.airyy.airymaintenance.utils.Players;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class Maintenance {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private MainConfig config = plugin.getMainConfig();

    private boolean enabled = false;
    private List<UUID> globalWhitelist = new ArrayList<>();
    private Map<RegisteredServer, Set<UUID>> whitelist = new HashMap<>();
    private Map<RegisteredServer, Boolean> lockedServers = new HashMap<>();

    public boolean addToWhitelist(String playerName) {
        return modifyWhitelist(playerName, Action.ADD, null);
    }

    public boolean addToWhitelist(String playerName, RegisteredServer server) {
        return modifyWhitelist(playerName, Action.ADD, server);
    }

    public boolean removeFromWhitelist(String playerName) {
        return modifyWhitelist(playerName, Action.REMOVE, null);
    }

    public boolean removeFromWhitelist(String playerName, RegisteredServer server) {
        return modifyWhitelist(playerName, Action.REMOVE, server);
    }

    private boolean modifyWhitelist(String playerName, Action action, @Nullable RegisteredServer server) {
        try {
            UUID uuid = Players.getUUIDFromName(playerName);

            if (server == null) {
                if (action == Action.ADD && !globalWhitelist.contains(uuid)) {
                    globalWhitelist.add(uuid);
                } else if (action == Action.REMOVE) {
                    globalWhitelist.remove(uuid);
                }
            } else {
                Set<UUID> newWhitelist = whitelist.containsKey(server) ? whitelist.get(server) : new LinkedHashSet<>();

                if (action == Action.ADD && !newWhitelist.contains(uuid)) {
                    whitelist.put(server, newWhitelist);
                    plugin.getLogger().info("Whitelisted Players: {}", whitelist.get(server).size());

                    whitelist.computeIfAbsent(server, s -> new LinkedHashSet<>()).add(uuid);

                    plugin.getLogger().info("Whitelisted Players: {}", whitelist.get(server).size());

                } else if (action == Action.REMOVE && whitelist.containsKey(server) && newWhitelist.contains(uuid)) {
                    whitelist.get(server).remove(uuid);
                }
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

    public boolean kickPlayer(Player player, boolean isGlobal) {
        // Ensure config is not null
        if (config == null) {
            config = plugin.getMainConfig();
        }

        if (isGlobal && isEnabled() && !isWhitelisted(player)) {
            player.disconnect(config.getKickMessage());
            return true;
        }

        for (RegisteredServer fallbackServer : config.getFallbackServers()) {
            plugin.getLogger().info("Fallback Server: {}", fallbackServer.getServerInfo().getName());
            plugin.getLogger().info("Enabled: {}", isEnabled(fallbackServer)); // isEnabled returns true when maintenance is enabled
            plugin.getLogger().info("Whitelisted: {}", isWhitelisted(player, fallbackServer));

            // If maintenance is NOT enabled on the server, attempt to connect to the fallback server
            if (!isEnabled(fallbackServer)) {
                plugin.getLogger().info("Connecting player to fallback server {}...", fallbackServer.getServerInfo().getName());
                player.createConnectionRequest(fallbackServer).connect().whenComplete((result, error) -> {
                    if (error != null) {
                        plugin.getLogger().warn("Failed to connect to fallback server: {}", error.getMessage());
                        player.disconnect(config.getKickMessage()); // Disconnect if fallback fails
                    }
                });

                return true; // Return true if connection is successful
            }

            // If maintenance is enabled, skip this fallback server
            if (!isWhitelisted(player, fallbackServer)) {
                continue; // Continue to the next fallback server if the player is not whitelisted
            }
        }

        // If per-server and player isn't allowed on that server
        player.disconnect(config.getKickMessage());

        return true;
    }

    public boolean isWhitelisted(Player player, @Nullable RegisteredServer server) {
        if (server == null)
            return globalWhitelist.contains(player.getUniqueId());

        return whitelist.getOrDefault(server, Set.of()).contains(player.getUniqueId());
    }

    public boolean isWhitelisted(Player player) {
        return globalWhitelist.contains(player.getUniqueId());
    }

    public List<UUID> getGlobalWhitelist() {
        return globalWhitelist;
    }

    public void setGlobalWhitelist(List<UUID> globalWhitelist) {
        this.globalWhitelist = globalWhitelist;
    }

    public void save() {
        WhitelistConfig.getInstance().save();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabled(RegisteredServer server, boolean enabled) {
        this.lockedServers.put(server, enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isEnabled(RegisteredServer server) {
        // If maintenance is enabled it returns true otherwise false
        return lockedServers.getOrDefault(server, false);
    }

    private enum Action {
        ADD, REMOVE
    }
}
