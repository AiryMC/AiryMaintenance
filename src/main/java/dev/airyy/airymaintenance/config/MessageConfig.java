package dev.airyy.airymaintenance.config;

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
    private Component maintenanceEnabled;
    private Component maintenanceDisabled;
    private Component maintenanceAddSuccess;
    private Component maintenanceAddFailure;
    private Component maintenanceRemoveSuccess;
    private Component maintenanceRemoveFailure;

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
        maintenanceEnabled = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-enabled")));
        maintenanceDisabled = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-disabled")));
        maintenanceAddSuccess = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-success")));
        maintenanceAddFailure = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-add-failure")));
        maintenanceRemoveSuccess = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-success")));
        maintenanceRemoveFailure = miniMessage.deserialize(getDocument().getString(Route.from("maintenance-remove-failure")));
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
        getDocument().set(Route.from("maintenance-enabled"), miniMessage.serialize(maintenanceEnabled));
        getDocument().set(Route.from("maintenance-disabled"), miniMessage.serialize(maintenanceDisabled));
        getDocument().set(Route.from("maintenance-add-success"), miniMessage.serialize(maintenanceAddSuccess));
        getDocument().set(Route.from("maintenance-add-failure"), miniMessage.serialize(maintenanceAddFailure));
        getDocument().set(Route.from("maintenance-remove-success"), miniMessage.serialize(maintenanceRemoveSuccess));
        getDocument().set(Route.from("maintenance-remove-failure"), miniMessage.serialize(maintenanceRemoveFailure));
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

    public Component getMaintenanceDisabled() {
        return Messages.replaceAll(maintenanceDisabled,
                "{prefix}", Messages.serialize(getPrefix())
        );
    }

    public Component getMaintenanceAddSuccess(String player) {
        return Messages.replaceAll(maintenanceAddSuccess,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceAddFailure(String player) {
        return Messages.replaceAll(maintenanceAddFailure,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceRemoveSuccess(String player) {
        return Messages.replaceAll(maintenanceRemoveSuccess,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }

    public Component getMaintenanceRemoveFailure(String player) {
        return Messages.replaceAll(maintenanceRemoveFailure,
                "{prefix}", Messages.serialize(getPrefix()),
                "{player}", player
        );
    }
}
