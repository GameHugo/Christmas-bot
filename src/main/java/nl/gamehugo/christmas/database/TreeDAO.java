package nl.gamehugo.christmas.database;

import nl.gamehugo.christmas.utils.Tree;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TreeDAO {
    private static final String TABLE_NAME = "Tree";
    private static final String COLUMN_TREE_ID = "TreeID";
    private static final String COLUMN_GUILD_ID = "GuildID";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_SIZE = "Size";
    private static final String COLUMN_LAST_WATERED = "LastWatered";

    private final Database database = Database.getInstance();

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Tree (" +
                "TreeID INT NOT NULL, " +
                "GuildID BIGINT NOT NULL, " +
                "Name VARCHAR(255) NOT NULL, " +
                "Size INT NOT NULL, " +
                "LastWatered BIGINT NOT NULL, " +
                "PRIMARY KEY (TreeID)" +
                ")";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while creating Tree table: " + e.getMessage());
        }
    }


    private static final String INSERT_OR_UPDATE_TREE_SQL = "INSERT INTO " + TABLE_NAME + " (" +
            COLUMN_TREE_ID + ", " + COLUMN_GUILD_ID + ", " + COLUMN_NAME + ", " + COLUMN_SIZE + ", " + COLUMN_LAST_WATERED +
            ") VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
            COLUMN_GUILD_ID + " = VALUES(" + COLUMN_GUILD_ID + "), " +
            COLUMN_NAME + " = VALUES(" + COLUMN_NAME + "), " +
            COLUMN_SIZE + " = VALUES(" + COLUMN_SIZE + "), " +
            COLUMN_LAST_WATERED + " = VALUES(" + COLUMN_LAST_WATERED + ")";

    /**
     * Insert or update a tree in the database
     *
     * @param tree The tree to insert or update
     */
    public boolean insertOrUpdateTree(Tree tree) {
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(INSERT_OR_UPDATE_TREE_SQL)) {
            preparedStatement.setInt(1, tree.getTreeID());
            preparedStatement.setLong(2, tree.getGuildID());
            preparedStatement.setString(3, tree.getName());
            preparedStatement.setInt(4, tree.getSize());
            preparedStatement.setLong(5, tree.getLastWatered());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error while inserting or updating tree: " + e.getMessage());
        }
        return false;
    }


    /**
     * Get the highest treeID from the database
     * @return The highest treeID
     */
    public int getHighestID() {
        String sql = "SELECT MAX(TreeID) AS MaxID FROM Tree";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("MaxID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while getting highest treeID: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get a tree from the database
     *
     * @param guildID The ID of the guild
     * @return The tree if found, otherwise if not found it will return null
     */
    public Tree getTreeByGuild(long guildID) {
        String sql = "SELECT * FROM Tree WHERE GuildID = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Tree(resultSet.getInt("TreeID"), resultSet.getLong("GuildID"), resultSet.getString("Name"), resultSet.getInt("Size"), resultSet.getLong("LastWatered"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while getting tree: " + e.getMessage());
        }
        return null;
    }

    /**
     * Delete a tree from the database
     *
     * @param treeID The tree to delete
     */
    public boolean deleteTree(int treeID) {
        if (!database.isConnected()) {
            throw new IllegalStateException("Cannot delete tree because database connection is not open.");
        }
        String sql = "DELETE FROM Tree WHERE TreeID = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, treeID);

            preparedStatement.executeUpdate();
            System.out.println("Deleted tree.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error while deleting tree: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete a tree from the database
     *
     * @param tree The tree to delete
     */
    public boolean deleteTree(Tree tree) {
        return deleteTree(tree.getTreeID());
    }

    /**
     * Check if a tree exists in the database via guildID
     *
     * @param guildID The tree to check
     */
    public boolean treeExistsByGuild(long guildID) {
        String sql = "SELECT * FROM Tree WHERE GuildID = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setLong(1, guildID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Error while checking if tree exists: " + e.getMessage());
        }
        return false;
    }
}
