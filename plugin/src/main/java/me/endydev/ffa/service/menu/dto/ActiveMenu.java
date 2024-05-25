package me.endydev.ffa.service.menu.dto;

import dev.triumphteam.gui.guis.BaseGui;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActiveMenu {
    private final BaseGui gui;
    @Builder.Default
    private final Object[] objects = new Object[]{};
    private Runnable closeAction;

    public <C> C getObject(int index) {
        if(index >= objects.length) {
            return null;
        }

        return (C) objects[index];
    }
}
