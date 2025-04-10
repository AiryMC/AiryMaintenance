package dev.airyy.airymaintenance.config;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import dev.dejvokep.boostedyaml.dvs.versioning.AutomaticVersioning;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.dvs.versioning.ManualVersioning;
import dev.dejvokep.boostedyaml.libs.org.snakeyaml.engine.v2.api.LoadSettings;
import dev.dejvokep.boostedyaml.route.Route;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainConfig extends Config {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private final ProxyServer server = plugin.getServer();
    private static MainConfig instance;

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final Maintenance maintenance = plugin.getMaintenance();
    private Component kickMessage;
    private Component pingMessage;
    private List<RegisteredServer> fallbackServers = new ArrayList<>();

    public MainConfig() {
        super("config.yml");

        instance = this;
    }

    @Override
    protected void onLoad() {
        if (getDocument() == null)
            return;

        maintenance.setEnabled(getDocument().getBoolean(Route.from("maintenance")));
        kickMessage = miniMessage.deserialize(getDocument().getString(Route.from("kick-message")));
        pingMessage = miniMessage.deserialize(getDocument().getString(Route.from("ping-message")));

        List<String> serverNames = getDocument().getStringList(Route.from("fallback-servers"));
        fallbackServers = new ArrayList<>();
        for (String name : serverNames) {
            Optional<RegisteredServer> registeredServer = server.getServer(name);
            if (registeredServer.isEmpty()) {
                plugin.getLogger().warn("Could not find registered server with name {}. Please check your configs", name);
                continue;
            }

            fallbackServers.add(registeredServer.get());
        }
    }

    @Override
    protected void onSave() {
        if (getDocument() == null)
            return;

        getDocument().set(Route.from("maintenance"), maintenance.isEnabled());
        getDocument().set(Route.from("kick-message"), miniMessage.serialize(kickMessage));
        getDocument().set(Route.from("ping-message"), miniMessage.serialize(pingMessage));

        List<String> serverNames = fallbackServers.stream().map((server) -> server.getServerInfo().getName()).toList();
        getDocument().set(Route.from("fallback-servers"), serverNames);
    }

    public static MainConfig getInstance() {
        return instance;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public Component getKickMessage() {
        return kickMessage;
    }

    public Component getPingMessage() {
        return pingMessage;
    }

    public List<RegisteredServer> getFallbackServers() {
        return fallbackServers;
    }
}
