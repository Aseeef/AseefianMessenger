package dev.aseef.communicateanywhere.common.jdbc;

import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.concurrent.CompletableFuture;

/*
create table ca_messages
(
	message_uid binary(16) not null,
	sender_id varchar(64) not null,
	channel varchar(64) not null,
	message int not null,
	constraint ca_messages_pk
		primary key (message_uid)
);
 */

public class SQLMessenger extends JDBCMessenger {

    public SQLMessenger(@NotNull DatabaseCredential credential) {
        super(MessengerType.MYSQL, "mysql", "com.mysql.cj.jdbc.Driver", credential);
    }

    @Override
    public void onAddedChannel(String channel) {

    }

    @Override
    public void onRemovedChannel(String channel) {

    }

}
