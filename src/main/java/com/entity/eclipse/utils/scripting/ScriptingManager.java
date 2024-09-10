package com.entity.eclipse.utils.scripting;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.modules.Module;
import com.entity.eclipse.modules.ModuleManager;
import com.entity.eclipse.utils.scripting.events.*;
import com.entity.eclipse.utils.scripting.module.ModuleTypeWrapper;
import com.entity.eclipse.utils.scripting.module.ModuleWrapper;
import com.entity.eclipse.utils.types.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptingManager {
    public static Context createEngine(String langID) {
        return Context.newBuilder(langID)
                .allowHostAccess(HostAccess.ALL)
                .allowHostClassLookup(s -> true)
                .build();
    }

    public static void loadBindings(Context engine, String langID) {
        Value bindings = engine.getBindings(langID);

        bindings.putMember("Eclipse", new EclipseWrapper());
        bindings.putMember("ModuleType", new ModuleTypeWrapper());

        bindings.putMember("Events", new EventsWrapper());
        bindings.putMember("PacketEvents", new PacketEventsWrapper());
        bindings.putMember("TickEvents", new TickEventsWrapper());
        bindings.putMember("RenderEvents", new RenderEventsWrapper());
        bindings.putMember("LoreEvents", new LoreEventsWrapper());
        bindings.putMember("BlockEvents", new BlockEventsWrapper());

        bindings.putMember("BlockValue", BlockValue.class);
        bindings.putMember("BooleanValue", BooleanValue.class);
        bindings.putMember("ByteValue", ByteValue.class);
        bindings.putMember("CharacterValue", CharacterValue.class);
        bindings.putMember("DoubleValue", DoubleValue.class);
        bindings.putMember("EntityTypeValue", EntityTypeValue.class);
        bindings.putMember("FloatValue", FloatValue.class);
        bindings.putMember("IntegerValue", IntegerValue.class);
        bindings.putMember("ItemValue", ItemValue.class);
        bindings.putMember("ListValue", ListValue.class);
        bindings.putMember("LongValue", LongValue.class);
        bindings.putMember("ShortValue", ShortValue.class);
        bindings.putMember("StringValue", StringValue.class);
    }

    public static void loadScripts(Context engine, String landID) {
        Path dir = Paths.get(Eclipse.client.runDirectory.getAbsolutePath() + "/." + Eclipse.MOD_ID + "/scripts");
        if(!Files.exists(dir)) {
            try { Files.createDirectory(dir); } catch(Exception e) { e.printStackTrace(); }
            return;
        }

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            for(Path path : stream) {
                String script = Files.readString(path);
                Value output = engine.eval(landID, script);

                if(output == null) {
                    Eclipse.log(String.format("Script `%s` didn't return anything!", path.getFileName()));
                    continue;
                }

                Module module = new ModuleWrapper(output);

                ModuleManager.appendExternal(module);
                Eclipse.log(String.format("Loaded script `%s`", path.getFileName()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
