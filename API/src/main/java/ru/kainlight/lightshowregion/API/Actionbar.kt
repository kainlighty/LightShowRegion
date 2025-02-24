package ru.kainlight.lightshowregion.API

interface Actionbar {

    var isActive: Boolean

    /**
     * Toggles actionbar visibility
     * @return true if actionbar was hidden, false if actionbar was shown
     */
    fun toggle(): Boolean
    /**
     * Hides actionbar
     * @return true if actionbar was hidden, false if actionbar was already hidden
     */
    fun hide(): Boolean
    /**
     * Shows actionbar
     */
    fun show()

}