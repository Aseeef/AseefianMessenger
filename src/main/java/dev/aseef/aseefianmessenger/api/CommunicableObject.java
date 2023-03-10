package dev.aseef.aseefianmessenger.api;

public interface CommunicableObject {

    String serialize();
    CommunicableObject deserialize(String s);

}
