package disassebler;

public class Utils {
    
    public static short bytesToShort(byte[] bytes) {
        /*
         * Trasform 2 bytes in a short
        */
        short a = (short)bytes[0];
        a = (short) (a & 0x00ff);
        
        short b = (short)bytes[1];
        b = (short) (b & 0x00ff);
        b = (short) (b << 8);
        
        return (short) (a + b);
    }
    
    public static String numberToHexString(short s) {
        String prefix;
        String h = Integer.toHexString(s & 0xffff);
        if(h.length() == 1)
            prefix = "0x000";
        else if(h.length() == 2)
            prefix = "0x00";
        else if(h.length() == 3)
            prefix = "0x0";
        else
            prefix = "0x";
        return prefix + h;
    }
    
}
