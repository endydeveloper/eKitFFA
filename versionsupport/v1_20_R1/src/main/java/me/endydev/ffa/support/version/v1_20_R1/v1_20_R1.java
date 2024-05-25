package me.endydev.ffa.support.version.v1_20_R1;

import com.mojang.datafixers.util.Pair;
import me.endydev.ffa.api.version.VersionSupport;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutUpdateHealth;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class v1_20_R1 extends VersionSupport {
    public v1_20_R1(Plugin plugin, String name) {
        super(plugin, name);
    }

    public EntityPlayer getPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    @Override
    public void voidKill(Player p) {
        EntityPlayer player = getPlayer(p);
        player.a(player.dJ().l(), 1000);
    }

    @Override
    public void hideArmor(@NotNull Player victim, Player receiver) {
        List<Pair<EnumItemSlot, ItemStack>> items = new ArrayList<>();
        items.add(new Pair<>(EnumItemSlot.f, new ItemStack(Item.b(0))));
        items.add(new Pair<>(EnumItemSlot.e, new ItemStack(Item.b(0))));
        items.add(new Pair<>(EnumItemSlot.d, new ItemStack(Item.b(0))));
        items.add(new Pair<>(EnumItemSlot.c, new ItemStack(Item.b(0))));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        sendPacket(receiver, packet1);
    }

    @Override
    public void showArmor(@NotNull Player victim, Player receiver) {
        List<Pair<EnumItemSlot, ItemStack>> items = new ArrayList<>();
        items.add(new Pair<>(EnumItemSlot.f, CraftItemStack.asNMSCopy(victim.getInventory().getHelmet())));
        items.add(new Pair<>(EnumItemSlot.e, CraftItemStack.asNMSCopy(victim.getInventory().getChestplate())));
        items.add(new Pair<>(EnumItemSlot.d, CraftItemStack.asNMSCopy(victim.getInventory().getLeggings())));
        items.add(new Pair<>(EnumItemSlot.c, CraftItemStack.asNMSCopy(victim.getInventory().getBoots())));
        PacketPlayOutEntityEquipment packet1 = new PacketPlayOutEntityEquipment(victim.getEntityId(), items);
        sendPacket(receiver, packet1);
    }

    @Override
    public void onUseGoldenHead(Player p) {
        CraftPlayer nmsPlayer = (CraftPlayer) p;
        sendPacket(p, new PacketPlayOutUpdateHealth(nmsPlayer.getScaledHealth(), p.getFoodLevel(), p.getSaturation() + 2));
    }

    @Override
    public void respawnPlayer(Player player) {
        if (!player.isDead()) {
            return;
        }

        Player.Spigot spigot = player.spigot();
        spigot.respawn();
    }

    @Override
    public void clearArrowsFromPlayerBody(Player player) {

    }

    @Override
    public void sendTitle(@NotNull Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        p.sendTitle(title == null ? " " : title, subtitle == null ? " " : subtitle, fadeIn, stay, fadeOut);
    }

    @Override
    public void playAction(@NotNull Player p, String text) {
        p.spigot().sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(ChatColor.translateAlternateColorCodes('&', text)
                )
        );
    }

    @Override
    public int getVersion() {
        return 9;
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer) player).getHandle().c.a(packet);
    }

    private void sendPackets(Player player, Packet<?> @NotNull ... packets) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().c;
        for (Packet<?> p : packets) {
            connection.a(p);
        }
    }
}
