package dev.airyy.airymaintenance.commands.arguments;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.AiryLib.core.command.argument.IArgument;
import dev.airyy.airymaintenance.AiryMaintenance;

import java.util.List;
import java.util.Optional;

public class RegisteredServerArgument implements IArgument<RegisteredServer> {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();

    @Override
    public RegisteredServer parse(String s) {
        Optional<RegisteredServer> registeredServer = plugin.getServer().getServer(s);
        if (registeredServer.isEmpty()) {
            return null;
        }

        return registeredServer.get();
    }

    @Override
    public List<String> suggest(String input) {
        return plugin.getServer().getAllServers().stream().map((server) -> server.getServerInfo().getName()).toList();
    }
}
