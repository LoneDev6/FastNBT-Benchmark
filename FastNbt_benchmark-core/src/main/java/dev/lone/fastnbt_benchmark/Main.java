package dev.lone.fastnbt_benchmark;

import dev.lone.fastnbt_benchmark.tests.Test;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    @Override
    public void onEnable()
    {
        Test.run();
    }
}
