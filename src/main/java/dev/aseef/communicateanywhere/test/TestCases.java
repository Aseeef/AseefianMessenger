package dev.aseef.communicateanywhere.test;

import dev.aseef.communicateanywhere.common.AbstractMessenger;
import dev.aseef.communicateanywhere.test.test2.Something;
import org.bson.BsonBinary;
import org.bson.BsonDocument;

import java.util.Random;

public class TestCases {

    public static void main(String[] args) {
        long sum = 0;

        for (int i = 0 ; i < 20 ; i++) {
            byte[] bytes = new byte[100000];
            new Random().nextBytes(bytes);

            //String base64 = Base64.getEncoder().encodeToString(bytes);
            String bson = new BsonDocument().append("test", new BsonBinary(bytes)).toString();
            sum += bson.length();
        }

        System.out.println(sum / 20f);

    }

    public static void lol() {
        AbstractMessenger.getLogger().trace("Hi");
        Something.lol();
        new Something().xD();
    }

}
