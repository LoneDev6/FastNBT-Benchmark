package dev.lone.fastnbt_benchmark.tests;

import dev.lone.LoneLibs.nbt.nbtapi.*;
import dev.lone.fastnbt.nms.nbt.NCompound;
import dev.lone.fastnbt.nms.nbt.NItem;
import dev.lone.fastnbt.nms.nbt.NTagList;
import dev.lone.fastnbt.nms.nbt.NbtType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Test
{
    public static void run()
    {
        try
        {
            Benchmark b = new Benchmark();
            b.init();
            for (int i = 0; i < 5_000; i++)
            {
                fastNbtTest();
            }
            b.elapsed();
            System.out.println("FastNbt: " + b.elapsed());

            b = new Benchmark();
            b.init();
            for (int i = 0; i < 5_000; i++)
            {
                nbtApiTest();
            }
            System.out.println("NBT API: " + b.elapsed());
        }
        catch (RuntimeException ex)
        {
            Bukkit.getLogger().severe("Failed NBT tests!");
            ex.printStackTrace();
        }
    }

    public static void fastNbtTest0() throws RuntimeException
    {
        ItemStack item = new ItemStack(Material.STONE);

        NItem nItem = new NItem(item);

        nItem.setString("str", "sus");
        nItem.setIntegerList("intlist", List.of(1,2,3,4,5,6,7,8,9,0));
        nItem.setLongList("longlist", List.of(1L,2L,3L));
        NTagList<?> compoundList = nItem.getOrAddList("compound_list", NbtType.Compound);
        {
            NCompound<?> entry = new NCompound<>();
            entry.setInt("prop1", 1337);
            compoundList.add(entry);
        }

        nItem.setDisplayName("{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}");
        nItem.setLore(List.of("{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}", "{\"text\":\"Tunic of Destiny\",\"color\":\"red\"}"));

        nItem.setAttributeModifier(
                "minecraft:generic.movement_speed",
                1,
                6,
                "bro",
                "mainhand",
                1337,
                1337
        );

        nItem.setEnchantment("minecraft:sharpness", (short) 10);

        nItem.save();

        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        String displayName = meta.getDisplayName();
        List<String> lore = meta.getLore();

        if(!ChatColor.stripColor(displayName).equals("Tunic of Destiny"))
            throw new RuntimeException();

        assert lore != null;
        for (String ss : lore)
        {
            if(!ChatColor.stripColor(ss).equals("Tunic of Destiny"))
                throw new RuntimeException();
        }

        if(meta.getAttributeModifiers() == null || meta.getAttributeModifiers().isEmpty())
            throw new RuntimeException();

        meta.getAttributeModifiers().forEach((attribute, attributeModifier) -> {
            if(!attribute.getKey().toString().equals("minecraft:generic.movement_speed"))
                throw new RuntimeException();
            if(!attributeModifier.getName().equals("bro"))
                throw new RuntimeException();
        });

        if(meta.getEnchantLevel(Enchantment.DAMAGE_ALL) != 10)
            throw new RuntimeException();


        nItem.removeEnchantment("minecraft:sharpness");
        nItem.save();
        meta = item.getItemMeta();

        if(meta.getEnchantLevel(Enchantment.DAMAGE_ALL) == 10)
            throw new RuntimeException();


        HashMap<String, Short> map = new HashMap<>();
        map.put("minecraft:sharpness", (short) 32);
        map.put("minecraft:flame", (short) 66);
        nItem.addEnchantments(map);

        nItem.save();
        meta = item.getItemMeta();

        if(meta.getEnchantLevel(Enchantment.DAMAGE_ALL) != 32)
            throw new RuntimeException();
        if(meta.getEnchantLevel(Enchantment.ARROW_FIRE) != 66)
            throw new RuntimeException();


        NCompound compound = new NCompound();
        compound.setBoolean("test_put_tag", true);

        nItem.putTag("this_is_a_put_tag", compound.getInternal());
        nItem.save();


        Optional<? extends Player> player = Bukkit.getOnlinePlayers().stream().findFirst();
        if(player.isPresent())
        {
            player.get().getInventory().addItem(item.clone());

            nItem.setSkull("amogus", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc4ZWYyZTRjZjJjNDFhMmQxNGJmZGU5Y2FmZjEwMjE5ZjViMWJmNWIzNWE0OWViNTFjNjQ2Nzg4MmNiNWYwIn19fQ==");
            nItem.save();
            player.get().getInventory().addItem(item.clone());
        }

        Bukkit.getLogger().info("");
    }

    public static void fastNbtTest() throws RuntimeException
    {
        ItemStack item = new ItemStack(Material.STONE);

        NItem nItem = new NItem(item);

        nItem.setString("str", "sus");
        nItem.setIntegerList("intlist", List.of(1,2,3,4,5,6,7,8,9,0));
        nItem.setLongList("longlist", List.of(1L,2L,3L));
        NTagList<?> compoundList = nItem.getOrAddList("compound_list", NbtType.Compound);
        {
            NCompound<?> entry = new NCompound<>();
            entry.setInt("prop1", 1337);
            compoundList.add(entry);
        }

        nItem.setDisplayName("{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}");
        nItem.setLore(List.of("{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}", "{\"text\":\"Tunic of Destiny\",\"color\":\"red\"}"));

        nItem.setEnchantment("minecraft:sharpness", (short) 10);
        nItem.removeEnchantment("minecraft:sharpness");

        HashMap<String, Short> map = new HashMap<>();
        map.put("minecraft:sharpness", (short) 32);
        map.put("minecraft:flame", (short) 66);
        nItem.addEnchantments(map);


        NCompound compound = new NCompound();
        compound.setBoolean("test_put_tag", true);

        nItem.putTag("this_is_a_put_tag", compound.getInternal());
        nItem.save();
    }

    public static void nbtApiTest() throws RuntimeException
    {
        ItemStack item = new ItemStack(Material.STONE);

        NBTItem nItem = new NBTItem(item, true);


        nItem.setString("str", "sus");
        nItem.setIntArray("intlist", new int[] {1,2,3,4,5,6,7,8,9,0});
        nItem.setLongArray("longlist", new long[] {1L,2L,3L});
        NBTCompoundList compoundList0 = nItem.getCompoundList("compound_list");
        {
            NBTListCompound entry = compoundList0.addCompound();
            entry.setInteger("prop1", 1337);
        }

        {
            NBTCompound display = nItem.getOrCreateCompound("display");
            display.setString("Name", "{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}");
        }
        {
            NBTCompound display = nItem.getOrCreateCompound("display");
            NBTList<String> lore = display.getStringList("Lore");
            lore.addAll(List.of("{\"text\":\"Tunic of Destiny\",\"color\":\"blue\"}", "{\"text\":\"Tunic of Destiny\",\"color\":\"red\"}"));
        }

        setEnchantment(nItem, "minecraft:sharpness", (short) 10);

        removeEnchantment(nItem.getCompoundList("Enchantments"), "minecraft:sharpness");

        HashMap<String, Short> map = new HashMap<>();
        map.put("minecraft:sharpness", (short) 32);
        map.put("minecraft:flame", (short) 66);
        setEnchantments(nItem, map);

        NBTCompound compound = nItem.addCompound("this_is_a_put_tag");
        compound.setBoolean("test_put_tag", true);
        nItem.saveMetaChanges();
    }

    private static void setEnchantment(NBTItem nItem, String id, short level)
    {
        NBTCompoundList compoundList = nItem.getCompoundList("Enchantments");
        setEnchantment(compoundList, id, level);
    }

    private static void setEnchantment(NBTCompoundList compoundList, String id, short level)
    {
        if(!compoundList.isEmpty())
        {
            for (int i = 0; i < compoundList.size(); i++)
            {
                NBTListCompound enchant = compoundList.get(i);
                if(!enchant.getString("id").equals(id))
                    continue;
                if(enchant.getShort("lvl") == level)
                    return;

                enchant.setShort("lvl", level);
                return;
            }
        }

        NBTListCompound enchant = compoundList.addCompound();
        compoundList.addCompound(enchant);
        enchant.setString("id", id);
        enchant.setShort("lvl", level);
    }

    private static void removeEnchantment(NBTCompoundList compoundList, String id)
    {
        if(!compoundList.isEmpty())
        {
            for (int i = 0; i < compoundList.size(); i++)
            {
                NBTListCompound enchant = compoundList.get(i);
                if(!enchant.getString("id").equals(id))
                    continue;
                compoundList.remove(i);
                return;
            }
        }
    }

    private static void setEnchantments(NBTItem nItem, Map<String, Short> map)
    {
        NBTCompoundList compoundList = nItem.getCompoundList("Enchantments");
        setEnchantments(compoundList, map);
    }

    private static void setEnchantments(NBTCompoundList compoundList, Map<String, Short> map)
    {
        for (Map.Entry<String, Short> entry : map.entrySet())
        {
            setEnchantment(compoundList, entry.getKey(), entry.getValue());
        }
    }
}
