package dev.airyy.airymaintenance.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.AiryLib.core.command.annotation.Command;
import dev.airyy.AiryLib.core.command.annotation.Default;
import dev.airyy.AiryLib.core.command.annotation.Handler;
import dev.airyy.AiryLib.core.command.annotation.OptionalArg;
import dev.airyy.AiryLib.core.utils.Timer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.config.MessageConfig;
import dev.airyy.airymaintenance.config.WhitelistConfig;
import dev.airyy.airymaintenance.maintenance.Maintenance;

import java.util.Collection;
import java.util.Optional;

@Command("maintenance")
public class MaintenanceCommand {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private final Maintenance maintenance = plugin.getMaintenance();

    private final MessageConfig messages = MessageConfig.getInstance();
    private final MainConfig config = MainConfig.getInstance();

    @Default
    public void onDefault(CommandSource source) {
        source.sendMessage(messages.getMaintenanceStatus(maintenance.isEnabled(null)));
    }

    @Handler("reload")
    public void onReload(CommandSource source) {
        Timer timer = Timer.start();

        MainConfig.getInstance().reload();
        MessageConfig.getInstance().reload();
        WhitelistConfig.getInstance().reload();

        String timeString = timer.getString(Timer.TimerUnit.Milliseconds);

        source.sendMessage(messages.getReloadMessage(timeString));
    }

    @Handler("on")
    public void onEnable(CommandSource source, @OptionalArg RegisteredServer server) {
        maintenance.setEnabled(true, server);
        config.save();

        Collection<Player> players = (server != null)
                ? server.getPlayersConnected()
                : plugin.getServer().getAllPlayers();

        for (Player player : players) {
            if (maintenance.isWhitelisted(player, server))
                continue;

            if (maintenance.kickPlayer(player, server))
                break;
        }

        source.sendMessage(messages.getMaintenanceEnabled());
    }

    @Handler("off")
    public void onDisable(CommandSource source, @OptionalArg RegisteredServer server) {
        boolean global = (server == null);

        maintenance.setEnabled(false, server);
        maintenance.setEnabled(global, server);

        config.save();

        source.sendMessage(messages.getMaintenanceDisabled());
    }

    @Handler("add")
    public void onAdd(CommandSource source, String target, @OptionalArg RegisteredServer server) {
        boolean status = maintenance.addToWhitelist(target, server);

        if (!status) {
            source.sendMessage(messages.getMaintenanceAddFailure(target));
            return;
        }

        source.sendMessage(messages.getMaintenanceAddSuccess(target));
    }

    @Handler("remove")
    public void onRemove(CommandSource source, String target, @OptionalArg RegisteredServer server) {
        boolean status = maintenance.removeFromWhitelist(target, server);

        if (!status) {
            source.sendMessage(messages.getMaintenanceRemoveFailure(target));
            return;
        }

        ProxyServer proxy = plugin.getServer();
        boolean global = (server == null);

        Optional<Player> playerOptional = proxy.getPlayer(target);
        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            if (maintenance.isEnabled(server) && !maintenance.isWhitelisted(player, server)) {
                maintenance.kickPlayer(player, server);
            }
        }

        source.sendMessage(messages.getMaintenanceRemoveSuccess(target));
    }
}
