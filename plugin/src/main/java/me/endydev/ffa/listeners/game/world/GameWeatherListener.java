package me.endydev.ffa.listeners.game.world;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameWeatherListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void disableWeather(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }
}
