package dev.airyy.airymaintenance.config;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.airyy.airymaintenance.maintenance.Maintenance;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import dev.dejvokep.boostedyaml.route.Route;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        List<String> globalUUIDs = getDocument().getStringList(Route.from("whitelist"));
        List<UUID> globalWhitelist = globalUUIDs.stream().map(UUID::fromString).toList();
        List<UUID> newGlobalWhitelist = new ArrayList<>(globalWhitelist);
        maintenance.setGlobalWhitelist(newGlobalWhitelist);

        // Load the whitelist of individual servers
        Section serversSection = getDocument().getSection("servers");
        Map<String, List<String>> serverUUIDs = new HashMap<>();

        if (serversSection != null) {
            for (Object keyObj : serversSection.getKeys()) {
                if (!(keyObj instanceof String key)) continue;

                List<String> uuidStrings = serversSection.getStringList(key);
                serverUUIDs.put(key, uuidStrings);
            }
        }

        Map<RegisteredServer, Set<UUID>> serverWhitelist = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : serverUUIDs.entrySet()) {
            Optional<RegisteredServer> optionalServer = plugin.getServer().getServer(entry.getKey());
            if (optionalServer.isEmpty())
                continue;

            Set<UUID> uuids = entry.getValue().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());

            serverWhitelist.put(optionalServer.get(), uuids);
        }

        maintenance.setWhitelist(serverWhitelist);
    }

    @Override
    protected void onSave() {
        if (getDocument() == null)
            return;

        List<String> globalWhitelist = maintenance.getGlobalWhitelist().stream().map(UUID::toString).toList();

        Map<RegisteredServer, Set<UUID>> originalWhitelist = maintenance.getWhitelist();

        Map<String, List<String>> serverUUIDs = originalWhitelist.entrySet().stream().collect(Collectors.toMap(
                entry -> entry.getKey().getServerInfo().getName(),
                entry -> entry.getValue().stream()
                        .map(UUID::toString)
                        .collect(Collectors.toList())));

        getDocument().set(Route.from("whitelist"), globalWhitelist);
        getDocument().set(Route.from("servers"), serverUUIDs);
    }

    public static WhitelistConfig getInstance() {
        return instance;
    }
}
