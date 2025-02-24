package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player

interface RegionHandler {

    /**
     * @param regionId Region id to add
     * @param regionName Region name to add
     * @return True if region was added, false otherwise
     */
    fun addCustomRegion(regionId: String, regionName: String): Boolean

    /**
     * @param player Player to get region name for
     * @return Custom region name or null if not found
     */
    fun getCustomRegionName(player: Player?): String?

    /**
     * @param regionId Region id to remove
     * @return True if region was removed, false otherwise
     */
    fun removeCustomRegion(regionId: String?): Boolean

    /**
     * @return Set of custom region names
     */
    fun getCustomRegionIds(): Set<String>

    /**
     * @param regionId Region name to check
     * @return True if region is custom, false otherwise
     */
    fun isCustomRegion(regionId: String?): Boolean

    /**
     * @param regionId Region id to add to blacklist
     * @return True if region was added to blacklist, false otherwise
     */
    fun addBlacklist(regionId: String): Boolean

    /**
     * @return List of blacklisted region ids
     */
    fun getBlacklist(): List<String>

    /**
     * @param regionId Region id to remove from blacklist
     * @return True if region was removed from blacklist, false otherwise
     */
    fun removeBlacklist(regionId: String?): Boolean

    /**
     * @return True if global region is shown, false otherwise
     */
    fun isGlobalRegion(): Boolean

    /**
     * @param value True to show global region, false to hide
     */
    fun setGlobalRegion(value: Boolean)

}