package dev.aseef.aseefianmessenger.minecraft.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import lombok.Getter;
import org.slf4j.Logger;

@Plugin(
        id = "communicateanywhere",
        name = "CommunicateAnywhere",
        version = "1.0.0",
        description = "Execute commands on other servers or proxies or use as a inter-server communications API."
)
public class CAVelocity {

    @Inject
    private Logger logger;
    @Getter
    private static CAVelocity instance;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {

    }

}
