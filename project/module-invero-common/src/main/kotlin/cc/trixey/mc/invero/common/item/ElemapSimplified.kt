package cc.trixey.mc.invero.common.item

import cc.trixey.mc.invero.common.Elemap
import cc.trixey.mc.invero.common.Element
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.panel.PanelInstance
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Arasple
 * @since 2022/11/22 11:26
 *
 * 简单的元素集
 * 仅支持静态元素（ElementAbsolute）
 */
open class ElemapSimplified(override val panel: Panel) : Elemap {

    internal val absoluteElements = ConcurrentHashMap<Int, ElementAbsolute>()

    override fun getOccupiedSlots(): Set<Int> {
        return absoluteElements.keys
    }

    override fun has(element: Element): Boolean {
        return absoluteElements.containsValue(element)
    }

    override fun remove(slot: Int) {
        absoluteElements.remove(slot)
    }

    override fun remove(element: Element) = find(element).forEach { remove(it) }

    override operator fun get(slot: Int): Element? = absoluteElements[slot]

    override operator fun set(slot: Int, element: Element) {
        if (element !is ElementAbsolute) return
        absoluteElements[slot] = element
    }

    override fun add(slot: Int, element: Element) {
        if (element !is ElementAbsolute) return
        absoluteElements[slot] = element
    }

    override fun find(element: Element): Set<Int> {
        if (element !is ElementAbsolute) return setOf()
        return absoluteElements.filterValues { it == element }.keys
    }

    override fun forEach(function: (Element) -> Unit) {
        absoluteElements.values.distinct().forEach(function)
    }

    override fun forEachSloted(function: (element: Element, slots: Set<Int>) -> Unit) {
        absoluteElements.values.distinct().forEach { function(it, find(it)) }
    }

    override fun clear() {
        absoluteElements.clear()
    }

    /*
    拓展函数
     */

    fun findUpperSlots(panel: PanelInstance, element: Element): Set<Int> {
        panel.apply {
            return find(element).mapNotNull {
                val v = it.toUpperSlot()
                if (v != null && v < 0) null
                else v
            }.toSet()
        }
    }

}