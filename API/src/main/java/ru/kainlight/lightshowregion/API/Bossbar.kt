package ru.kainlight.lightshowregion.API

interface Bossbar {

    var isActive: Boolean

    /**
     * @return True if the bossbar was hidden, false if it was shown
     */
    fun toggle(): Boolean
    /**
     * @return True if the bossbar was hidden, false if it was already hidden
     */
    fun hide(): Boolean
    /**
     * Shows the bossbar
     */
    fun show()
}