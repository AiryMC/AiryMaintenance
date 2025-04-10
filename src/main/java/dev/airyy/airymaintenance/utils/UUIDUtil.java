package dev.airyy.airymaintenance.utils;

import java.util.UUID;

public final class UUIDUtil {

    public static UUID formatUUID(String uuid) {
        // Remove any existing dashes
        uuid = uuid.replace("-", "");

        // Ensure the string is 32 characters long before adding dashes
        if (uuid.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID length");
        }

        return UUID.fromString(uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-"
                + uuid.substring(16, 20) + "-" + uuid.substring(20, 32));
    }
}
