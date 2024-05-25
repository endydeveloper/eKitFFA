package me.endydev.ffa.inject.modules;

import me.endydev.ffa.FFAPlugin;
import com.zelicraft.commons.shared.data.enums.Language;
import com.zelicraft.commons.shared.repositories.user.UserRepository;
import com.zelicraft.commons.shared.repositories.user.dto.User;
import com.zelicraft.core.spigot.api.CoreAPI;
import com.zelicraft.core.spigot.api.utils.Text;
import me.endydev.ffa.translation.linguist.PlayerLinguist;
import me.endydev.ffa.translation.linguist.SenderLinguist;
import me.endydev.ffa.translation.linguist.UUIDLinguist;
import me.endydev.ffa.translation.sender.CommandSenderMessageSender;
import me.endydev.ffa.translation.sender.PlayerMessageSender;
import me.yushust.message.MessageHandler;
import me.yushust.message.config.ConfigurationHandle;
import me.yushust.message.source.MessageSourceDecorator;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Inject;
import team.unnamed.inject.Provides;
import team.unnamed.inject.Singleton;

import java.io.File;
import java.util.UUID;

import static com.zelicraft.commons.shared.utils.folder.FolderUtils.copyFolder;
import static me.yushust.message.bukkit.BukkitMessageAdapt.newYamlSource;

public class TranslationModule extends AbstractModule {
    private final FFAPlugin plugin;

    @Inject
    private UserRepository userRepository;

    public TranslationModule(FFAPlugin plugin) {
        this.plugin = plugin;

    }

    @Override
    protected void configure() {
        try {
            copyFolder("i18n", plugin.getDataFolder(), false);
        } catch (Exception e) {
            throw new RuntimeException("Failed to copy translation files.", e);
        }
    }


    private MessageHandler getMessageHandler(File menusFolder, String menu, CoreAPI coreAPI, Text text) {
        return MessageHandler.of(
                MessageSourceDecorator
                        .decorate(newYamlSource(plugin, new File(menusFolder, menu), "%lang%.yml"))
                        .addFallbackLanguage(Language.getDefaultLanguage().getCode()).get(),
                handle -> getConfigurationModule(handle, coreAPI)
        );
    }

    private void getConfigurationModule(ConfigurationHandle handle, CoreAPI coreAPI) {
        Text text = coreAPI.getTextColorizer();
        handle.delimiting("{", "}")
                .addInterceptor(text::colorizeHex);//§

        handle.specify(UUID.class)
                .setLinguist(new UUIDLinguist(coreAPI));

        handle.specify(CommandSender.class)
                .setLinguist(new SenderLinguist(coreAPI))
                .setMessageSender(new CommandSenderMessageSender());

        handle.specify(User.class)
                .setLinguist(new PlayerLinguist())
                .setMessageSender(new PlayerMessageSender());


        handle.bindCompatibleSupertype(CommandSender.class, ConsoleCommandSender.class);

    }


    @Provides
    @Singleton
    private MessageHandler provideMessageHandler(CoreAPI coreAPI) {
        return MessageHandler.of(
                MessageSourceDecorator
                        .decorate(newYamlSource(plugin, new File(plugin.getDataFolder(), "i18n"), "%lang%.yml"))
                        .addFallbackLanguage(Language.getDefaultLanguage().getCode())
                        .get(),
                handle -> {
                    Text text = coreAPI.getTextColorizer();
                    handle.delimiting("{", "}")
                            .addInterceptor(text::colorizeHex);

                    handle.specify(UUID.class)
                                    .setLinguist(new UUIDLinguist(coreAPI));

                    handle.specify(CommandSender.class)
                            .setLinguist(new SenderLinguist(coreAPI))
                            .setMessageSender(new CommandSenderMessageSender());

                    handle.specify(User.class)
                                    .setLinguist(new PlayerLinguist())
                                    .setMessageSender(new PlayerMessageSender());


                    handle.bindCompatibleSupertype(CommandSender.class, ConsoleCommandSender.class);
                }
        );
    }
}

