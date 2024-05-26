package me.endydev.ffa.repositories;

import com.google.gson.Gson;
import me.endydev.ffa.api.data.FFAPlayer;
import me.endydev.ffa.api.perks.PerkType;
import me.endydev.ffa.cache.LocalFFAPlayer;
import me.endydev.ffa.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import team.unnamed.inject.Inject;

import java.sql.*;
import java.util.*;

public class FFAPlayerRepository implements IRepository {
    @Inject
    private Database database;

    @Inject
    private Gson gson;

    private String CREATE_DATA_TABLE = "CREATE TABLE IF NOT EXISTS player_data (UUID varchar(64) NOT NULL, name varchar(16) NOT NULL, kills int(20) DEFAULT 0, assists int(20) DEFAULT 0, deaths int(20) DEFAULT 0, coins int(20) DEFAULT 0, level int(10) DEFAULT 1, xp int(20) DEFAULT 0, prestige INT(10) DEFAULT 0, maxks varchar(50) DEFAULT 0, kit varchar(50) DEFAULT NULL, perk1 varchar(50) DEFAULT 'NONE', perk2 varchar(50) DEFAULT 'NONE', perk3 varchar(50) DEFAULT 'NONE');";
    private String CREATE_PERKS_TABLE = "CREATE TABLE IF NOT EXISTS player_perks(uuid varchar(64), perk varchar(64) not null);";


    @Override
    public void createTable() {
        try {
            Connection connection = database.getConnection();
            Statement st = connection.createStatement();
            st.executeUpdate(CREATE_DATA_TABLE);
            st.executeUpdate(CREATE_PERKS_TABLE);
            database.close(connection, st, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Set<PerkType> getPerks(UUID uuid) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultSet = null;

        Set<PerkType> perks = new HashSet<>();

        try {
            connection = database.getConnection();
            select = connection.prepareStatement("SELECT uuid, perk FROM player_perks WHERE uuid = ?;");
            select.setString(1, uuid.toString());
            resultSet = select.executeQuery();

            while (resultSet.next()) {
                perks.add(PerkType.valueOf(resultSet.getString("perk").toUpperCase()));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, resultSet);
        }

        return perks;
    }

    public boolean hasPerk(UUID uuid, PerkType perk) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultSet = null;

        try {
            connection = database.getConnection();
            select = connection.prepareStatement("SELECT EXISTS(SELECT 1 FROM player_perks WHERE uuid = ? AND perk = ?);");
            select.setString(1, uuid.toString());
            select.setString(2, perk.toString());
            resultSet = select.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, resultSet);
        }

        return false;
    }

