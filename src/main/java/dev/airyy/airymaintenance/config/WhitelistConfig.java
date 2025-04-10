package dev.airyy.airymaintenance.config;

import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import dev.dejvokep.boostedyaml.route.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WhitelistConfig extends Config {

    private static WhitelistConfig instance;

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private final Maintenance maintenance;

    public WhitelistConfig() {
        super("whitelist.yml");

        instance = this;

        this.maintenance = plugin.getMaintenance();
    }

    @Override
    protected void onLoad() {
        if (getDocument() == null)
            return;

        List<String> uuids = getDocument().getStringList(Route.from("whitelist"));
        List<UUID> whitelist = uuids.stream().map(UUID::fromString).toList();
        List<UUID> newWhitelist = new ArrayList<>(whitelist);
        maintenance.setWhitelist(newWhitelist);
    }

    @Override
    protected void onSave() {
        if (getDocument() == null)
            return;

        List<String> uuids = maintenance.getWhitelist().stream().map(UUID::toString).toList();
        plugin.getLogger().info("Whitelisted Players: {}", uuids.size());

        getDocument().set(Route.from("whitelist"), uuids);
    }

    public static WhitelistConfig getInstance() {
        return instance;
    }
}
