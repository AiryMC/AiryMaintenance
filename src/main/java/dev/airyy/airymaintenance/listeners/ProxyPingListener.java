package dev.airyy.airymaintenance.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import dev.airyy.AiryLib.core.utils.Messages;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.maintenance.Maintenance;

public class ProxyPingListener extends EventListener<AiryMaintenance> {

    private final ProxyServer server = getPlugin().getServer();
    private final Maintenance maintenance = getPlugin().getMaintenance();
    private final MainConfig config = getPlugin().getMainConfig();

    public ProxyPingListener(AiryMaintenance plugin) {
        super(plugin);
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        if (!maintenance.isEnabled())
            return;

        event.setPing(ServerPing
                .builder()
                .version(new ServerPing.Version(0, Messages.serialize(config.getPingMessage())))
                .description(server.getConfiguration().getMotd())
                .build()
        );
    }
}
