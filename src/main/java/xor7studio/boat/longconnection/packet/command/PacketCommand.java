package xor7studio.boat.longconnection.packet.command;

public interface PacketCommand {
    byte HEARTBEAT_REQUEST = 1;
    byte HEARTBEAT_RESPONSE = 2;
    byte HANDSHAKE_REQUEST = 3;
    byte HANDSHAKE_RESPONSE = 4;

}
