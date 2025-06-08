package com.entity.eclipse.commands;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.commands.base.Command;
import com.entity.eclipse.utils.UpdateManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.net.URI;

public class Update extends Command {
    public Update() {
        super("Update", "Checks for client updates.", "update");
    }

    @Override
    public void onExecute(String[] args) {
        UpdateManager.Update latest = UpdateManager.getLatest();
        if(latest.version().equalsIgnoreCase(Eclipse.VERSION)) {
            Eclipse.notifyUser("Latest version: " + latest.version());
            Eclipse.notifyUser("You're all good!");
            return;
        }

        Eclipse.notifyUser("New version available: " + latest.version());
        Eclipse.notifyUserRaw(Text.literal("")
                .append(Text.literal("Click "))
                .append(Text.literal("here")
                        .setStyle(Style.EMPTY
                                .withColor(Formatting.GOLD)
                                .withUnderline(true)
                                .withClickEvent(new ClickEvent.OpenUrl(URI.create(String.format(
                                        "https://github.com/ImaEntity/Eclipse/releases/download/%s/%s",
                                        latest.version(),
                                        latest.jarFileName()
                                ))))
                                .withHoverEvent(new HoverEvent.ShowText(
                                        Text.of("Download " + latest.jarFileName() + "?")
                                ))
                        )
                )
                .append(Text.of(" to download the latest update!"))
        );
    }
}
