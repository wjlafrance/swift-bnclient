// https://github.com/wjlafrance/javaop2/blob/db200ea2f96c9455d1dbd464b2a2430fbd7b7d8b/jbls/src/main/java/util/IntFromByteArray.java
// https://github.com/wjlafrance/javaop2/blob/db200ea2f96c9455d1dbd464b2a2430fbd7b7d8b/jbls/src/main/java/util/ByteFromIntArray.java
// https://github.com/wjlafrance/javaop2/blob/db200ea2f96c9455d1dbd464b2a2430fbd7b7d8b/jbls/src/main/java/Hashing/War3Decode.java

import java.security.MessageDigest;

class ByteFromIntArray
{
    private boolean littleEndian;

    public static final ByteFromIntArray LITTLEENDIAN = new ByteFromIntArray(true);
    public static final ByteFromIntArray BIGENDIAN = new ByteFromIntArray(false);

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args)
    {
        int []test = { 0x01234567, 0x89abcdef };

        ByteFromIntArray bfia = new ByteFromIntArray(false);

        byte []newArray = bfia.getByteArray(test);

        for(int i = 0; i < newArray.length; i++)
            System.out.print(" " + PadString.padHex(newArray[i], 2));
    }*/

    public ByteFromIntArray(boolean littleEndian)
    {
        this.littleEndian = littleEndian;
    }

    public byte getByte(int[] array, int location)
    {
        if((location / 4) >= array.length)
            throw new ArrayIndexOutOfBoundsException("location = " + location + ", number of bytes = " + (array.length * 4));

        int theInt = location / 4; // rounded
        int theByte = location % 4; // remainder


        // reverse the byte to simulate little endian
        if(littleEndian)
            theByte = 3 - theByte;

        // I was worried about sign-extension here, but then I realized that they are being
        // put into a byte anyway so it wouldn't matter.
        if(theByte == 0)
            return (byte)((array[theInt] & 0x000000FF) >> 0);
        else if(theByte == 1)
            return (byte)((array[theInt] & 0x0000FF00) >> 8);
        else if(theByte == 2)
            return (byte)((array[theInt] & 0x00FF0000) >> 16);
        else if(theByte == 3)
            return (byte)((array[theInt] & 0xFF000000) >> 24);

        return 0;
    }


    /** This function is used to insert the byte into a specified spot in
     * an int array.  This is used to simulate pointers used in C++.
     * Note that this works in little endian only.
     * @param intBuffer The buffer to insert the int into.
     * @param b The byte we're inserting.
     * @param location The location (which byte) we're inserting it into.
     * @return The new array - this is returned for convenience only.
     */
    public int[] insertByte(int[] intBuffer, int location, byte b)
    {
        // Get the location in the array and in the int
        int theInt = location / 4;
        int theByte = location % 4;

        // If we're using little endian reverse the hex position
        if(littleEndian == false)
            theByte = 3 - theByte;

        int replaceInt = intBuffer[theInt];

        // Creating a new variable here because b is a byte and I need an int
        int newByte = b << (8 * theByte);

        if(theByte == 0)
            replaceInt &= 0xFFFFFF00;
        else if(theByte == 1)
            replaceInt &= 0xFFFF00FF;
        else if(theByte == 2)
            replaceInt &= 0xFF00FFFF;
        else if(theByte == 3)
            replaceInt &= 0x00FFFFFF;

        replaceInt = replaceInt | newByte;

        intBuffer[theInt] = replaceInt;

        return intBuffer;

    }


    public byte[] getByteArray(int[] array)
    {
        byte[] newArray = new byte[array.length * 4];

        int pos = 0;
        for(int i = 0; i < array.length; i++)
        {
            if(littleEndian)
            {
                newArray[pos++] = (byte)((array[i] >> 0) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 8) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 16) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 24) & 0xFF);
            }
            else
            {
                newArray[pos++] = (byte)((array[i] >> 24) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 16) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 8) & 0xFF);
                newArray[pos++] = (byte)((array[i] >> 0) & 0xFF);
            }
        }

        return newArray;
    }

    public byte[] getByteArray(int integer)
    {
        int[] temp = new int[1];
        temp[0] = integer;
        return getByteArray(temp);
    }
}

 class IntFromByteArray
 {
     private boolean littleEndian;

     public static final IntFromByteArray LITTLEENDIAN = new IntFromByteArray(true);
     public static final IntFromByteArray BIGENDIAN = new IntFromByteArray(false);

     /**
      * @param args the command line arguments
      */

     /*public static void main(String args[])
     {
         byte[] test = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

         IntFromByteArray ifba = new IntFromByteArray(true);

         int[] newArray = ifba.getIntArray(test);

         for(int i = 0; i < newArray.length; i++)
             System.out.print(PadString.padHex(newArray[i], 8) + " ");
     }*/

     public IntFromByteArray(boolean littleEndian)
     {
         this.littleEndian = littleEndian;
     }
     public int getInteger(byte[] array, int location)
     {
         if((location + 3) >= array.length)
             throw new ArrayIndexOutOfBoundsException("location = " + location + ", number of bytes = " + array.length + " (note: 4 available bytes are needed)");

         int retVal = 0;

         // reverse the byte to simulate little endian
         if(littleEndian)
         {
             retVal = retVal | ((array[location++] << 0)  & 0x000000FF);
             retVal = retVal | ((array[location++] << 8)  & 0x0000FF00);
             retVal = retVal | ((array[location++] << 16) & 0x00FF0000);
             retVal = retVal | ((array[location++] << 24) & 0xFF000000);
         }
         else
         {
             retVal = retVal | ((array[location++] << 24) & 0xFF000000);
             retVal = retVal | ((array[location++] << 16) & 0x00FF0000);
             retVal = retVal | ((array[location++] << 8)  & 0x0000FF00);
             retVal = retVal | ((array[location++] << 0)  & 0x000000FF);
         }

         return retVal;
     }


     /** This function is used to insert the byte into a specified spot in
      * an int array.  This is used to simulate pointers used in C++.
      * Note that this works in little endian only.
      * @param intBuffer The buffer to insert the int into.
      * @param b The byte we're inserting.
      * @param location The location (which byte) we're inserting it into.
      * @return The new array - this is returned for convenience only.
      */
     public byte[] insertInteger(byte[] array, int location, int b)
     {
         if(location + 3 >= array.length)
             throw new ArrayIndexOutOfBoundsException("location = " + location + ", length = " + array.length + " - note that we need 4 bytes to insert an int");

         if(littleEndian)
         {
             array[location++] = (byte)((b & 0x000000FF) >> 0);
             array[location++] = (byte)((b & 0x0000FF00) >> 8);
             array[location++] = (byte)((b & 0x00FF0000) >> 16);
             array[location++] = (byte)((b & 0xFF000000) >> 24);
         }
         else
         {
             array[location++] = (byte)((b & 0xFF000000) >> 24);
             array[location++] = (byte)((b & 0x00FF0000) >> 16);
             array[location++] = (byte)((b & 0x0000FF00) >> 8);
             array[location++] = (byte)((b & 0x000000FF) >> 0);
         }

         return array;
     }

     /** Note: This will cut off the end bytes to ensure it's a multiple of 4 */
     public int[] getIntArray(byte[] array)
     {
         int[] newArray = new int[array.length / 4];

         int pos = 0;
         for(int i = 0; i < newArray.length; i++)
         {
             if(littleEndian)
             {
                 newArray[i] |= ((array[pos++] << 0) &  0x000000FF);
                 newArray[i] |= ((array[pos++] << 8) &  0x0000FF00);
                 newArray[i] |= ((array[pos++] << 16) & 0x00FF0000);
                 newArray[i] |= ((array[pos++] << 24) & 0xFF000000);
             }
             else
             {
                 newArray[i] |= array[pos++] << 24;
                 newArray[i] |= array[pos++] << 16;
                 newArray[i] |= array[pos++] << 8;
                 newArray[i] |= array[pos++] << 0;
             }
         }

         return newArray;
     }

}