    public void addPerk(UUID uuid, PerkType perk) {
        Connection connection = null;
        PreparedStatement insert = null;

        try {
            connection = database.getConnection();
            insert = connection.prepareStatement("INSERT INTO player_perks (uuid, perk) VALUES (?,?);");
            insert.setString(1, uuid.toString());
            insert.setString(2, perk.toString());
            insert.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, insert, null);
        }
    }

    public void removePerk(UUID uuid, PerkType type) {
        Connection connection = null;
        PreparedStatement insert = null;

        try {
            connection = database.getConnection();
            insert = connection.prepareStatement("DELETE FROM player_perks WHERE uuid = ? AND perk = ?;");
            insert.setString(1, uuid.toString());
            insert.setString(2, type.toString());
            insert.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, insert, null);
        }
    }

    public void savePlayer(FFAPlayer data) {
        Connection connection = null;
        PreparedStatement update = null;

        try {
            connection = database.getConnection();

            update = connection.prepareStatement("UPDATE player_data SET maxks = ?, level = ?, kills = ?, deaths = ?, assists = ?, kit = ?, perk1 = ?, perk2 = ?, perk3 = ?, coins = ?, xp = ?, prestige = ? WHERE UUID = ?;");
            update.setInt(1, data.getMaxKs());
            update.setInt(2, data.getLevel());
            update.setInt(3, data.getKills());
            update.setInt(4, data.getDeaths());
            update.setInt(5, data.getAssists());
            update.setString(6, data.getSelectedKit());
            update.setString(7, data.getPerk1().toString());
            update.setString(8, data.getPerk2().toString());
            update.setString(9, data.getPerk3().toString());
            update.setInt(10, data.getCoins());
            update.setDouble(11, data.getXP());
            update.setInt(12, data.getPrestige());
            update.setString(13, data.getUniqueId().toString());

            update.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, update, null);
        }
    }

    public List<FFAPlayer> getTopCoins(int amount) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;

        List<FFAPlayer> FFAPlayerList = new ArrayList<>();
        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data ORDER BY coins DESC LIMIT %s", amount));
            rs = select.executeQuery();

            while (rs.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
                if (player == null || player.isOp() || player.isBanned()) {
                    continue;
                }
                //if(Arrays.asList("desarrollador", "potsg_", "emojiado", "zelika777").contains(rs.getString("name").toLowerCase())) continue;

                FFAPlayerList.add(parsePlayerData(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, rs);
        }

        return FFAPlayerList;
    }

    public List<FFAPlayer> getTopKillers(int amount) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;

        List<FFAPlayer> FFAPlayerList = new ArrayList<>();
        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data ORDER BY kills DESC LIMIT %s", amount));
            rs = select.executeQuery();

            while (rs.next()) {
                FFAPlayerList.add(parsePlayerData(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, rs);
        }

        return FFAPlayerList;
    }

    public List<FFAPlayer> getTopDeaths(int amount) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;

        List<FFAPlayer> FFAPlayerList = new ArrayList<>();
        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data ORDER BY deaths DESC LIMIT %s", amount));
            rs = select.executeQuery();

            while (rs.next()) {
                FFAPlayerList.add(parsePlayerData(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, rs);
        }

        return FFAPlayerList;
    }

    public FFAPlayer getPlayer(UUID uuid) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;
        FFAPlayer ffaPlayer = null;

        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data WHERE UUID = '%s'", uuid.toString()));
            rs = select.executeQuery();

            if(rs.next()) {
                int kills = rs.getInt("kills");
                int deaths = rs.getInt("deaths");
                int assists = rs.getInt("assists");
                int coins = rs.getInt("coins");
                int level = rs.getInt("level");
                int prestige = rs.getInt("prestige");
                int xp = rs.getInt("xp");
                int maxKs = rs.getInt("maxks");
                String kit = rs.getString("kit");
                PerkType perk1 = PerkType.from(rs.getString("perk1"));
                PerkType perk2 = PerkType.from(rs.getString("perk2"));
                PerkType perk3 = PerkType.from(rs.getString("perk3"));

                ffaPlayer = LocalFFAPlayer.of(uuid, rs.getString("name"), kills, deaths, assists, coins, level, prestige, xp, 0, maxKs, kit, perk1, perk2, perk3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.close(connection, select, rs);
        }

        return ffaPlayer;
    }

    public FFAPlayer createPlayer(Player player) {
        Connection connection = null;
        PreparedStatement select = null;
        PreparedStatement insert = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        FFAPlayer ffaPlayer = null;

        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data WHERE UUID = '%s'", player.getUniqueId().toString()));
            rs = select.executeQuery();

            int kills = 0;
            int deaths = 0;
            int assists = 0;
            int coins = 0;
            int level = 1;
            int prestige = 0;
            double xp = 0.0;
            int maxKs = 0;
            String kit = null;
            PerkType perk1 = PerkType.NONE;
            PerkType perk2 = PerkType.NONE;
            PerkType perk3 = PerkType.NONE;

            if (!rs.next()) {
                insert = connection.prepareStatement(String.format("INSERT INTO player_data(UUID, name) VALUES('%s', '%s');", player.getUniqueId().toString(), player.getName()));
                insert.execute();
            } else {
                update = connection.prepareStatement(String.format("UPDATE player_data SET name = '%s' WHERE UUID = '%s';", player.getName(), player.getUniqueId().toString()));
                update.execute();

                kills = rs.getInt("kills");
                deaths = rs.getInt("deaths");
                assists = rs.getInt("assists");
                coins = rs.getInt("coins");
                level = rs.getInt("level");
                prestige = rs.getInt("prestige");
                xp = rs.getDouble("xp");
                maxKs = rs.getInt("maxks");
                kit = rs.getString("kit");
                perk1 = PerkType.from(rs.getString("perk1"));
                perk2 = PerkType.from(rs.getString("perk2"));
                perk3 = PerkType.from(rs.getString("perk3"));

            }

            ffaPlayer = LocalFFAPlayer.of(player.getUniqueId(), player.getName(), kills, deaths, assists, coins, level, prestige, xp, 0, maxKs, kit, perk1, perk2, perk3);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.close(connection, select, null);
            database.close(null, insert, rs);
            database.close(null, update, null);
        }

        return ffaPlayer;
    }

    public List<FFAPlayer> getTopLevel(int amount) {
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet rs = null;

        List<FFAPlayer> FFAPlayerList = new ArrayList<>();
        try {
            connection = database.getConnection();
            select = connection.prepareStatement(String.format("SELECT * FROM player_data ORDER BY level DESC LIMIT %s", amount));
            rs = select.executeQuery();

            while (rs.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUID")));
                if (player == null || player.isOp() || player.isBanned()) continue;

                FFAPlayerList.add(parsePlayerData(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            database.close(connection, select, rs);
        }

        return FFAPlayerList;
    }

    private FFAPlayer parsePlayerData(ResultSet rs) throws SQLException {
        UUID uuid = UUID.fromString(rs.getString("UUID"));
        String name = rs.getString("name");
        int kills = rs.getInt("kills");
        int deaths = rs.getInt("deaths");
        int assists = rs.getInt("assists");
        int coins = rs.getInt("coins");
        int level = rs.getInt("level");
        int prestige = rs.getInt("prestige");
        int xp = rs.getInt("xp");
        int maxKs = rs.getInt("maxks");
        String kit = rs.getString("kit");
        PerkType perk1 = PerkType.from(rs.getString("perk1"));
        PerkType perk2 = PerkType.from(rs.getString("perk2"));
        PerkType perk3 = PerkType.from(rs.getString("perk3"));

        return LocalFFAPlayer.of(uuid, name, kills, deaths, assists, coins, level, prestige, xp, 0, maxKs, kit, perk1, perk2, perk3);
    }
}
