package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
*/
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final CommonsLibraryAccessors laccForCommonsLibraryAccessors = new CommonsLibraryAccessors(owner);
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final NmessageLibraryAccessors laccForNmessageLibraryAccessors = new NmessageLibraryAccessors(owner);
    private final RetrofitLibraryAccessors laccForRetrofitLibraryAccessors = new RetrofitLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

        /**
         * Creates a dependency provider for anvilgui (net.wesjd:anvilgui)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAnvilgui() { return create("anvilgui"); }

        /**
         * Creates a dependency provider for bossbarapi (org.inventivetalent:bossbarapi)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBossbarapi() { return create("bossbarapi"); }

        /**
         * Creates a dependency provider for buycraftx (com.github.tebexio.BuycraftX:buycraftx-common)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBuycraftx() { return create("buycraftx"); }

        /**
         * Creates a dependency provider for fastboard (fr.mrmicky:fastboard)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFastboard() { return create("fastboard"); }

        /**
         * Creates a dependency provider for gson (com.google.code.gson:gson)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGson() { return create("gson"); }

        /**
         * Creates a dependency provider for hikaricp (com.zaxxer:HikariCP)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getHikaricp() { return create("hikaricp"); }

        /**
         * Creates a dependency provider for inject (team.unnamed:inject)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getInject() { return create("inject"); }

        /**
         * Creates a dependency provider for jackson (com.fasterxml.jackson.core:jackson-databind)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJackson() { return create("jackson"); }

        /**
         * Creates a dependency provider for jedis (redis.clients:jedis)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJedis() { return create("jedis"); }

        /**
         * Creates a dependency provider for jooq (org.jooq:jooq)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJooq() { return create("jooq"); }

        /**
         * Creates a dependency provider for lombok (org.projectlombok:lombok)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLombok() { return create("lombok"); }

        /**
         * Creates a dependency provider for placeholderapi (me.clip:placeholderapi)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPlaceholderapi() { return create("placeholderapi"); }

        /**
         * Creates a dependency provider for postgresql (org.postgresql:postgresql)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPostgresql() { return create("postgresql"); }

        /**
         * Creates a dependency provider for protocollib (com.comphenix.protocol:ProtocolLib)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getProtocollib() { return create("protocollib"); }

        /**
         * Creates a dependency provider for spigot (org.spigotmc:spigot)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getSpigot() { return create("spigot"); }

        /**
         * Creates a dependency provider for triumphcmd (dev.triumphteam:triumph-cmd-bukkit)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTriumphcmd() { return create("triumphcmd"); }

        /**
         * Creates a dependency provider for triumphgui (dev.triumphteam:triumph-gui)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getTriumphgui() { return create("triumphgui"); }

        /**
         * Creates a dependency provider for vault (com.github.MilkBowl:VaultAPI)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getVault() { return create("vault"); }

        /**
         * Creates a dependency provider for viaversion (com.viaversion:viaversion-api)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getViaversion() { return create("viaversion"); }

        /**
         * Creates a dependency provider for xseries (com.github.cryptomorin:XSeries)
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getXseries() { return create("xseries"); }

    /**
     * Returns the group of libraries at commons
     */
    public CommonsLibraryAccessors getCommons() { return laccForCommonsLibraryAccessors; }

    /**
     * Returns the group of libraries at junit
     */
    public JunitLibraryAccessors getJunit() { return laccForJunitLibraryAccessors; }

    /**
     * Returns the group of libraries at nmessage
     */
    public NmessageLibraryAccessors getNmessage() { return laccForNmessageLibraryAccessors; }

    /**
     * Returns the group of libraries at retrofit
     */
    public RetrofitLibraryAccessors getRetrofit() { return laccForRetrofitLibraryAccessors; }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() { return vaccForVersionAccessors; }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() { return baccForBundleAccessors; }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() { return paccForPluginAccessors; }

    public static class CommonsLibraryAccessors extends SubDependencyFactory {

        public CommonsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for shared (com.zelicraft.commons:shared)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getShared() { return create("commons.shared"); }

            /**
             * Creates a dependency provider for spigot (com.zelicraft.commons:spigot)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getSpigot() { return create("commons.spigot"); }

    }

    public static class JunitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for junit (junit:junit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("junit"); }

            /**
             * Creates a dependency provider for jupiter (org.junit.jupiter:junit-jupiter)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJupiter() { return create("junit.jupiter"); }

    }

    public static class NmessageLibraryAccessors extends SubDependencyFactory {
        private final NmessageSourcetypeLibraryAccessors laccForNmessageSourcetypeLibraryAccessors = new NmessageSourcetypeLibraryAccessors(owner);

        public NmessageLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for core (me.yushust.message:core)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getCore() { return create("nmessage.core"); }

        /**
         * Returns the group of libraries at nmessage.sourcetype
         */
        public NmessageSourcetypeLibraryAccessors getSourcetype() { return laccForNmessageSourcetypeLibraryAccessors; }

    }

    public static class NmessageSourcetypeLibraryAccessors extends SubDependencyFactory {
        private final NmessageSourcetypeBukkitLibraryAccessors laccForNmessageSourcetypeBukkitLibraryAccessors = new NmessageSourcetypeBukkitLibraryAccessors(owner);

        public NmessageSourcetypeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at nmessage.sourcetype.bukkit
         */
        public NmessageSourcetypeBukkitLibraryAccessors getBukkit() { return laccForNmessageSourcetypeBukkitLibraryAccessors; }

    }

    public static class NmessageSourcetypeBukkitLibraryAccessors extends SubDependencyFactory {

        public NmessageSourcetypeBukkitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for yml (me.yushust.message:sourcetype-bukkit-yml)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getYml() { return create("nmessage.sourcetype.bukkit.yml"); }

    }

    public static class RetrofitLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public RetrofitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for retrofit (com.squareup.retrofit2:retrofit)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> asProvider() { return create("retrofit"); }

            /**
             * Creates a dependency provider for gson (com.squareup.retrofit2:converter-gson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getGson() { return create("retrofit.gson"); }

            /**
             * Creates a dependency provider for jackson (com.squareup.retrofit2:converter-jackson)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJackson() { return create("retrofit.jackson"); }

    }

    public static class VersionAccessors extends VersionFactory  {

        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: commons (0.0.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getCommons() { return getVersion("commons"); }

            /**
             * Returns the version associated to this alias: nmessage (7.2.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getNmessage() { return getVersion("nmessage"); }

            /**
             * Returns the version associated to this alias: retrofit (2.9.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRetrofit() { return getVersion("retrofit"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

            /**
             * Creates a dependency bundle provider for commons which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.zelicraft.commons:spigot</li>
             *    <li>com.zelicraft.commons:shared</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getCommons() { return createBundle("commons"); }

            /**
             * Creates a dependency bundle provider for junit which is an aggregate for the following dependencies:
             * <ul>
             *    <li>junit:junit</li>
             *    <li>org.junit.jupiter:junit-jupiter</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getJunit() { return createBundle("junit"); }

            /**
             * Creates a dependency bundle provider for nmessage which is an aggregate for the following dependencies:
             * <ul>
             *    <li>me.yushust.message:core</li>
             *    <li>me.yushust.message:sourcetype-bukkit-yml</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getNmessage() { return createBundle("nmessage"); }

            /**
             * Creates a dependency bundle provider for retrofit which is an aggregate for the following dependencies:
             * <ul>
             *    <li>com.squareup.retrofit2:retrofit</li>
             *    <li>com.squareup.retrofit2:converter-gson</li>
             *    <li>com.squareup.retrofit2:converter-jackson</li>
             * </ul>
             * This bundle was declared in catalog libs.versions.toml
             */
            public Provider<ExternalModuleDependencyBundle> getRetrofit() { return createBundle("retrofit"); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for bukkitPlugin to the plugin id 'net.minecrell.plugin-yml.bukkit'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getBukkitPlugin() { return createPlugin("bukkitPlugin"); }

            /**
             * Creates a plugin provider for paperweight to the plugin id 'io.papermc.paperweight.userdev'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getPaperweight() { return createPlugin("paperweight"); }

            /**
             * Creates a plugin provider for shadow to the plugin id 'com.github.johnrengelman.shadow'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getShadow() { return createPlugin("shadow"); }

    }

}
