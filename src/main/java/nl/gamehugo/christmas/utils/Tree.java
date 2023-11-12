package nl.gamehugo.christmas.utils;

import nl.gamehugo.christmas.database.Database;

public class Tree {
    private int treeID;
    private long guildID;
    private String name;
    private int size;
    private long lastWatered;

    /**
     * Create a new tree
     *
     * @param guildID The ID of the guild
     * @param name    The name of the tree
     * @param size    The size of the tree
     */
    public Tree(long guildID, String name, int size, long lastWatered) {
        // get highest treeID from database and add 1
        Database database = Database.getInstance();
        this.treeID = database.getTreeDAO().getHighestID() + 1;
        this.guildID = guildID;
        this.name = name;
        this.size = size;
        this.lastWatered = lastWatered;
    }

    /**
     * Create an existing tree
     *
     * @param treeID  The ID of the tree
     * @param guildID The ID of the guild
     * @param name    The name of the tree
     * @param size    The size of the tree
     */
    public Tree(int treeID, long guildID, String name, int size, long lastWatered) {
        this.treeID = treeID;
        this.guildID = guildID;
        this.name = name;
        this.size = size;
        this.lastWatered = lastWatered;
    }

    public int getTreeID() {
        return treeID;
    }

    public void setTreeID(int treeID) {
        this.treeID = treeID;
    }

    public long getGuildID() {
        return guildID;
    }

    public void setGuildID(int guildID) {
        this.guildID = guildID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(long lastWatered) {
        this.lastWatered = lastWatered;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "treeID=" + treeID +
                ", guildID=" + guildID +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", lastWatered=" + lastWatered +
                '}';
    }
}
