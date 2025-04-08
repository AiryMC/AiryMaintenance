package dev.airyy.airymaintenance;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(id = "airymaintenance", name = "AiryMaintenance", version = BuildConstants.VERSION, authors = {"AiryyCodes"})
public class AiryMaintenance {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
    }
}
