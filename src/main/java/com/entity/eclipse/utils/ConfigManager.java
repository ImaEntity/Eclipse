package com.entity.eclipse.utils;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.types.DynamicValue;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

// I don't care that it's more readable.
// I don't care that there's built-in libraries.
// Fuck JSON it's shit (in Java).
// But this is:
//     1. Easier
//     2. Faster
//     3. Smaller
//     4. Doesn't have to parse object trees
public class ConfigManager {
    private static final int VERSION = 5;
    private static final File configFile = new File(Eclipse.client.runDirectory.getAbsolutePath() + "/." + Eclipse.MOD_ID + "/config");

    private static byte[] configToBytes(Configuration config) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write(config.getAll().size());
        for(String key : config.getAll()) {
            DynamicValue<?> rawValue = config.getRaw(key);
            String value = rawValue.toRawString();

            stream.write(key.length() >> 8);
            stream.write(key.length() & 0xFF);
            stream.writeBytes(key.getBytes());

            stream.write(value.length() >> 8);
            stream.write(value.length() & 0xFF);
            stream.writeBytes(value.getBytes());
        }

        return stream.toByteArray();
    }

    public static void saveState() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        stream.write(VERSION);

        // decompressed length
        // (overwritten later)
        stream.write(0);
        stream.write(0);

        stream.writeBytes(configToBytes(Eclipse.config));

        stream.write(ModuleManager.getModules().size());
        for(Module module : ModuleManager.getModules()) {
            String name = module.getName()
                    .replace('λ', '_'); // unicode bs

            stream.write(name.length() >> 8);
            stream.write(name.length() & 0xFF);
            stream.writeBytes(name.getBytes());

            stream.write(module.keybind.getCode() >> 24);
            stream.write(module.keybind.getCode() >> 16 & 0xFF);
            stream.write(module.keybind.getCode() >> 8 & 0xFF);
            stream.write(module.keybind.getCode() & 0xFF);

            stream.write(module.keybind.isKey() ? 1 : 0);
            stream.write(module.keybind.togglesOnRelease() ? 1 : 0);

            stream.write(module.shouldShowToasts() ? 1 : 0);

            stream.writeBytes(configToBytes(module.config));
        }

        stream.write(ModuleManager.getActiveModules().size());
        for(Module module : ModuleManager.getActiveModules()) {
            String name = module.getName();

            stream.write(name.length() >> 8);
            stream.write(name.length() & 0xFF);
            stream.writeBytes(name.getBytes());
        }

        // very stupid
        byte[] finalConfig = stream.toByteArray();
        finalConfig[1] = (byte) (stream.size() >> 8);
        finalConfig[2] = (byte) (stream.size() & 0xFF);

        Deflater deflater = new Deflater();
        byte[] tempOut = new byte[stream.size()];

        deflater.setInput(finalConfig);
        deflater.finish();

        int deflatedSize = deflater.deflate(tempOut);
        deflater.end();

        byte[] compressedBytes = new byte[deflatedSize];
        System.arraycopy(tempOut, 0, compressedBytes, 0, deflatedSize);

        try {
            FileUtils.writeByteArrayToFile(configFile, compressedBytes);
            Eclipse.log("Saved client configs!");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadState() {
        try {
            Inflater inflater = new Inflater();
            byte[] compressedBytes = FileUtils.readFileToByteArray(configFile);
            byte[] uncompressedBytes = new byte[4096]; // This is dumb

            inflater.setInput(compressedBytes);
            inflater.inflate(uncompressedBytes);
            inflater.end();

            ByteArrayInputStream stream = new ByteArrayInputStream(uncompressedBytes);

            int version = stream.read();
            Eclipse.log("Config version: " + version);

            int decompressedSize = -1;
            if(version >= 5) decompressedSize = (short) (stream.read() << 8 | stream.read());

            // even more stupid
            if(decompressedSize != -1) {
                Inflater betterInflater = new Inflater();
                byte[] decompressedBytes = new byte[decompressedSize];

                betterInflater.setInput(compressedBytes);
                betterInflater.inflate(decompressedBytes);
                betterInflater.end();

                stream = new ByteArrayInputStream(decompressedBytes);
                stream.readNBytes(3);
            }

            int optionCount = stream.read();
            for(int i = 0; i < optionCount; i++) {
                short keyLength = (short) (stream.read() << 8 | stream.read());
                String key = new String(stream.readNBytes(keyLength));

                short valueLength = (short) (stream.read() << 8 | stream.read());
                String value = new String(stream.readNBytes(valueLength));

                if(Eclipse.config.getRaw(key) == null) {
                    Eclipse.log("Ignoring missing property: " + key);
                    continue;
                }

                Eclipse.config.create(
                        key,
                        Eclipse.config.getRaw(key).fromString(value)
                );
            }

            int moduleCount = stream.read();
            for(int i = 0; i < moduleCount; i++) {
                short nameLength = (short) (stream.read() << 8 | stream.read());
                String name = new String(stream.readNBytes(nameLength))
                        .replace('_', 'λ'); // for modules from scripts

                boolean modifyModule = true;
                Module module = ModuleManager.getByName(name);
                if(module == null) {
                    Eclipse.log("Failed to load module: " + name);
                    modifyModule = false;
                }

                int keybindCode = stream.read() << 24 | stream.read() << 16 | stream.read() << 8 | stream.read();
                boolean keybindIsKey = stream.read() == 1;
                boolean keybindTOR = false;
                if(version >= 3)
                    keybindTOR = stream.read() == 1;

                if(modifyModule) {
                    module.keybind = keybindIsKey ?
                            Keybind.key(keybindCode, keybindTOR) :
                            Keybind.mouse(keybindCode, keybindTOR);
                }

                boolean showToasts = true;
                if(version >= 4)
                    showToasts = stream.read() == 1;

                if(modifyModule)
                    module.shouldShowToasts(showToasts);

                int moduleOptionCount = stream.read();
                for(int j = 0; j < moduleOptionCount; j++) {
                    short keyLength = (short) (stream.read() << 8 | stream.read());
                    String key = new String(stream.readNBytes(keyLength));

                    short valueLength = (short) (stream.read() << 8 | stream.read());
                    String value = new String(stream.readNBytes(valueLength));

                    if(modifyModule && module.config.getRaw(key) == null) {
                        Eclipse.log("Ignoring missing property: " + key + " on module: " + module.getName());
                        continue;
                    }

                    if(modifyModule) {
                        module.config.create(
                                key,
                                module.config.getRaw(key).fromString(value)
                        );
                    }
                }
            }

            int activeCount = stream.read();
            for(int i = 0; i < activeCount; i++) {
                short nameLength = (short) (stream.read() << 8 | stream.read());
                String name = new String(stream.readNBytes(nameLength));

                Module module = ModuleManager.getByName(name);
                if(module == null) {
                    Eclipse.log("Failed to enable module: " + name);
                    continue;
                }

                ModuleManager.queueEnable(module);
            }

            Eclipse.log("Loaded client configs!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
