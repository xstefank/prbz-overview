/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.overview.server.bean;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.infinispan.api.BasicCacheContainer;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.logging.Logger;

/**
 * {@link CacheContainerProvider}'s implementation creating a DefaultCacheManager which is configured programmatically.
 * Infinispan's libraries need to be bundled with the application - this is called "library" mode.
 *
 *
 * @author Martin Gencur
 *
 */
@ApplicationScoped
public class CacheContainerProvider {
    private static final Logger LOGGER = Logger.getLogger(CacheContainerProvider.class);

    private BasicCacheContainer manager;

    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                .nonClusteredDefault() //Helper method that gets you a default constructed GlobalConfiguration, preconfigured for use in LOCAL mode
                .globalJmxStatistics().enable() //This method allows enables the jmx statistics of the global configuration.
                .jmxDomain("overview")
                .build();
            //Builds  the GlobalConfiguration object
            Configuration loc = new ConfigurationBuilder()
                .clustering().cacheMode(CacheMode.LOCAL) //Set Cache mode to LOCAL - Data is not replicated.
                .eviction().maxEntries(500).strategy(EvictionStrategy.LIRS) //Sets 500 as maximum number of entries in a cache instance and uses the LIRS strategy - an efficient low inter-reference recency set replacement policy to improve buffer cache performance
                .loaders().passivation(false) //Disable passivation and adds a FileCacheStore that is Purged on Startup
                .locking().concurrencyLevel(16)
                .build(); //Builds the Configuration object
            manager = new DefaultCacheManager(glob, loc, true);
            LOGGER.info("=== Using DefaultCacheManager (library mode) ===");
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }

}
