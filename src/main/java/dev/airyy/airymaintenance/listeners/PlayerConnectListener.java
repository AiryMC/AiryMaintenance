package dev.airyy.airymaintenance.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import net.kyori.adventure.text.Component;

public class PlayerConnectListener extends EventListener<AiryMaintenance> {

    private final Maintenance maintenance = getPlugin().getMaintenance();
    private final MainConfig config = getPlugin().getMainConfig();

    public PlayerConnectListener(AiryMaintenance plugin) {
        super(plugin);
    }

    @Subscribe
    public void onPreJoin(ServerPreConnectEvent event) {
        Player player = event.getPlayer();
        RegisteredServer targetServer = event.getResult().getServer().orElse(null);

        // Global maintenance check
        if (maintenance.isEnabled() && !maintenance.isWhitelisted(player)) {
            maintenance.kickPlayer(player, true);
            return;
        }

        if (targetServer == null)
            return;

        targetServer.sendMessage(Component.text("Some one joined!"));
        getPlugin().getLogger().info("Target Server: " + targetServer.toString());

        if (maintenance.isEnabled(targetServer) && !maintenance.isWhitelisted(player, targetServer)) {
            RegisteredServer fallback = getFallbackServer(player);
            if (fallback == null) {
                player.disconnect(config.getKickMessage());
                return;
            }

            player.sendMessage(config.getKickMessage());
            event.setResult(ServerPreConnectEvent.ServerResult.denied());
        }
    }

    private RegisteredServer getFallbackServer(Player player) {
        for (RegisteredServer fallbackServer : config.getFallbackServers()) {
            getPlugin().getLogger().info("Fallback Server: {}", fallbackServer.getServerInfo().getName());
            getPlugin()
                    .getLogger()
                    .info(
                            "Enabled: {}",
                            maintenance.isEnabled(
                                    fallbackServer));
            getPlugin()
                    .getLogger()
                    .info("Whitelisted: {}", maintenance.isWhitelisted(player, fallbackServer));

            // If maintenance is NOT enabled on the server, attempt to connect to the
            // fallback server
            if (!maintenance.isEnabled(fallbackServer)) {
                getPlugin()
                        .getLogger()
                        .info(
                                "Connecting player to fallback server {}...",
                                fallbackServer.getServerInfo().getName());

                return fallbackServer;
            }

            // If maintenance is enabled, skip this fallback server
            if (!maintenance.isWhitelisted(player, fallbackServer)) {
                continue; // Continue to the next fallback server if the player is not whitelisted
            }

            return fallbackServer;
        }

        return null;
    }
}
