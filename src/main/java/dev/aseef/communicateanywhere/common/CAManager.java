package dev.aseef.communicateanywhere.common;

import dev.aseef.communicateanywhere.api.CommunicateAnywhere;
import dev.aseef.communicateanywhere.api.CommunicationDatabase;
import dev.aseef.communicateanywhere.api.Reply;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CAManager implements CommunicateAnywhere {

    @Getter
    private final static Logger logger = LogManager.getLogger(CAManager.class.getSimpleName());

    public CAManager(CommunicationDatabase database) {

    }

    @Override
    public Reply message(String channel, MessageObject mo) {
        return null;
    }


}
