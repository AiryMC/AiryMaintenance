package dev.airyy.airymaintenance.listeners;

import dev.airyy.AiryLib.velocity.AiryPlugin;

public abstract class EventListener<T extends AiryPlugin> {

    private final T plugin;

    public EventListener(T plugin) {
        this.plugin = plugin;
    }

    public T getPlugin() {
        return plugin;
    }
}
