package one.oktw.galaxy.recipe

import one.oktw.galaxy.Main
import one.oktw.galaxy.block.data.FakeBlockItem
import one.oktw.galaxy.block.enums.CustomBlocks
import one.oktw.galaxy.item.enums.ButtonType
import one.oktw.galaxy.item.enums.MaterialType
import one.oktw.galaxy.item.enums.ToolType
import one.oktw.galaxy.item.type.Button
import one.oktw.galaxy.item.type.Gun
import one.oktw.galaxy.item.type.Material
import one.oktw.galaxy.item.type.Tool
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.recipe.crafting.Ingredient.of
import org.spongepowered.api.text.translation.Translation
import java.util.Arrays.asList

class Recipes {
    companion object {
        private val lang = Main.translationService
        private val lapis = of(ItemStack.of(ItemTypes.DYE, 1).apply { offer(Keys.DYE_COLOR, DyeColors.BLUE) })

        enum class Type {
            ALL,
            TOOL,
            MATERIAL,
            MACHINE,
            WEAPON
        }

        private val materials: List<HiTechCraftingRecipe> = ArrayList<HiTechCraftingRecipe>()

        private val creativeMaterials: List<HiTechCraftingRecipe> = ArrayList<HiTechCraftingRecipe>().apply {
            add(HiTechCraftingRecipe.builder()
                .cost(0)
                .result(Material(MaterialType.PART_RAW_BASE).createItemStack())
                .build()
            )
            add(HiTechCraftingRecipe.builder()
                .cost(0)
                .result(Material(MaterialType.PART_BASE).createItemStack())
                .build()
            )
        }.plus(materials)

        private val tools: List<HiTechCraftingRecipe> = asList(
            HiTechCraftingRecipe.builder()
                .add(of(ItemTypes.IRON_INGOT), 3)
                .add(of(ItemTypes.STICK), 1)
                .cost(0)
                .result(Tool(ToolType.WRENCH).createItemStack())
                .build()
        )

        private val creativeTools = tools

        private val machines: List<HiTechCraftingRecipe> = asList(
            HiTechCraftingRecipe.builder()
                .add(of(ItemTypes.IRON_INGOT), 9)
                .add(of(ItemTypes.ENDER_PEARL), 1)
                .cost(0)
                .result(FakeBlockItem(CustomBlocks.ELEVATOR).createItemStack())
                .build(),

            HiTechCraftingRecipe.builder()
                .add(of(ItemTypes.REDSTONE), 2)
                .add(lapis, 2)
                .add(of(ItemTypes.IRON_INGOT), 2)
                .add(of(ItemTypes.DIAMOND), 1)
                .add(of(ItemTypes.OBSIDIAN), 1)
                .add(of(ItemTypes.CRAFTING_TABLE), 1)
                .cost(0)
                .result(FakeBlockItem(CustomBlocks.HT_CRAFTING_TABLE).createItemStack())
                .build()
        )

        private val creativeMachines = ArrayList<HiTechCraftingRecipe>().apply {
            add(HiTechCraftingRecipe.builder()
                .cost(0)
                .result(FakeBlockItem(CustomBlocks.TELEPORTER).createItemStack())
                .build()
            )

            add(HiTechCraftingRecipe.builder()
                .cost(0)
                .result(FakeBlockItem(CustomBlocks.TELEPORTER_ADVANCED).createItemStack())
                .build()
            )

            add(HiTechCraftingRecipe.builder()
                .cost(0)
                .result(FakeBlockItem(CustomBlocks.TELEPORTER_FRAME).createItemStack())
                .build()
            )
        }.plus(machines)

        private val weapons: List<HiTechCraftingRecipe> = ArrayList()

        private val creativeWeapons = weapons

        private val all: List<HiTechCraftingRecipe> = ArrayList<HiTechCraftingRecipe>().plus(tools).plus(materials).plus(machines).plus(weapons)

        private val creativeAll: List<HiTechCraftingRecipe> = ArrayList<HiTechCraftingRecipe>().plus(creativeTools).plus(creativeMaterials).plus(
            creativeMachines).plus(creativeWeapons)

        val catalog: Map<Type, List<HiTechCraftingRecipe>> = mapOf(
            Type.ALL to all,
            Type.TOOL to tools,
            Type.MATERIAL to materials,
            Type.MACHINE to machines,
            Type.WEAPON to weapons
        )

        val creativeCatalog: Map<Type, List<HiTechCraftingRecipe>> = mapOf(
            Type.ALL to creativeAll,
            Type.TOOL to creativeTools,
            Type.MATERIAL to creativeMaterials,
            Type.MACHINE to creativeMachines,
            Type.WEAPON to creativeWeapons
        )

        val icons: Map<Type, ItemStackSnapshot> = mapOf(
            Type.ALL to Button(ButtonType.ALL).createItemStack().createSnapshot(),
            Type.TOOL to Tool(ToolType.WRENCH).createItemStack().createSnapshot(),
            Type.MATERIAL to Material(MaterialType.PART_RAW_BASE).createItemStack().createSnapshot(),
            Type.MACHINE to FakeBlockItem(CustomBlocks.ELEVATOR).createItemStack().createSnapshot(),
            Type.WEAPON to Gun().createItemStack().createSnapshot()
        )

        val names: Map<Type, Translation> = mapOf(
            Type.ALL to lang.translation("recipe.catalog.ALL"),
            Type.TOOL to lang.translation("recipe.catalog.TOOL"),
            Type.MATERIAL to lang.translation("recipe.catalog.MATERIAL"),
            Type.MACHINE to lang.translation("recipe.catalog.MACHINE"),
            Type.WEAPON to lang.translation("recipe.catalog.WEAPON")
        )

        val types: List<Type> = asList(Type.ALL, Type.MACHINE, Type.TOOL, Type.WEAPON, Type.MATERIAL)
    }
}
