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
public class SaveManager {
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

        // Version
        stream.write(3);

        stream.writeBytes(configToBytes(Eclipse.config));

        stream.write(ModuleManager.getModules().size());
        for(Module module : ModuleManager.getModules()) {
            String name = module.getName();

            stream.write(name.length() >> 8);
            stream.write(name.length() & 0xFF);
            stream.writeBytes(name.getBytes());

            stream.write(module.keybind.getCode() >> 24);
            stream.write(module.keybind.getCode() >> 16 & 0xFF);
            stream.write(module.keybind.getCode() >> 8 & 0xFF);
            stream.write(module.keybind.getCode() & 0xFF);

            stream.write(module.keybind.isKey() ? 1 : 0);
            stream.write(module.keybind.togglesOnRelease() ? 1 : 0);

            stream.writeBytes(configToBytes(module.config));
        }

        stream.write(ModuleManager.getActiveModules().size());
        for(Module module : ModuleManager.getActiveModules()) {
            String name = module.getName();

            stream.write(name.length() >> 8);
            stream.write(name.length() & 0xFF);
            stream.writeBytes(name.getBytes());
        }

        Deflater deflater = new Deflater();
        byte[] tempOut = new byte[stream.size()];

        deflater.setInput(stream.toByteArray());
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
            byte[] tempOut = new byte[4096]; // This is dumb

            inflater.setInput(compressedBytes);
            int inflatedSize = inflater.inflate(tempOut);

            inflater.end();

            byte[] uncompressedBytes = new byte[inflatedSize];
            System.arraycopy(tempOut, 0, uncompressedBytes, 0, inflatedSize);

            ByteArrayInputStream stream = new ByteArrayInputStream(uncompressedBytes);

            int version = stream.read();

            int optionCount = stream.read();
            for(int i = 0; i < optionCount; i++) {
                short keyLength = (short) (stream.read() << 8 | stream.read());
                String key = new String(stream.readNBytes(keyLength));

                short valueLength = (short) (stream.read() << 8 | stream.read());
                String value = new String(stream.readNBytes(valueLength));

                Eclipse.config.set(key, value);
            }

            int moduleCount = stream.read();
            for(int i = 0; i < moduleCount; i++) {
                short nameLength = (short) (stream.read() << 8 | stream.read());
                String name = new String(stream.readNBytes(nameLength));

                Module module = ModuleManager.getByName(name);

                if(module == null) {
                    Eclipse.log("Failed to find module: " + name);
                    continue;
                }

                int keybindCode = stream.read() << 24 | stream.read() << 16 | stream.read() << 8 | stream.read();
                boolean keybindIsKey = stream.read() == 1;
                boolean keybindTOR = version < 3 && stream.read() == 1;

                module.keybind = keybindIsKey ?
                        Keybind.key(keybindCode, keybindTOR) :
                        Keybind.mouse(keybindCode, keybindTOR);

                int moduleOptionCount = stream.read();
                for(int j = 0; j < moduleOptionCount; j++) {
                    short keyLength = (short) (stream.read() << 8 | stream.read());
                    String key = new String(stream.readNBytes(keyLength));

                    short valueLength = (short) (stream.read() << 8 | stream.read());
                    String value = new String(stream.readNBytes(valueLength));

                    module.config.create(
                            key,
                            module.config.getRaw(key).fromString(value)
                    );
                }
            }

            int activeCount = stream.read();
            for(int i = 0; i < activeCount; i++) {
                short nameLength = (short) (stream.read() << 8 | stream.read());
                String name = new String(stream.readNBytes(nameLength));
                Module module = ModuleManager.getByName(name);

                ModuleManager.queueEnable(module);
            }

            Eclipse.log("Loaded client configs!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
