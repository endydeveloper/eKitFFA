package me.endydev.ffa.utils;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkullUtils {
    public static Map<String, String> cached;
    public static Map<UUID, String> cachedNames;
    public static Map<UUID, String> cachedTextures;

    private final static Gson GSON = new Gson();

    static {
        cached = new HashMap<>();
        cachedNames = new HashMap<>();
        cachedTextures = new HashMap<>();
    }

    public static String getHeadValue(String name){
        return getValue(name);
    }

    public static String getValue(String name){
        try {
            String result = getURLContent("https://api.mojang.com/users/profiles/minecraft/" + name);
            JsonObject obj = GSON.fromJson(result, JsonObject.class);
            String uid = obj.get("id").toString().replace("\"","");
            String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uid);
            obj = GSON.fromJson(signature, JsonObject.class);
            String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value));
            obj = GSON.fromJson(decoded,JsonObject.class);
            String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
            return new String(Base64.getEncoder().encode(skinByte));
        } catch (Exception ignored){ }
        return null;
    }

    public static CompletableFuture<ItemStack> getHeadOfflineAsync(UUID uuid){
        return CompletableFuture.supplyAsync(() -> getHeadOffline(uuid));
    }

    public static ItemStack getHeadOffline(UUID uuid){
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        String skinTexture = null;
        try {
            String texture = cachedTextures.get(uuid);
            if (texture != null) {
                if(texture.equalsIgnoreCase("not-loaded")) return head;
                skinTexture = texture;
            } else {
                try {
                    String signature = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
                    JsonObject obj = GSON.fromJson(signature, JsonObject.class);
                    String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
                    String decoded = new String(Base64.getDecoder().decode(value));
                    obj = GSON.fromJson(decoded,JsonObject.class);
                    String skinURL = obj.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
                    byte[] skinByte = ("{\"textures\":{\"SKIN\":{\"url\":\"" + skinURL + "\"}}}").getBytes();
                    skinTexture = new String(Base64.getEncoder().encode(skinByte));
                } catch (Exception e) {
                    cachedTextures.put(uuid, "not-loaded");
                }
            }


            if (skinTexture == null) return head;
            cachedTextures.put(uuid, skinTexture);
            return getHead(skinTexture);
        } catch (Exception ignored){ }
        return head;
    }

    private static String getURLContent(String urlStr) {
        URL url;
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try{
            url = new URL(urlStr);
            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8) );
            String str;
            while((str = in.readLine()) != null) {
                sb.append( str );
            }
        } catch (Exception ignored) { }
        finally{
            try{
                if(in!=null) {
                    in.close();
                }
            }catch(IOException ignored) { }
        }
        return sb.toString();
    }

    public static ItemStack getHead(OfflinePlayer player) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        String skinURL = null;
        String texture = cachedTextures.get(player.getUniqueId());
        if (texture != null) {
            if(texture.equalsIgnoreCase("not-loaded")) return head;
            skinURL = texture;
        }else {
            try {
                skinURL = getHeadValue(player.getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("ERROR: Loading Url from "+player.getName());
            }
        }

        if (skinURL == null) return head;
        cachedTextures.put(player.getUniqueId(), skinURL);
        return getHead(skinURL);
    }

    public static ItemStack getHead(String skinURL) {
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", skinURL));
        Field profileField;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static ItemStack getSteveHead() {
        return XMaterial.PLAYER_HEAD.parseItem();
    }


    public static ItemStack getHead(UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        ItemStack head = XMaterial.PLAYER_HEAD.parseItem();
        String skinURL = null;
        String texture = cachedTextures.get(player.getUniqueId());
        if (texture != null) {
            if(texture.equalsIgnoreCase("not-loaded")) return head;
            skinURL = texture;
        }else {
            try {
                skinURL = getHeadValue(player.getName());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("ERROR: Loading Url from UUID: "+uuid);
            }
        }
        if (skinURL == null) return head;
        cachedTextures.put(uuid, skinURL);
        return getHead(skinURL);
    }
}
