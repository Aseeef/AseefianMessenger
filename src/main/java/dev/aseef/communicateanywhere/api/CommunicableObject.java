package dev.aseef.communicateanywhere.api;

public interface CommunicableObject {

    String serialize();
    CommunicableObject deserialize(String s);

}
