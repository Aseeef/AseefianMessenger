package dev.aseef.communicateanywhere.api;

import dev.aseef.communicateanywhere.common.DatabaseCredential;

import java.lang.reflect.InvocationTargetException;

public class MessengerBuilder {

    long listenerThreadsKeepAliveTime = 120;
    private final MessengerType type;
    private DatabaseCredential credential;
    private int replyTimeout = 300;

    protected MessengerBuilder(MessengerType type) {
        this.type = type;
    }

    public MessengerBuilder setCredentials(DatabaseCredential credential) {
        this.credential = credential;
        return this;
    }

    public MessengerBuilder setThreadKeepAliveTime(int milliseconds) {
        this.listenerThreadsKeepAliveTime = milliseconds;
        return this;
    }

    public MessengerBuilder setReplyTimeout(int milliseconds) {
        this.replyTimeout = milliseconds;
        return this;
    }

    public CAMessenger build() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // settings validations here
        //todo

        switch (this.type) {
            case H2:
            case MYSQL:
            case PostgreSQL:
            case MONGODB:
            case REDIS:
                return this.type.getClazz().getConstructor(DatabaseCredential.class, Long.TYPE, Long.TYPE).newInstance(credential, this.listenerThreadsKeepAliveTime, this.replyTimeout);
            case SOCKETS:
                return null;
            default:
                throw new NullPointerException();
        }
    }

}
