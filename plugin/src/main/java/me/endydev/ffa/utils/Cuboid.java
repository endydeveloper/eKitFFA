package me.endydev.ffa.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class Cuboid {
    private String worldName;

    private int minX;

    private int maxX;

    private int minY;

    private int maxY;

    private int minZ;

    private int maxZ;

    public String getWorldName() {
        return this.worldName;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

    public Cuboid() {}

    public Cuboid(Location loc1, Location loc2) {
        this.worldName = loc1.getWorld().getName();
        this.minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        this.maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        this.minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        this.maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        this.minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        this.maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public Location getCenter() {
        int x2 = this.maxX + 1;
        int y2 = this.maxY + 1;
        int z2 = this.maxZ + 1;
        return new Location(getWorld(), this.minX + (x2 - this.minX) / 2.0D, this.minY + (y2 - this.minY) / 2.0D, this.minZ + (z2 - this.minZ) / 2.0D);
    }

    public Location getCenterWithMinY() {
        int x2 = this.maxX + 1;
        int y2 = this.maxY + 1;
        int z2 = this.maxZ + 1;
        return new Location(getWorld(), this.minX + (x2 - this.minX) / 2.0D, this.minY, this.minZ + (z2 - this.minZ) / 2.0D);
    }

    public World getWorld() {
        return Bukkit.getWorld(this.worldName);
    }

    private boolean contains(World world, int x, int y, int z) {
        return (world.getName().equals(this.worldName) && x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ);
    }

    public boolean contains(Location loc) {
        return contains(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public int[] getFirstLocation() {
        return new int[] { this.minX, this.minY, this.minZ };
    }

    public int[] getSecondLocation() {
        return new int[] { this.maxX, this.maxY, this.maxZ };
    }

    public Stream<? extends Player> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().filter(player -> contains(player.getLocation()));
    }
}
