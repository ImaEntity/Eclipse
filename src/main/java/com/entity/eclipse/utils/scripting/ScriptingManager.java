package com.entity.eclipse.utils.scripting;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.utils.events.Events;
import com.entity.eclipse.utils.events.block.BlockEvents;
import com.entity.eclipse.utils.events.lore.LoreEvents;
import com.entity.eclipse.utils.events.packet.PacketEvents;
import com.entity.eclipse.utils.events.render.RenderEvents;
import com.entity.eclipse.utils.events.tick.TickEvents;
import com.entity.eclipse.utils.scripting.wrappers.ConfigWrapper;
import com.entity.eclipse.utils.scripting.wrappers.EclipseWrapper;
import com.entity.eclipse.utils.scripting.wrappers.ModuleManagerWrapper;
import com.entity.eclipse.utils.scripting.wrappers.ModuleTypeWrapper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptingManager {
    public static Context createEngine() {
        return Context.enter();
    }

    public static Scriptable createScope(Context engine) {
        Scriptable scope = engine.initStandardObjects();

        ScriptableObject.putProperty(scope, "Eclipse", Context.javaToJS(new EclipseWrapper(), scope));

        ScriptableObject.putProperty(scope, "ModuleType", Context.javaToJS(new ModuleTypeWrapper(), scope));
        ScriptableObject.putProperty(scope, "ModuleManager", Context.javaToJS(new ModuleManagerWrapper(), scope));

        ScriptableObject.putProperty(scope, "Events", Context.javaToJS(Events.class, scope));
        ScriptableObject.putProperty(scope, "PacketEvents", Context.javaToJS(PacketEvents.class, scope));
        ScriptableObject.putProperty(scope, "TickEvents", Context.javaToJS(TickEvents.class, scope));
        ScriptableObject.putProperty(scope, "RenderEvents", Context.javaToJS(RenderEvents.class, scope));
        ScriptableObject.putProperty(scope, "LoreEvents", Context.javaToJS(LoreEvents.class, scope));
        ScriptableObject.putProperty(scope, "BlockEvents", Context.javaToJS(BlockEvents.class, scope));

        ScriptableObject.putProperty(scope, "Config", Context.javaToJS(new ConfigWrapper(), scope));

        return scope;
    }

    public static void loadScripts(Context engine, Scriptable scope) {
        Path dir = Paths.get(Eclipse.client.runDirectory.getAbsolutePath() + "/." + Eclipse.MOD_ID + "/scripts");
        if(!Files.exists(dir)) {
            try { Files.createDirectories(dir); } catch(Exception e) { e.printStackTrace(); }
            return;
        }

        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
            for(Path path : stream) {
                Reader script = Files.newBufferedReader(path);

                engine.evaluateReader(
                        scope,
                        script,
                        path.getFileName().toString(),
                        1,
                        null
                );

                Eclipse.log(String.format("Loaded script `%s`", path.getFileName()));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
