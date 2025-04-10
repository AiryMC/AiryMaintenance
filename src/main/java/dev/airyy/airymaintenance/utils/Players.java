package dev.airyy.airymaintenance.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.airyy.airymaintenance.AiryMaintenance;
import org.slf4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public final class Players {

    private static final Logger logger = AiryMaintenance.getInstance().getLogger();

    public static UUID getUUIDFromName(String playerName) throws Exception {
        String url = Http.PROFILE_ENDPOINT + playerName;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        client.close();

        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        if (!json.has("id")) {
            throw new Exception("Key \"id\" does not exist in json body");
        }
        String uuidStr = json.get("id").getAsString();

        return UUIDUtil.formatUUID(uuidStr);
    }
}
