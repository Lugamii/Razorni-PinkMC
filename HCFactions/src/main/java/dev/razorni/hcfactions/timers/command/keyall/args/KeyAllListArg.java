package dev.razorni.hcfactions.timers.command.keyall.args;

import dev.razorni.hcfactions.commands.CommandManager;
import dev.razorni.hcfactions.extras.framework.commands.Argument;
import dev.razorni.hcfactions.timers.type.CustomTimer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class KeyAllListArg extends Argument {
    public KeyAllListArg(CommandManager manager) {
        super(manager, Arrays.asList("names", "list"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (String s : this.getLanguageConfig().getStringList("KEY_ALL_COMMAND.KEY_ALL_LIST.TIMERS_LIST")) {
            if (!s.equalsIgnoreCase("%timers%")) {
                this.sendMessage(sender, s);
            } else {
                for (CustomTimer timer : this.getInstance().getTimerManager().getCustomTimers().values()) {
                    this.sendMessage(sender, this.getLanguageConfig().getString("KEY_ALL_COMMAND.KEY_ALL_LIST.TIMERS_FORMAT").replaceAll("%name%", timer.getName()).replaceAll("%remaining%", timer.getRemainingString()).replaceAll("%displayName%", timer.getDisplayName()));
                }
            }
        }
    }

    @Override
    public String usage() {
        return null;
    }
}
