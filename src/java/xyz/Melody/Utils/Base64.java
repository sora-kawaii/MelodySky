package xyz.Melody.Utils;

public final class Base64 {
   private static final int BASELENGTH = 128;
   private static final int LOOKUPLENGTH = 64;
   private static final int TWENTYFOURBITGROUP = 24;
   private static final int EIGHTBIT = 8;
   private static final int SIXTEENBIT = 16;
   private static final int SIXBIT = 6;
   private static final int FOURBYTE = 4;
   private static final int SIGN = -128;
   private static final char PAD = '=';
   private static final boolean fDebug = false;
   private static final byte[] base64Alphabet = new byte[128];
   private static final char[] lookUpBase64Alphabet = new char[64];

   protected static boolean isWhiteSpace(char octect) {
      return octect == ' ' || octect == '\r' || octect == '\n' || octect == '\t';
   }

   protected static boolean isPad(char octect) {
      return octect == '=';
   }

   protected static boolean isData(char octect) {
      return octect < 128 && base64Alphabet[octect] != -1;
   }

   protected static boolean isBase64(char octect) {
      return isWhiteSpace(octect) || isPad(octect) || isData(octect);
   }

   public static String encode(byte[] binaryData) {
      if (binaryData == null) {
         return null;
      } else {
         int lengthDataBits = binaryData.length * 8;
         if (lengthDataBits == 0) {
            return "";
         } else {
            int fewerThan24bits = lengthDataBits % 24;
            int numberTriplets = lengthDataBits / 24;
            int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
            char[] encodedData = null;
            char[] encodedData = new char[numberQuartet * 4];
            byte k = false;
            byte l = false;
            byte b1 = false;
            byte b2 = false;
            byte b3 = false;
            int encodedIndex = 0;
            int dataIndex = 0;

            byte val2;
            byte k;
            byte l;
            byte b1;
            byte b2;
            for(int i = 0; i < numberTriplets; ++i) {
               b1 = binaryData[dataIndex++];
               b2 = binaryData[dataIndex++];
               byte b3 = binaryData[dataIndex++];
               l = (byte)(b2 & 15);
               k = (byte)(b1 & 3);
               val2 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
               byte val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
               byte val3 = (b3 & -128) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 252);
               encodedData[encodedIndex++] = lookUpBase64Alphabet[val2];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2 | val3];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 63];
            }

            byte val1;
            if (fewerThan24bits == 8) {
               b1 = binaryData[dataIndex];
               k = (byte)(b1 & 3);
               val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
               encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
               encodedData[encodedIndex++] = '=';
               encodedData[encodedIndex++] = '=';
            } else if (fewerThan24bits == 16) {
               b1 = binaryData[dataIndex];
               b2 = binaryData[dataIndex + 1];
               l = (byte)(b2 & 15);
               k = (byte)(b1 & 3);
               val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
               val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
               encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | k << 4];
               encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
               encodedData[encodedIndex++] = '=';
            }

            return new String(encodedData);
         }
      }
   }

   public static byte[] decode(String encoded) {
      if (encoded == null) {
         return null;
      } else {
         char[] base64Data = encoded.toCharArray();
         int len = removeWhiteSpace(base64Data);
         if (len % 4 != 0) {
            return null;
         } else {
            int numberQuadruple = len / 4;
            if (numberQuadruple == 0) {
               return new byte[0];
            } else {
               byte[] decodedData = null;
               byte b1 = false;
               byte b2 = false;
               byte b3 = false;
               byte b4 = false;
               char d1 = false;
               char d2 = false;
               char d3 = false;
               char d4 = false;
               int i = 0;
               int encodedIndex = 0;
               int dataIndex = 0;

               byte[] decodedData;
               byte b1;
               byte b2;
               byte b3;
               byte b4;
               char d1;
               char d2;
               char d3;
               char d4;
               for(decodedData = new byte[numberQuadruple * 3]; i < numberQuadruple - 1; ++i) {
                  if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++]) || !isData(d3 = base64Data[dataIndex++]) || !isData(d4 = base64Data[dataIndex++])) {
                     return null;
                  }

                  b1 = base64Alphabet[d1];
                  b2 = base64Alphabet[d2];
                  b3 = base64Alphabet[d3];
                  b4 = base64Alphabet[d4];
                  decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                  decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                  decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
               }

               if (isData(d1 = base64Data[dataIndex++]) && isData(d2 = base64Data[dataIndex++])) {
                  b1 = base64Alphabet[d1];
                  b2 = base64Alphabet[d2];
                  d3 = base64Data[dataIndex++];
                  d4 = base64Data[dataIndex++];
                  if (isData(d3) && isData(d4)) {
                     b3 = base64Alphabet[d3];
                     b4 = base64Alphabet[d4];
                     decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                     decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                     decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
                     return decodedData;
                  } else {
                     byte[] tmp;
                     if (isPad(d3) && isPad(d4)) {
                        if ((b2 & 15) != 0) {
                           return null;
                        } else {
                           tmp = new byte[i * 3 + 1];
                           System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                           tmp[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                           return tmp;
                        }
                     } else if (!isPad(d3) && isPad(d4)) {
                        b3 = base64Alphabet[d3];
                        if ((b3 & 3) != 0) {
                           return null;
                        } else {
                           tmp = new byte[i * 3 + 2];
                           System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                           tmp[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                           tmp[encodedIndex] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                           return tmp;
                        }
                     } else {
                        return null;
                     }
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   protected static int removeWhiteSpace(char[] data) {
      if (data == null) {
         return 0;
      } else {
         int newSize = 0;
         int len = data.length;

         for(int i = 0; i < len; ++i) {
            if (!isWhiteSpace(data[i])) {
               data[newSize++] = data[i];
            }
         }

         return newSize;
      }
   }

   static {
      int i;
      for(i = 0; i < 128; ++i) {
         base64Alphabet[i] = -1;
      }

      for(i = 90; i >= 65; --i) {
         base64Alphabet[i] = (byte)(i - 65);
      }

      for(i = 122; i >= 97; --i) {
         base64Alphabet[i] = (byte)(i - 97 + 26);
      }

      for(i = 57; i >= 48; --i) {
         base64Alphabet[i] = (byte)(i - 48 + 52);
      }

      base64Alphabet[43] = 62;
      base64Alphabet[47] = 63;

      for(i = 0; i <= 25; ++i) {
         lookUpBase64Alphabet[i] = (char)(65 + i);
      }

      i = 26;

      int j;
      for(j = 0; i <= 51; ++j) {
         lookUpBase64Alphabet[i] = (char)(97 + j);
         ++i;
      }

      i = 52;

      for(j = 0; i <= 61; ++j) {
         lookUpBase64Alphabet[i] = (char)(48 + j);
         ++i;
      }

      lookUpBase64Alphabet[62] = '+';
      lookUpBase64Alphabet[63] = '/';
   }
}
