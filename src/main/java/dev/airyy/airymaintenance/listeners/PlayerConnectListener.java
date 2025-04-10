package dev.airyy.airymaintenance.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.config.MainConfig;
import dev.airyy.airymaintenance.maintenance.Maintenance;

public class PlayerConnectListener extends EventListener<AiryMaintenance> {

    private final Maintenance maintenance = getPlugin().getMaintenance();
    private final MainConfig config = getPlugin().getMainConfig();

    public PlayerConnectListener(AiryMaintenance plugin) {
        super(plugin);
    }

    @Subscribe
    public void onJoin(ServerPreConnectEvent event) {
        // TODO: Check if maintenance is global or for a specific server

        if (!maintenance.isEnabled())
            return;

        if (maintenance.isWhitelisted(event.getPlayer()))
            return;


        event.setResult(ServerPreConnectEvent.ServerResult.denied());
        event.getPlayer().disconnect(config.getKickMessage());
    }
}
