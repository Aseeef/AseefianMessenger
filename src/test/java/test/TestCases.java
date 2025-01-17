package test;

import dev.aseef.aseefianmessenger.api.CAListener;
import dev.aseef.aseefianmessenger.api.CAMessenger;
import dev.aseef.aseefianmessenger.api.MessengerType;
import dev.aseef.aseefianmessenger.common.CAReply;
import dev.aseef.aseefianmessenger.common.DatabaseCredential;
import dev.aseef.aseefianmessenger.common.MessageObject;
import lombok.SneakyThrows;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.HashMap;

public class TestCases {

    static long last = 0;

    @SneakyThrows
    public static void main(String[] args) {

        HashMap<Integer,Long> sentTimeMap = new HashMap<>();
        JSONObject config = new JSONObject(IOUtils.toString(new FileReader("config.json")));

        CAMessenger messenger = MessengerType.MYSQL.builder()
                .setCredentials(
                        new DatabaseCredential(
                                config.getString("hostname"),
                                config.getString("username"),
                                config.getString("password"),
                                config.getInt("port"),
                                config.getString("database"))
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
                System.out.println("RECEIVED: " + object.getAsInteger() + " in " + (System.currentTimeMillis() - sentTimeMap.get(object.getAsInteger())));
                callbackReply.reply(null);
            }
        });

        for (int i = 0 ; i < 50 ;i ++) {
            sentTimeMap.put(i, System.currentTimeMillis());
            messenger.message("test", MessageObject.from(i));
        }
        System.exit(0);

    }

}
