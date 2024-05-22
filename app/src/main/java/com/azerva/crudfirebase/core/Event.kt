package com.azerva.crudfirebase.core

/**
 * Clase genérica para representar un evento que puede ser observado.
 *
 * @param T El tipo de contenido del evento.
 * @property content El contenido del evento.
 * @property hasBeenHandled Indica si el evento ya ha sido manejado o no.
 * @constructor Crea una instancia de la clase Event con el contenido proporcionado.
 */
open class Event<out T>(private val content: T) {

    /**
     * Indica si el evento ya ha sido manejado.
     */
    private var hasBeenHandled = false
        private set

    /**
     * Devuelve el contenido del evento si aún no ha sido manejado, de lo contrario, devuelve null.
     *
     * @return El contenido del evento si aún no ha sido manejado, o null si ya ha sido manejado.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Devuelve el contenido del evento, independientemente de si ha sido manejado o no.
     *
     * @return El contenido del evento.
     */
    fun getContent(): T? {
        return content
    }

    /**
     * Obtiene el contenido del evento sin marcarlo como manejado.
     *
     * @return El contenido del evento.
     */
    fun peekContent(): T = content
}