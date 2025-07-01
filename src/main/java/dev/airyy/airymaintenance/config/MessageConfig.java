package dev.airyy.airymaintenance.config;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.AiryLib.core.utils.Messages;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.dejvokep.boostedyaml.route.Route;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class MessageConfig extends Config {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private static MessageConfig instance;

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private Component prefix;
    private Component reloadMessage;
    private Component active;
    private Component inactive;
    private Component maintenanceStatus;
    private Component maintenanceStatusServer;
    private Component maintenanceEnabled;
    private Component maintenanceEnabledServer;
    private Component maintenanceDisabled;
    private Component maintenanceDisabledServer;
    private Component maintenanceAddSuccess;
    private Component maintenanceAddSuccessServer;
    private Component maintenanceAddFailure;
    private Component maintenanceAddFailureServer;
    private Component maintenanceRemoveSuccess;
    private Component maintenanceRemoveSuccessServer;
    private Component maintenanceRemoveFailure;
    private Component maintenanceRemoveFailureServer;

    public MessageConfig() {
        super("messages.yml");

        instance = this;
    }

    @Override
    protected void onLoad() {
        if (getDocument() == null)
            return;

        prefix = miniMessage.deserialize(getDocument().getString(Route.from("prefix")));
        reloadMessage = miniMessage.deserialize(getDocument().getString(Route.from("reload-message")));
        active = miniMessage.deserialize(getDocument().getString(Route.from("active")));
        inactive = miniMessage.deserialize(getDocument().getString(Route.from("inactive")));
        maintenanceStatus = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-status")));
        maintenanceStatusServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-status-server")));
        maintenanceEnabled = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-enabled")));
        maintenanceEnabledServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-enabled-server")));
        maintenanceDisabled = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-disabled")));
        maintenanceDisabledServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-disabled-server")));
        maintenanceAddSuccess = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-success")));
        maintenanceAddSuccessServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-success-server")));
        maintenanceAddFailure = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-failure")));
        maintenanceAddFailureServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-failure-server")));
        maintenanceRemoveSuccess = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-success")));
        maintenanceRemoveSuccessServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-success-server")));
        maintenanceRemoveFailure = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-failure")));
        maintenanceRemoveFailureServer = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-failure-server")));
    }

    @Override
    protected void onSave() {
        if (getDocument() == null)
            return;

        getDocument().set(Route.from("prefix"), miniMessage.serialize(prefix));
        getDocument().set(Route.from("reload-message"), miniMessage.serialize(reloadMessage));
        getDocument().set(Route.from("active"), miniMessage.serialize(active));
        getDocument().set(Route.from("inactive"), miniMessage.serialize(inactive));
        getDocument().set(Route.from("maintenance-status"), miniMessage.serialize(maintenanceStatus));
        getDocument().set(Route.from("maintenance-status-server"), miniMessage.serialize(maintenanceStatusServer));
        getDocument().set(Route.from("maintenance-enabled"), miniMessage.serialize(maintenanceEnabled));
        getDocument().set(Route.from("maintenance-enabled-server"), miniMessage.serialize(maintenanceEnabledServer));
        getDocument().set(Route.from("maintenance-disabled"), miniMessage.serialize(maintenanceDisabled));
        getDocument().set(Route.from("maintenance-disabled-server"), miniMessage.serialize(maintenanceDisabledServer));
        getDocument().set(Route.from("maintenance-add-success"), miniMessage.serialize(maintenanceAddSuccess));
        getDocument().set(Route.from("maintenance-add-success-server"), miniMessage.serialize(maintenanceAddSuccessServer));
        getDocument().set(Route.from("maintenance-add-failure"), miniMessage.serialize(maintenanceAddFailure));
        getDocument().set(Route.from("maintenance-add-failure-server"), miniMessage.serialize(maintenanceAddFailureServer));
        getDocument().set(Route.from("maintenance-remove-success"), miniMessage.serialize(maintenanceRemoveSuccess));
        getDocument().set(Route.from("maintenance-remove-success-server"), miniMessage.serialize(maintenanceRemoveSuccessServer));
        getDocument().set(Route.from("maintenance-remove-failure"), miniMessage.serialize(maintenanceRemoveFailure));
        getDocument().set(Route.from("maintenance-remove-failure-server"), miniMessage.serialize(maintenanceRemoveFailureServer));
    }

    public static MessageConfig getInstance() {
        return instance;
    }

    public Component getPrefix() {
        if (prefix == null) {
            plugin.getLogger().error("Prefix component is null!");
        }

        return prefix;
    }

    public Component getReloadMessage(String time) {
        return Messages.replaceAll(reloadMessage,
                "{prefix}", Messages.serialize(getPrefix()),
                "{time}", time
        );
    }

    public Component getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public Component getMaintenanceStatus(boolean status) {
        return Messages.replaceAll(maintenanceStatus,
                "{prefix}", Messages.serialize(prefix),
                "{status}", Messages.serialize(status ? getActive() : getInactive())
        );
    }

    public Component getMaintenanceStatusServer(boolean status, RegisteredServer server) {
        return Messages.replaceAll(maintenanceStatusServer,
                "{prefix}", Messages.serialize(prefix),
                "{status}", Messages.serialize(status ? getActive() : getInactive()),
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getActive() {
        return Messages.replaceAll(active);
    }

    public Component getInactive() {
        return Messages.replaceAll(inactive);
    }

    public Component getMaintenanceEnabled() {
        return Messages.replaceAll(maintenanceEnabled,
                "{prefix}", Messages.serialize(getPrefix())
        );
    }

    public Component getMaintenanceEnabledServer(RegisteredServer server) {
        return Messages.replaceAll(maintenanceEnabledServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getMaintenanceDisabled() {
        return Messages.replaceAll(maintenanceDisabled,
                "{prefix}", Messages.serialize(getPrefix())
        );
    }

    public Component getMaintenanceDisabledServer(RegisteredServer server) {
        return Messages.replaceAll(maintenanceDisabledServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getMaintenanceAddSuccess(String player) {
        return Messages.replaceAll(maintenanceAddSuccess,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceAddSuccessServer(String player, RegisteredServer server) {
        return Messages.replaceAll(maintenanceAddSuccessServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player,
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getMaintenanceAddFailure(String player) {
        return Messages.replaceAll(maintenanceAddFailure,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceAddFailureServer(String player, RegisteredServer server) {
        return Messages.replaceAll(maintenanceAddFailureServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player,
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getMaintenanceRemoveSuccess(String player) {
        return Messages.replaceAll(maintenanceRemoveSuccess,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceRemoveSuccessServer(String player, RegisteredServer server) {
        return Messages.replaceAll(maintenanceRemoveSuccessServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player,
                "{server}", server.getServerInfo().getName()
        );
    }

    public Component getMaintenanceRemoveFailure(String player) {
        return Messages.replaceAll(maintenanceRemoveFailure,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceRemoveFailureServer(String player, RegisteredServer server) {
        return Messages.replaceAll(maintenanceRemoveFailureServer,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player,
                "{server}", server.getServerInfo().getName()
        );
    }
}
