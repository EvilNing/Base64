package com.example.ning.base64;

import android.icu.util.MeasureUnit;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.charset.Charset;

import static android.icu.util.MeasureUnit.BYTE;

class Base64Test {

	public static Charset UTF8_CHARSET = Charset.forName("UTF-8");
	public static byte[] charArray = "Q8vN-ryaEJGoTWOtK_qMkh5RZ6LxcUA3dnzeHu2XjSbVsFYwfPD94C0lm1Ip7gBi".getBytes(UTF8_CHARSET);
	
	public static int hashIndex[] = {
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
			4, 0xFFFFFFFF, 0xFFFFFFFF, 54, 57, 38, 31, 52, 22,
			25, 60, 1, 51, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF,
            0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 30,
            62, 53, 50, 8, 45, 10, 36, 58, 9, 16, 26, 19, 3, 14,
            49, 0, 23, 41, 12, 29, 43, 13, 39, 46, 24, 0xFFFFFFFF,
            0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 17, 0xFFFFFFFF,
			7, 42, 28, 32, 35, 48, 61, 21, 63, 40, 20, 55, 56,
            33, 11, 59, 18, 5, 44, 15, 37, 2, 47, 27, 6, 34, 0xFFFFFFFF,
            0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF
		};

	@RequiresApi(api = Build.VERSION_CODES.N)
    public static String decode(String inStr) {
		byte[] in = inStr.getBytes(UTF8_CHARSET);
		byte[] decBytes = new byte[in.length];
        int i = 0;
        int j = 0;
        int inLen = in.length;

        do
        {
            if ((inLen - i) <= 3) {
                break;
            }
            int iTmp1 = (hashIndex[in[i+1]] >> 4) & 3 & 0xFF;
            int iTmp2 = 4 * (hashIndex[in[i+0]] & 0xFF);
            decBytes[j+0] = (byte) (iTmp1 | iTmp2);
            decBytes[j+1] = (byte) ((hashIndex[in[i+2]] >> 2) & 0xF | 16 * (hashIndex[in[i+1]] & 0xFF));
            int iIdx = in[i+2] & 0xFF;
            int jIdx = in[i+3] & 0xFF;
            decBytes[j+2] = (byte) (((hashIndex[iIdx] & 0xFF) << 6) | (hashIndex[jIdx] & 0xFF));
            j += 3;
            i += 4;
        } while (true);

        if ((inLen - i) == 3)
        {
            decBytes[j+0] = (byte) ((hashIndex[in[i+1]] >> 4) & 3 | 4 * (hashIndex[in[i+0]] & 0xFF));
            decBytes[j+1] = (byte) ((hashIndex[in[i+2]] << 26 >> 28) | 16 * (hashIndex[in[i+1]] & 0xFF));
            decBytes[j+2] = (byte) ((hashIndex[in[i+2]] & 0xFF) << 6);
            j += 3;
        }
        else if ((inLen - i) == 2)
        {
            decBytes[j+0] = (byte) ((hashIndex[in[i+1]] << 26 >> 30) | 4 * (hashIndex[in[i+0]] & 0xFF));
            decBytes[j+1] = (byte) (16 * (hashIndex[in[i+1]] & 0xFF));
            j += 2;
        }
        else if ((inLen - i) == 1)
        {
            decBytes[j+0] = (byte) (4 * (hashIndex[in[i+0]] & 0xFF));
            j += 1;
        }
        if (j < 1) {
            return new String("");
        }

        return new String(decBytes, 0, j - 2, UTF8_CHARSET);
	}

	public static String encode(String inStr) {
        byte[] in = inStr.getBytes(UTF8_CHARSET);
        System.out.println("raw len:" + in.length);
        byte[] encBytes = new byte[in.length * 2 + 4];

        int i = 0;
        int j = 0;
        int inLen = in.length;

        do
        {
            if ((inLen - i) <= 2) {
                break;
            }
            int b0 = 0x00FF & in[i+0];
            int b1 = 0x00FF & in[i+1];
            int b2 = 0x00FF & in[i+2];
            int idx = b0 >> 2;
            encBytes[j+0] = charArray[idx & 0x3F];
            idx = (b1 >> 4) | ((b0 << 4) & 0x30);
            encBytes[j+1] = charArray[idx & 0x3F];
            idx = (b2 >> 6) | ((b1 << 2) & 0x3C);
            encBytes[j+2] = charArray[idx & 0x3F];
            encBytes[j+3] = charArray[b2 & 0x3F];
            i += 3;
            j += 4;
        } while (true);

        if ((inLen - i) > 0)
        {
            int b0 = 0x00FF & in[i+0];
            encBytes[j+0] = charArray[b0 >> 2];
            if ((inLen - i) == 2)
            {
                int b1 = 0x00FF & in[i+1];
                encBytes[j+1] = charArray[(b0 << 4) & 0x30 | (b1 >> 4)];
                encBytes[j+2] = charArray[(b1 << 2) & 0x3C];
                j += 3;
            }
            else
            {
                encBytes[j+1] = charArray[(b0 << 4) & 0x30];
                j += 2;
            }
        }
        if (j < 1) {
            return new String("");
        }

        System.out.println("enc len:" + j);
        return new String(encBytes, 0, j, UTF8_CHARSET);
	}
}
