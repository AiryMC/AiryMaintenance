package dev.airyy.airymaintenance.config;

import dev.airyy.airymaintenance.AiryMaintenance;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

public abstract class Config {

    private static final AiryMaintenance plugin = AiryMaintenance.getInstance();
    private static final Logger logger = plugin.getLogger();
    private static final Path dataDirectory = plugin.getDataDirectory();

    @Nullable
    private final YamlDocument document;
    private String path;

    public Config(String path) {
        this.document = createDocument(path);
        if (document != null && document.getFile() != null) {
            this.path = this.document.getFile().getPath();
        }
    }

    public static @Nullable YamlDocument createDocument(String path) {
        logger.info("Loading config: {}", path);

        try {
            File file = new File(dataDirectory.toString(), path);
            URL resource = Config.class.getClassLoader().getResource(path);
            if (resource != null) {
                return YamlDocument.create(file, resource.openStream());
            } else {
                return YamlDocument.create(file);
            }

        } catch (IOException e) {
            logger.error("Failed to create/load yaml document: {}.", path);
            return null;
        }
    }

    public static @Nullable YamlDocument createDocument(File file) {
        return createDocument(file.getPath());
    }

    protected abstract void onLoad();

    protected abstract void onSave();

    public void save() {
        if (document == null)
            return;

        onSave();

        try {
            document.save();
        } catch (IOException e) {
            logger.error("Failed to save config: {}", path);
        }
    }

    public void load() {
        if (document == null)
            return;

        try {
            document.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        onLoad();
    }

    public void reload() {
        if (document == null)
            return;

        try {
            document.reload();
        } catch (IOException e) {
            logger.error("Failed to reload config: {}", path);
        }

        onLoad();
    }

    public @Nullable YamlDocument getDocument() {
        return document;
    }

    public String getPath() {
        return path;
    }
}
