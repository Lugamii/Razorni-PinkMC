package dev.razorni.crates.crate.menu.editor.buttons;

import dev.razorni.crates.crate.Crate;
import cc.invictusgames.ilib.builder.ItemBuilder;
import cc.invictusgames.ilib.chatinput.ChatInput;
import cc.invictusgames.ilib.menu.Button;
import cc.invictusgames.ilib.utils.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@RequiredArgsConstructor
public class CrateSetDisplayNameButton extends Button {

    private final Crate crate;

    @Override
    public ItemStack getItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG)
                .setDisplayName(crate.getDisplayName())
                .setLore(Arrays.asList(
                        CC.MENU_BAR,
                        ChatColor.WHITE + "Click to change the displayname of the crate.",
                        CC.MENU_BAR)
                ).build();
    }

    @Override
    public void click(Player whoClicked, int slot, ClickType clickType, int hotbarButton) {
        whoClicked.closeInventory();

        new ChatInput<String>(String.class)
                .text(CC.GREEN + "Insert a new displayname.")
                .accept((player, s) -> {
                    crate.setDisplayName(ChatColor.translateAlternateColorCodes('&', s));

                    player.sendMessage(ChatColor.GREEN + "You set the display name to "
                            + crate.getDisplayName() + ChatColor.GREEN + ".");
                    return true;
                }).send(whoClicked);
    }
}
