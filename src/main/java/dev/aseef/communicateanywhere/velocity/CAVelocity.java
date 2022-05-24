package dev.aseef.communicateanywhere.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import org.slf4j.Logger;

@Plugin(
        id = "communicateanywhere",
        name = "CommunicateAnywhere",
        version = "1.0.0",
        description = "Execute commands on other servers or proxies or use as a inter-server communications API.",
        authors = {"${author}"}
)
public class CAVelocity {

    @Inject
    private Logger logger;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

}
