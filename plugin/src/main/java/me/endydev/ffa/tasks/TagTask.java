package me.endydev.ffa.tasks;

import com.zelicraft.commons.shared.cache.ObjectCache;
import me.endydev.ffa.cache.TagPlayer;
import team.unnamed.inject.Inject;

import java.util.UUID;

public class TagTask implements Runnable{
    @Inject
    private ObjectCache<UUID, TagPlayer> taggeds;

    @Override
    public void run() {
        taggeds.getAll().values().forEach(tag -> {
            if(!tag.isTagged()) {
                return;
            }

            tag.setSeconds(tag.getSeconds()-1);

            if(tag.getSeconds() < 1) {
                tag.setTagged(false);
                tag.getAttackers().clear();
            }
        });
    }
}
