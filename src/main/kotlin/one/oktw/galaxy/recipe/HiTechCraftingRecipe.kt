package one.oktw.galaxy.recipe

import one.oktw.galaxy.Main
import one.oktw.galaxy.galaxy.traveler.data.Traveler
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemType
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.inventory.Slot
import org.spongepowered.api.item.inventory.entity.MainPlayerInventory
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.item.recipe.crafting.Ingredient
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.text.translation.Translatable
import java.util.Arrays.asList


class HiTechCraftingRecipe {
    companion object {
        private val lang = Main.translationService

        class Builder {

            private val recipe = HiTechCraftingRecipe()

            fun add(item: Ingredient, count: Int): Builder {
                recipe.add(item, count)
                return this
            }

            fun cost(price: Int): Builder {
                recipe.price(price)
                return this
            }

            fun result(newResult: ItemType): Builder {
                recipe.setResult(newResult)
                return this
            }

            fun result(newResult: ItemStack): Builder {
                recipe.setResult(newResult)
                return this
            }

            fun result(newResult: ItemStackSnapshot): Builder {
                recipe.setResult(newResult)
                return this
            }

            fun build(): HiTechCraftingRecipe {
                return recipe
            }
        }

        fun builder() = Builder()
    }

    private val ingredientList: ArrayList<Ingredient> = ArrayList()
    private val toMatch: HashMap<Ingredient, Int> = HashMap()
    private var cost: Int = 0
    private var result: ItemStackSnapshot = ItemStackSnapshot.NONE

    private fun add(item: Ingredient, count: Int) {
        if (!ingredientList.contains(item)) {
            ingredientList.add(item)
        }

        toMatch[item] = toMatch[item] ?: 0 + count
    }

    private fun price(price: Int) {
        cost = price
    }

    private fun setResult(newResult: ItemType) {
        setResult(ItemStack.of(newResult, 1))
    }

    private fun setResult(newResult: ItemStack) {
        setResult(newResult.createSnapshot())
    }

    private fun setResult(newResult: ItemStackSnapshot) {
        result = newResult
    }

    fun getCost(): Int {
        return cost
    }

    fun previewRequirement(player: Player): List<ItemStack> {
        val list = ArrayList<ItemStack>()

        for (item in ingredientList) {
            item.displayedItems()[0]?.createStack()
                ?.apply {
                    quantity = toMatch[item] ?: return@apply

                    if (!haveEnoughIngredient(player, item, quantity)) {
                        val originalName = lang.removeStyle(lang.fromItem(this))

                        offer(Keys.DISPLAY_NAME, lang.ofPlaceHolder(TextColors.RED, originalName))
                    }

                    offer(
                        Keys.ITEM_LORE, asList(
                            lang.ofPlaceHolder("UI.Tip.needItemCount", quantity),
                            lang.ofPlaceHolder("UI.Tip.haveItemCount", haveIngredient(player, item))
                        ) as List<Text>
                    )
                }
                ?.let {
                    list += it
                }
        }

        return list
    }

    fun haveEnoughDust(traveler: Traveler): Boolean {
        return traveler.starDust >= cost
    }

    private fun haveIngredient(player: Player, ingredient: Ingredient): Int {
        val inv = player.inventory.query<MainPlayerInventory>(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory::class.java))

        var has = 0

        for (item in inv.slots<Slot>()) {
            item.peek().orElse(null)?.let {
                if (ingredient.test(it)) {
                    has += it.quantity
                }
            }
        }

        return has
    }

    private fun haveEnoughIngredient(player: Player, ingredient: Ingredient, count: Int): Boolean {
        val has = haveIngredient(player, ingredient)

        return has >= count
    }

    fun haveEnoughIngredient(player: Player): Boolean {
        for (item in toMatch) {
            if (!haveEnoughIngredient(player, item.key, item.value)) {
                return false
            }
        }

        return true
    }

    private fun consume(player: Player, ingredient: Ingredient, count: Int) {
        val inv = player.inventory.query<MainPlayerInventory>(QueryOperationTypes.INVENTORY_TYPE.of(MainPlayerInventory::class.java))

        var remain = count

        for (item in inv.slots<Slot>()) {
            item.peek().orElse(null)?.let {
                if (ingredient.test(it)) {
                    if (it.quantity >= remain) {
                        item.poll(remain)
                        remain = 0
                    } else {
                        remain -= it.quantity
                        item.poll()
                    }
                }
            }

            if (remain == 0) {
                break
            }
        }
    }

    fun consume(player: Player, traveler: Traveler): Boolean {
        if (!haveEnoughDust(traveler) || !haveEnoughIngredient(player)) {
            return false
        }

        traveler.takeStarDust(cost)

        for (item in toMatch) {
            consume(player, item.key, item.value)
        }

        return true
    }

    fun result(): ItemStack {
        return result.createStack()
    }

    fun previewResult(player: Player, traveler: Traveler): ItemStack {
        return result.createStack().apply {
            val list = ArrayList<Text>()
            var enough = true

            ingredientList.forEach { ingredient ->
                val count = toMatch[ingredient] ?: return@forEach

                val has = haveIngredient(player, ingredient)
                val item = ingredient.displayedItems()[0]!!

                if (has < count) {
                    enough = false
                }

                list += lang.ofPlaceHolder(
                    if (has < count) {
                        TextColors.RED
                    } else {
                        TextColors.GREEN
                    },
                    "$has / $count ",
                    lang.removeStyle(lang.fromItem(item.createStack()))
                )
            }

            if (traveler.starDust < cost) {
                enough = false
            }

            list += lang.ofPlaceHolder(
                if (traveler.starDust < cost) {
                    TextColors.RED
                } else {
                    TextColors.GREEN
                },
                "${traveler.starDust} / $cost ",
                lang.of("UI.Tip.StarDust")
            )

            offer(Keys.ITEM_LORE, list)

            if (!enough) {
                offer(
                    Keys.DISPLAY_NAME,
                    lang.ofPlaceHolder(TextColors.RED, lang.removeStyle(lang.fromItem(this)))
                )
            }
        }
    }
}
