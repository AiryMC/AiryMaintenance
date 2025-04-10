package dev.airyy.airymaintenance.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.AiryLib.core.utils.Timer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.config.MessageConfig;
import dev.airyy.airymaintenance.config.WhitelistConfig;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import dev.airyy.airymaintenance.utils.Players;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Sender;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "maintenance")
public class MaintenanceCommand {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private final Maintenance maintenance = plugin.getMaintenance();

    private final MessageConfig messages = MessageConfig.getInstance();
    private final MainConfig config = MainConfig.getInstance();

    @Execute
    public void onDefault(@Sender CommandSource source) {
        source.sendMessage(messages.getMaintenanceStatus(maintenance.isEnabled()));
    }

    @Execute(name = "reload")
    public void onReload(@Sender CommandSource source) {
        Timer timer = Timer.start();

        MainConfig.getInstance().reload();
        MessageConfig.getInstance().reload();
        WhitelistConfig.getInstance().reload();

        String timeString = timer.getString(Timer.TimerUnit.Milliseconds);

        source.sendMessage(messages.getReloadMessage(timeString));
    }

    @Execute(name = "on")
    public void onEnable(@Sender CommandSource source) {
        maintenance.setEnabled(true);
        config.save();

        for (Player player : plugin.getServer().getAllPlayers()) {
            maintenance.kickPlayer(player);
        }

        source.sendMessage(messages.getMaintenanceEnabled());
    }

    @Execute(name = "off")
    public void onDisable(@Sender CommandSource source) {
        maintenance.setEnabled(false);
        config.save();

        source.sendMessage(messages.getMaintenanceDisabled());
    }

    @Execute(name = "add")
    public void onAdd(@Sender CommandSource source, @Arg("player") String target) {
        boolean status = maintenance.addToWhitelist(target);
        if (!status) {
            source.sendMessage(messages.getMaintenanceAddFailure(target));
            return;
        }

        source.sendMessage(messages.getMaintenanceAddSuccess(target));
    }

    @Execute(name = "remove")
    public void onRemove(@Sender CommandSource source, @Arg("player") String target) {
        boolean status = maintenance.removeFromWhitelist(target);
        if (!status) {
            source.sendMessage(messages.getMaintenanceRemoveFailure(target));
            return;
        }

        source.sendMessage(messages.getMaintenanceRemoveSuccess(target));
    }
}
