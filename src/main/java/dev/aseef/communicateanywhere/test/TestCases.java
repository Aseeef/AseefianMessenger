package dev.aseef.communicateanywhere.test;

import dev.aseef.communicateanywhere.api.CAListener;
import dev.aseef.communicateanywhere.api.CAMessenger;
import dev.aseef.communicateanywhere.api.MessengerType;
import dev.aseef.communicateanywhere.common.CAReply;
import dev.aseef.communicateanywhere.common.DatabaseCredential;
import dev.aseef.communicateanywhere.common.MessageObject;
import lombok.SneakyThrows;

public class TestCases {

    static long last = 0;

    @SneakyThrows
    public static void main(String[] args) {

        CAMessenger messenger = MessengerType.MYSQL.builder()
                .setCredentials(
                        new DatabaseCredential(
                                "vps1.aseef.dev",
                                "root",
                                "Aseef@2003@aseef.dev",
                                3306,
                                "gtm")
                ).setReplyTimeout(5000)
                .setThreadKeepAliveTime(60000)
                .build();

        messenger.addListener(new CAListener() {
            @Override
            public String[] getListeningChannels() {
                return new String[] {"test"};
            }

            @Override
            public void onMessage(String senderId, String channel, CAReply callbackReply, MessageObject object) {
                System.out.println(channel + " " + (System.currentTimeMillis() - last));
                callbackReply.reply(MessageObject.from("Sup."));
            }
        });

        for (int i = 0 ; i < 500 ;i ++) {
            last = System.currentTimeMillis();
            messenger.message("test", MessageObject.from("Hi"));
            messenger.message("test2", MessageObject.from("Hi"));
            messenger.message("test3", MessageObject.from("Hi"));
            messenger.message("test4", MessageObject.from("Hi"));
        }

    }

}
