package ru.kainlight.lightshowregion.API

import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus
import ru.kainlight.lightshowregion.API.exceptions.ProviderException

interface LightShowRegionAPI {

    companion object {
        private var provider: LightShowRegionAPI? = null

        @ApiStatus.Internal
        fun setProvider(value: LightShowRegionAPI) {
            if(provider == null) provider = value
            else throw ProviderException("The provider has already been assigned")
        }

        @ApiStatus.Internal
        fun removeProvider() {
            provider = null
        }

        /**
         * Get the provider instance.
         *
         * @return The provider instance.
         * @throws ProviderException If the provider is not assigned by the parent plugin.
         */
        @JvmStatic
        fun getProvider(): LightShowRegionAPI {
            return provider ?: throw ProviderException("The provider is not assigned by the parent plugin")
        }
    }

    /**
     * @param player Player to create a ShowedPlayer for
     * @return Created ShowedPlayer
     */
    fun createShowedPlayer(player: Player): ShowedPlayer

    /**
     * @param player Player to delete ShowedPlayer for
     * @return True if the ShowedPlayer was deleted, false otherwise
     */
    fun deleteShowedPlayer(player: Player): Boolean

    /**
     * @param showedPlayer ShowedPlayer to delete
     * @return True if the ShowedPlayer was deleted, false otherwise
     */
    fun deleteShowedPlayer(showedPlayer: ShowedPlayer): Boolean

    /**
     * @param player Player to get ShowedPlayer for
     * @return ShowedPlayer for the player or null if not found
     */
    fun getShowedPlayer(player: Player): ShowedPlayer?

    /**
     * @param player Player to get or create ShowedPlayer for
     * @return ShowedPlayer for the player
     */
    fun getOrCreateShowedPlayer(player: Player): ShowedPlayer

    /**
     * @return List of all ShowedPlayers
     */
    fun getShowedPlayers(): List<ShowedPlayer>

    /**
     * @param player Player to check
     * @return True if the ShowedPlayer exists, false otherwise
     */
    fun isShowedPlayer(player: Player?): Boolean

    /**
     * @param player Player to reload actionbar for
     */
    fun reloadActionbar(player: Player)

    /**
     * Reloads actionbars for all players
     */
    fun reloadActionbars()

    /**
     * Unloads actionbars for all players
     */
    fun unloadActionbars()

    /**
     * @param player Player to reload bossbar for
     */
    fun reloadBossbar(player: Player)

    /**
     * Reload bossbar for all players
     */
    fun reloadBossbars()

    /**
     * Unloads bossbar for all players
     */
    fun unloadBossbars()

    /**
     * @param world World to add to disabled worlds
     * @return True if world was added to disabled worlds, false otherwise
     */
    fun addDisabledWorld(world: String): Boolean

    /**
     * @return List of disabled worlds
     */
    fun getDisabledWorlds(): List<String>

    /**
     * @param world World to
     * @return True if world was removed from disabled worlds, false otherwise
     */
    fun removeDisabledWorld(world: String?): Boolean

    /**
     * @return RegionHandler instance
     */
    fun getRegionHandler(): RegionHandler

}