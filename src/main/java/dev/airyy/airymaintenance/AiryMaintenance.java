package dev.airyy.airymaintenance;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.AiryLib.velocity.AiryPlugin;
import dev.airyy.airymaintenance.commands.MaintenanceCommand;
import dev.airyy.airymaintenance.commands.arguments.RegisteredServerArgument;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.config.MessageConfig;
import dev.airyy.airymaintenance.config.WhitelistConfig;
import dev.airyy.airymaintenance.listeners.PlayerConnectListener;
import dev.airyy.airymaintenance.listeners.ProxyPingListener;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(id = "airymaintenance", name = "AiryMaintenance", version = BuildConstants.VERSION, authors = {"AiryyCodes"})
public class AiryMaintenance extends AiryPlugin {

    private final Maintenance maintenance;
    private final MainConfig mainConfig;
    private final MessageConfig messageConfig;
    private final WhitelistConfig whitelistConfig;

    @Inject
    public AiryMaintenance(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        super(server, logger, dataDirectory);

        maintenance = new Maintenance();

        mainConfig = new MainConfig();
        messageConfig = new MessageConfig();
        whitelistConfig = new WhitelistConfig();
    }

    @Override
    public void onInit() {
        mainConfig.load();
        messageConfig.load();
        whitelistConfig.load();

        getCommandManager().registerArgumentParser(RegisteredServer.class, new RegisteredServerArgument());
        getCommandManager().register(new MaintenanceCommand());

        getServer().getEventManager().register(this, new ProxyPingListener(this));
        getServer().getEventManager().register(this, new PlayerConnectListener(this));
    }

    @Override
    public void onDestroy() {
        // Ensure changes in configs are not overriden by the plugin state
        mainConfig.reload();
        messageConfig.reload();
        whitelistConfig.reload();

        mainConfig.save();
        messageConfig.save();
        whitelistConfig.save();
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }
}
