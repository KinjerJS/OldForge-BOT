package fr.kinjer.oldforge.bot.event;

import fr.kinjer.oldforge.bot.command.CommandManager;
import fr.kinjer.oldforge.bot.utils.ScannerPackage;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final List<ListenerAdapter> events = new ArrayList<>();

    public EventManager() {
        this.registerEvent(new CommandManager.Event());

        ScannerPackage.getClassOfPackage(ListenerAdapter.class, "fr.kinjer.oldforge.bot.event.model").forEach(listener -> {
            try {
                this.registerEvent(listener.getConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void registerEvent(ListenerAdapter event) {
        events.add(event);
    }

    public ListenerAdapter getEvent(String name) {
        return events.stream().filter(ev -> ev.getClass().getSimpleName().equals(name)).findFirst().orElse(null);
    }

    public List<ListenerAdapter> getEvents() {
        return events;
    }

}
