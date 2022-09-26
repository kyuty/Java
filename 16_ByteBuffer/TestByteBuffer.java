import java.nio.ByteBuffer;

public class TestByteBuffer {

    public static void main(String args[]){
        String dataKey = "shabiganggge";
        int dataKeyLength = dataKey.length();
        System.out.println("dataKeyLength = " + dataKeyLength);
        String str = ByteBuffer.allocate(4).putInt(dataKeyLength).array().toString();
        System.out.println("str = " + str);

        {
            byte[] array = ByteBuffer.allocate(4).putInt(dataKeyLength).array();
            System.out.println("array " + array + " int = " + byteToInt(array));
        }
    }

    public static int byteToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

}
