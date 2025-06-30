package dev.airyy.airymaintenance.commands.arguments;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import dev.airyy.airymaintenance.AiryMaintenance;
import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;

import java.util.Optional;

public class RegisteredServerArgument extends ArgumentResolver<CommandSource, RegisteredServer> {

    private final AiryMaintenance plugin = AiryMaintenance.getInstance();

    @Override
    protected ParseResult<RegisteredServer> parse(Invocation<CommandSource> invocation, Argument<RegisteredServer> context, String argument) {
        Optional<RegisteredServer> registeredServer = plugin.getServer().getServer(argument);
        if (registeredServer.isEmpty()) {
            return ParseResult.failure("Could not find a registered server with the provided name");
        }

        return ParseResult.success(registeredServer.get());
    }

    @Override
    public SuggestionResult suggest(Invocation<CommandSource> invocation, Argument<RegisteredServer> argument, SuggestionContext context) {
        return plugin.getServer().getAllServers().stream().map((server) -> server.getServerInfo().getName()).collect(SuggestionResult.collector());
    }
}
