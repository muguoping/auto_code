package cn.com.icss.code.util;

import java.lang.Character.UnicodeBlock;

public class UnicodeTranslate
{
	public static void main(String[] args)
	{
		String s = "简介";
		String tt = gbEncoding(s);
		// String tt1 = "你好，我想给你说一个事情";
		System.out.println(decodeUnicode("\\u7b80\\u4ecb"));
		// System.out.println(decodeUnicode(tt1));
		//System.out.println(HTMLDecoder.decode("中国"));
		String s1 = "\u7b80\u4ecb";
		System.out.println(s.indexOf("\\"));
		
		
	}

	public static String gbEncoding(String gbString)
	{
		char[] utfBytes = gbString.toCharArray();
		String unicodeBytes = "";
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++)
		{
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2)
			{
				hexB = "00" + hexB;
			}
			unicodeBytes = unicodeBytes + "\\u" + hexB;
		}
		return unicodeBytes;
	}

	public static String decodeUnicode(String dataStr)
	{
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1)
		{
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1)
			{
				charStr = dataStr.substring(start + 2, dataStr.length());
			}
			else
			{
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	public String gbk2utf8(String gbk)
	{
		String l_temp = GBK2Unicode(gbk);
		l_temp = unicodeToUtf8(l_temp);

		return l_temp;
	}

	public String utf82gbk(String utf)
	{
		String l_temp = utf8ToUnicode(utf);
		l_temp = Unicode2GBK(l_temp);

		return l_temp;
	}

	/**
	 * 
	 * @param str
	 * @return String
	 */

	public static String GBK2Unicode(String str)
	{
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			char chr1 = (char) str.charAt(i);

			if (!isNeedConvert(chr1))
			{
				result.append(chr1);
				continue;
			}

			result.append("\\u" + Integer.toHexString((int) chr1));
		}

		return result.toString();
	}

	/**
	 * 
	 * @param dataStr
	 * @return String
	 */

	public static String Unicode2GBK(String dataStr)
	{
		int index = 0;
		StringBuffer buffer = new StringBuffer();

		int li_len = dataStr.length();
		while (index < li_len)
		{
			if (index >= li_len - 1 || !"\\u".equals(dataStr.substring(index, index + 2)))
			{
				buffer.append(dataStr.charAt(index));

				index++;
				continue;
			}

			String charStr = "";
			charStr = dataStr.substring(index + 2, index + 6);

			char letter = (char) Integer.parseInt(charStr, 16);

			buffer.append(letter);
			index += 6;
		}

		return buffer.toString();
	}

	public static boolean isNeedConvert(char para)
	{
		return ((para & (0x00FF)) != para);
	}

	/**
	 * utf-8 转unicode
	 * 
	 * @param inStr
	 * @return String
	 */
	public static String utf8ToUnicode(String inStr)
	{
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++)
		{
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN)
			{
				sb.append(myBuffer[i]);
			}
			else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
			{
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			}
			else
			{
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param theString
	 * @return String
	 */
	public static String unicodeToUtf8(String theString)
	{
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;)
		{
			aChar = theString.charAt(x++);
			if (aChar == '\\')
			{
				aChar = theString.charAt(x++);
				if (aChar == 'u')
				{
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++)
					{
						aChar = theString.charAt(x++);
						switch (aChar)
						{
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				}
				else
				{
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			}
			else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

}
