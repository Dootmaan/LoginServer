package login_server;

public class XOR {
  public static byte[] xorEncode(byte[] data, String key) {

    byte[] keyBytes = key.getBytes();

    byte[] encryptBytes = new byte[data.length];
    for (int i = 0; i < data.length; i++) {
      encryptBytes[i] = (byte) (data[i] ^ keyBytes[i % keyBytes.length]);
    }
    return encryptBytes;
  }
}