public class CdKeyDecode
{
    public final static byte[] KeyTable =
    {
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0xFF, (byte) 0x01, (byte) 0xFF, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0xFF, (byte) 0x0D, (byte) 0x0E, (byte) 0xFF, (byte) 0x0F, (byte) 0x10, (byte) 0xFF,
        (byte) 0x11, (byte) 0xFF, (byte) 0x12, (byte) 0xFF, (byte) 0x13, (byte) 0xFF, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0x06, (byte) 0x07, (byte) 0x08, (byte) 0x09, (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0xFF, (byte) 0x0D, (byte) 0x0E, (byte) 0xFF, (byte) 0x0F, (byte) 0x10, (byte) 0xFF,
        (byte) 0x11, (byte) 0xFF, (byte) 0x12, (byte) 0xFF, (byte) 0x13, (byte) 0xFF, (byte) 0x14, (byte) 0x15, (byte) 0x16, (byte) 0x17, (byte) 0x18, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF
    };

    public final static byte []TranslateTable =
    {
        (byte) 0x09, (byte) 0x04, (byte) 0x07, (byte) 0x0F, (byte) 0x0D, (byte) 0x0A, (byte) 0x03, (byte) 0x0B, (byte) 0x01, (byte) 0x02, (byte) 0x0C, (byte) 0x08, (byte) 0x06, (byte) 0x0E, (byte) 0x05, (byte) 0x00,
        (byte) 0x09, (byte) 0x0B, (byte) 0x05, (byte) 0x04, (byte) 0x08, (byte) 0x0F, (byte) 0x01, (byte) 0x0E, (byte) 0x07, (byte) 0x00, (byte) 0x03, (byte) 0x02, (byte) 0x0A, (byte) 0x06, (byte) 0x0D, (byte) 0x0C,
        (byte) 0x0C, (byte) 0x0E, (byte) 0x01, (byte) 0x04, (byte) 0x09, (byte) 0x0F, (byte) 0x0A, (byte) 0x0B, (byte) 0x0D, (byte) 0x06, (byte) 0x00, (byte) 0x08, (byte) 0x07, (byte) 0x02, (byte) 0x05, (byte) 0x03,
        (byte) 0x0B, (byte) 0x02, (byte) 0x05, (byte) 0x0E, (byte) 0x0D, (byte) 0x03, (byte) 0x09, (byte) 0x00, (byte) 0x01, (byte) 0x0F, (byte) 0x07, (byte) 0x0C, (byte) 0x0A, (byte) 0x06, (byte) 0x04, (byte) 0x08,
        (byte) 0x06, (byte) 0x02, (byte) 0x04, (byte) 0x05, (byte) 0x0B, (byte) 0x08, (byte) 0x0C, (byte) 0x0E, (byte) 0x0D, (byte) 0x0F, (byte) 0x07, (byte) 0x01, (byte) 0x0A, (byte) 0x00, (byte) 0x03, (byte) 0x09,
        (byte) 0x05, (byte) 0x04, (byte) 0x0E, (byte) 0x0C, (byte) 0x07, (byte) 0x06, (byte) 0x0D, (byte) 0x0A, (byte) 0x0F, (byte) 0x02, (byte) 0x09, (byte) 0x01, (byte) 0x00, (byte) 0x0B, (byte) 0x08, (byte) 0x03,
        (byte) 0x0C, (byte) 0x07, (byte) 0x08, (byte) 0x0F, (byte) 0x0B, (byte) 0x00, (byte) 0x05, (byte) 0x09, (byte) 0x0D, (byte) 0x0A, (byte) 0x06, (byte) 0x0E, (byte) 0x02, (byte) 0x04, (byte) 0x03, (byte) 0x01,
        (byte) 0x03, (byte) 0x0A, (byte) 0x0E, (byte) 0x08, (byte) 0x01, (byte) 0x0B, (byte) 0x05, (byte) 0x04, (byte) 0x02, (byte) 0x0F, (byte) 0x0D, (byte) 0x0C, (byte) 0x06, (byte) 0x07, (byte) 0x09, (byte) 0x00,
        (byte) 0x0C, (byte) 0x0D, (byte) 0x01, (byte) 0x0F, (byte) 0x08, (byte) 0x0E, (byte) 0x05, (byte) 0x0B, (byte) 0x03, (byte) 0x0A, (byte) 0x09, (byte) 0x00, (byte) 0x07, (byte) 0x02, (byte) 0x04, (byte) 0x06,
        (byte) 0x0D, (byte) 0x0A, (byte) 0x07, (byte) 0x0E, (byte) 0x01, (byte) 0x06, (byte) 0x0B, (byte) 0x08, (byte) 0x0F, (byte) 0x0C, (byte) 0x05, (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x09,
        (byte) 0x03, (byte) 0x0E, (byte) 0x07, (byte) 0x05, (byte) 0x0B, (byte) 0x0F, (byte) 0x08, (byte) 0x0C, (byte) 0x01, (byte) 0x0A, (byte) 0x04, (byte) 0x0D, (byte) 0x00, (byte) 0x06, (byte) 0x09, (byte) 0x02,
        (byte) 0x0B, (byte) 0x06, (byte) 0x09, (byte) 0x04, (byte) 0x01, (byte) 0x08, (byte) 0x0A, (byte) 0x0D, (byte) 0x07, (byte) 0x0E, (byte) 0x00, (byte) 0x0C, (byte) 0x0F, (byte) 0x02, (byte) 0x03, (byte) 0x05,
        (byte) 0x0C, (byte) 0x07, (byte) 0x08, (byte) 0x0D, (byte) 0x03, (byte) 0x0B, (byte) 0x00, (byte) 0x0E, (byte) 0x06, (byte) 0x0F, (byte) 0x09, (byte) 0x04, (byte) 0x0A, (byte) 0x01, (byte) 0x05, (byte) 0x02,
        (byte) 0x0C, (byte) 0x06, (byte) 0x0D, (byte) 0x09, (byte) 0x0B, (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x0F, (byte) 0x07, (byte) 0x03, (byte) 0x04, (byte) 0x0A, (byte) 0x0E, (byte) 0x08, (byte) 0x05,
        (byte) 0x03, (byte) 0x06, (byte) 0x01, (byte) 0x05, (byte) 0x0B, (byte) 0x0C, (byte) 0x08, (byte) 0x00, (byte) 0x0F, (byte) 0x0E, (byte) 0x09, (byte) 0x04, (byte) 0x07, (byte) 0x0A, (byte) 0x0D, (byte) 0x02,
        (byte) 0x0A, (byte) 0x07, (byte) 0x0B, (byte) 0x0F, (byte) 0x02, (byte) 0x08, (byte) 0x00, (byte) 0x0D, (byte) 0x0E, (byte) 0x0C, (byte) 0x01, (byte) 0x06, (byte) 0x09, (byte) 0x03, (byte) 0x05, (byte) 0x04,
        (byte) 0x0A, (byte) 0x0B, (byte) 0x0D, (byte) 0x04, (byte) 0x03, (byte) 0x08, (byte) 0x05, (byte) 0x09, (byte) 0x01, (byte) 0x00, (byte) 0x0F, (byte) 0x0C, (byte) 0x07, (byte) 0x0E, (byte) 0x02, (byte) 0x06,
        (byte) 0x0B, (byte) 0x04, (byte) 0x0D, (byte) 0x0F, (byte) 0x01, (byte) 0x06, (byte) 0x03, (byte) 0x0E, (byte) 0x07, (byte) 0x0A, (byte) 0x0C, (byte) 0x08, (byte) 0x09, (byte) 0x02, (byte) 0x05, (byte) 0x00,
        (byte) 0x09, (byte) 0x06, (byte) 0x07, (byte) 0x00, (byte) 0x01, (byte) 0x0A, (byte) 0x0D, (byte) 0x02, (byte) 0x03, (byte) 0x0E, (byte) 0x0F, (byte) 0x0C, (byte) 0x05, (byte) 0x0B, (byte) 0x04, (byte) 0x08,
        (byte) 0x0D, (byte) 0x0E, (byte) 0x05, (byte) 0x06, (byte) 0x01, (byte) 0x09, (byte) 0x08, (byte) 0x0C, (byte) 0x02, (byte) 0x0F, (byte) 0x03, (byte) 0x07, (byte) 0x0B, (byte) 0x04, (byte) 0x00, (byte) 0x0A,
        (byte) 0x09, (byte) 0x0F, (byte) 0x04, (byte) 0x00, (byte) 0x01, (byte) 0x06, (byte) 0x0A, (byte) 0x0E, (byte) 0x02, (byte) 0x03, (byte) 0x07, (byte) 0x0D, (byte) 0x05, (byte) 0x0B, (byte) 0x08, (byte) 0x0C,
        (byte) 0x03, (byte) 0x0E, (byte) 0x01, (byte) 0x0A, (byte) 0x02, (byte) 0x0C, (byte) 0x08, (byte) 0x04, (byte) 0x0B, (byte) 0x07, (byte) 0x0D, (byte) 0x00, (byte) 0x0F, (byte) 0x06, (byte) 0x09, (byte) 0x05,
        (byte) 0x07, (byte) 0x02, (byte) 0x0C, (byte) 0x06, (byte) 0x0A, (byte) 0x08, (byte) 0x0B, (byte) 0x00, (byte) 0x0F, (byte) 0x04, (byte) 0x03, (byte) 0x0E, (byte) 0x09, (byte) 0x01, (byte) 0x0D, (byte) 0x05,
        (byte) 0x0C, (byte) 0x04, (byte) 0x05, (byte) 0x09, (byte) 0x0A, (byte) 0x02, (byte) 0x08, (byte) 0x0D, (byte) 0x03, (byte) 0x0F, (byte) 0x01, (byte) 0x0E, (byte) 0x06, (byte) 0x07, (byte) 0x0B, (byte) 0x00,
        (byte) 0x0A, (byte) 0x08, (byte) 0x0E, (byte) 0x0D, (byte) 0x09, (byte) 0x0F, (byte) 0x03, (byte) 0x00, (byte) 0x04, (byte) 0x06, (byte) 0x01, (byte) 0x0C, (byte) 0x07, (byte) 0x0B, (byte) 0x02, (byte) 0x05,
        (byte) 0x03, (byte) 0x0C, (byte) 0x04, (byte) 0x0A, (byte) 0x02, (byte) 0x0F, (byte) 0x0D, (byte) 0x0E, (byte) 0x07, (byte) 0x00, (byte) 0x05, (byte) 0x08, (byte) 0x01, (byte) 0x06, (byte) 0x0B, (byte) 0x09,
        (byte) 0x0A, (byte) 0x0C, (byte) 0x01, (byte) 0x00, (byte) 0x09, (byte) 0x0E, (byte) 0x0D, (byte) 0x0B, (byte) 0x03, (byte) 0x07, (byte) 0x0F, (byte) 0x08, (byte) 0x05, (byte) 0x02, (byte) 0x04, (byte) 0x06,
        (byte) 0x0E, (byte) 0x0A, (byte) 0x01, (byte) 0x08, (byte) 0x07, (byte) 0x06, (byte) 0x05, (byte) 0x0C, (byte) 0x02, (byte) 0x0F, (byte) 0x00, (byte) 0x0D, (byte) 0x03, (byte) 0x0B, (byte) 0x04, (byte) 0x09,
        (byte) 0x03, (byte) 0x08, (byte) 0x0E, (byte) 0x00, (byte) 0x07, (byte) 0x09, (byte) 0x0F, (byte) 0x0C, (byte) 0x01, (byte) 0x06, (byte) 0x0D, (byte) 0x02, (byte) 0x05, (byte) 0x0A, (byte) 0x0B, (byte) 0x04,
        (byte) 0x03, (byte) 0x0A, (byte) 0x0C, (byte) 0x04, (byte) 0x0D, (byte) 0x0B, (byte) 0x09, (byte) 0x0E, (byte) 0x0F, (byte) 0x06, (byte) 0x01, (byte) 0x07, (byte) 0x02, (byte) 0x00, (byte) 0x05, (byte) 0x08
    };


    public static final int W3_KEYLEN = 26;
    public static final int W3_BUFLEN = (W3_KEYLEN*2);

    private int val1;
    private byte[] val2;
    private int product;

	 public static void main(String args[]) {
		 String testKey = "YKY7EPR664G6DWG7CV8REKVEGK";
		 CdKeyDecode decode = new CdKeyDecode(testKey);
		 System.out.format("Product: %08X\n", decode.getProduct());
		 System.out.format("Val 1:   %08X\n", decode.getVal1());
		 System.out.print("Val 2:\n\t");
		 for (int i = 0; i < 10; i++) {
			 System.out.format("%02X ", decode.getWar3Val2()[i]);
		 }
		 System.out.print("\n");
		 System.out.println("Hashed (client 5, server 6):\n\t");
		 for (int i = 0; i < 5; i++) {
			 System.out.format("%08X ", decode.getKeyHash(5, 6)[i]);
		 }
	 }

    /** Creates a new instance of War3Decode */
    public CdKeyDecode(String cdkey)
    {
        byte []table = new byte[W3_BUFLEN];
        int []values = new int[4];
        tableLookup(cdkey.toUpperCase(), table);

        for(int i = W3_BUFLEN; i > 0; i--)
            Mult(4, 5, values, values, table[i - 1]);

        // -------------

        decodeKeyTablePass1(values);


        decodeKeyTablePass2(values);


        product = values[0] >> 0x0a;
        //val1 = ((values[0] & 0x03FF) << 0x10) | (values[1] >> 0x10);
        //val1 = IntFromByteArray.LITTLEENDIAN.getInteger(values, 0);
        val1 = ((values[0] & 0x03FF) << 0x10) | (values[1] >>> 0x10);

        val2 = new byte[10];
        val2[0] = (byte)((values[1] & 0x00FF) >> 0);
        val2[1] = (byte)((values[1] & 0xFF00) >> 8);

        IntFromByteArray.LITTLEENDIAN.insertInteger(val2, 2, values[2]);
        IntFromByteArray.LITTLEENDIAN.insertInteger(val2, 6, values[3]);

    }


    private void tableLookup(String key, byte []buf)
    {
        int a;
        int b = 0x21;
        byte decode;

        for(int i = 0; i < W3_KEYLEN; i++)
        {
            a = (b + 0x07B5) % W3_BUFLEN;
            b = (a + 0x07B5) % W3_BUFLEN;
            decode = KeyTable[key.charAt(i)];
            buf[a] = (byte)(decode / 5);
            buf[b] = (byte)(decode % 5);
        }
    }

    private void Mult(int rounds, int mulx, int []bufA, int []bufB, int decodedByte)
    {
        int posA = rounds - 1;
        int posB = rounds - 1;

        while(rounds-- > 0)
        {
            long param1 = bufA[posA--];
            param1 &= 0x00000000FFFFFFFFl;

            long param2 = mulx;
            param2 &= 0x00000000FFFFFFFFl;

            long edxeax = param1 * param2;

            //ULONGLONG edxeax = UInt32x32To64(*BufA--, Mulx);
            bufB[posB--] = decodedByte + (int)edxeax;
            decodedByte = (int)(edxeax >> 32);
        }

    }

    private void decodeKeyTablePass1(int []keyTable)
    {
        int ebx, ecx, esi, ebp;
        int var_C, var_4;
        int var_8 = 29;

        for(int i = 464; i >= 0; i -= 16)
        {
            esi = (var_8 & 7) << 2;
            var_4 = var_8 >>> 3;
            var_C = (keyTable[3 - var_4] & (0x0F << esi)) >>> esi;

            if(i < 464)
            {
                for(int j = 29; j > var_8; j--)
                {
                    ecx = (j & 7) << 2;
                    ebp = (keyTable[0x03 - (j >>> 3)] & (0x0F << ecx)) >>> ecx;
                    var_C = TranslateTable[ebp^TranslateTable[var_C + i] + i];
                }
            }

            for(int j = --var_8; j >= 0; j--)
            {
                ecx = (j & 7) << 2;
                ebp = (keyTable[0x03 - (j >>> 3)] & (0x0F << ecx)) >>> ecx;
                var_C = TranslateTable[ebp^TranslateTable[var_C + i] + i];
            }

            int index = 3 - var_4;
            ebx = (TranslateTable[var_C + i] & 0x0F) << esi;
            keyTable[index] =(ebx | ~(0x0F << esi) & ((int)keyTable[index]));
        }
    }

    void decodeKeyTablePass2(int []keyTable)
    {
        int eax, edx, ecx, edi, esi, ebp;
        byte []Copy = ByteFromIntArray.LITTLEENDIAN.getByteArray(keyTable);
        esi = 0;

        for(edi = 0; edi < 120; edi++)
        {
            eax = edi & 0x1F;
            ecx = esi & 0x1F;
            edx = 3 - (edi >>> 5);

//            ebp = *(DWORD *)((BYTE *)(Copy+3) - ((esi >> 5) << 2));  <-- original c++ code
            // could you convert that line to java?
            int location = 12 - ((esi >>> 5) << 2);
            ebp = IntFromByteArray.LITTLEENDIAN.getInteger(Copy, location);

            //System.out.print(PadString.padHex(ebp, 8) + " ");


            ebp = (ebp & (1 << ecx)) >>> ecx;
            keyTable[edx] = ((ebp & 1) << eax) | (~(1 << eax) & keyTable[edx]);
            esi += 0x0B;
            if(esi >= 120)
                esi -= 120;
        }
    }

    public int[] getKeyHash(int clientToken, int serverToken)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte [] warBuf = new byte[26];

            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 0, clientToken);
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 4, serverToken);
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 8, getProduct());
            IntFromByteArray.LITTLEENDIAN.insertInteger(warBuf, 12, getVal1());

            for(int i = 16; i < 26; i++)
                warBuf[i] = getWar3Val2()[i - 16];

            digest.update(warBuf);
            return IntFromByteArray.LITTLEENDIAN.getIntArray(digest.digest());

        }
        catch(java.security.NoSuchAlgorithmException e)
        {
            System.out.println("Could not find SHA1 library " + e);
            System.exit(1);
            return new int[1];
        }
    }

    public int getVal1()
    {
        return val1;
    }

    public byte[] getWar3Val2()
    {
        return val2;
    }

    public int getProduct()
    {
        return product;
    }
}
