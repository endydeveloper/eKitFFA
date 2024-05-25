package me.endydev.ffa.translation.linguist;

import com.zelicraft.commons.shared.repositories.user.dto.User;
import me.yushust.message.language.Linguist;
import org.jetbrains.annotations.Nullable;

public class PlayerLinguist implements Linguist<User> {
    @Override
    public @Nullable String getLanguage(User user) {
        return user.getLanguage().getCode();
    }
}
