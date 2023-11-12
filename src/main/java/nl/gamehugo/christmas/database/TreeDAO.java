package nl.gamehugo.christmas.database;

import nl.gamehugo.christmas.utils.Tree;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TreeDAO {

    private final Database database = Database.getInstance();

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
     * Insert a tree into the database
     *
     * @param tree The tree to insert
     */
    public boolean insertTree(Tree tree) {
        // Check if the connection is open
        if (!database.isConnected()) {
            throw new IllegalStateException("Cannot insert tree because database connection is not open.");
        }
        String sql = "INSERT INTO Tree (TreeID, GuildID, Name, Size, LastWatered) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, tree.getTreeID());
            preparedStatement.setLong(2, tree.getGuildID());
            preparedStatement.setString(3, tree.getName());
            preparedStatement.setInt(4, tree.getSize());
            preparedStatement.setLong(5, tree.getLastWatered());

            preparedStatement.executeUpdate();
            System.out.println("Inserted tree.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error while inserting tree: " + e.getMessage());
        }
        return false;
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
     * Update a tree in the database
     *
     * @param tree The tree to update
     */
    public boolean updateTree(Tree tree) {
        // Check if the connection is open
        if (!database.isConnected()) {
            throw new IllegalStateException("Cannot update tree because the database connection is not open.");
        }

        String sql = "UPDATE Tree SET GuildID = ?, Name = ?, Size = ?, LastWatered = ? WHERE TreeID = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setLong(1, tree.getGuildID());
            preparedStatement.setString(2, tree.getName());
            preparedStatement.setInt(3, tree.getSize());
            preparedStatement.setLong(4, tree.getLastWatered());
            preparedStatement.setInt(5, tree.getTreeID());

            preparedStatement.executeUpdate();
            System.out.println("Updated tree.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error while updating tree: " + e.getMessage());
        }
        return false;
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
