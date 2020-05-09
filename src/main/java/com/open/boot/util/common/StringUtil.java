package com.open.boot.util.common;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    static final char LIST_ESC_CHAR = '\\';
    private static MessageDigest digest = null;

    public StringUtil() {
    }

    public static String nullable(Object obj, String defaultValue) {
        return isEmpty(obj) ? defaultValue : obj.toString();
    }

    public static String nullable(Object obj) {
        return nullable(obj, "");
    }

    public static boolean isBlank(Object obj) {
        return isEmptyOrBlank(obj, true);
    }

    public static boolean isEmpty(Object obj) {
        return isEmptyOrBlank(obj, false);
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmptyOrBlank(obj, false);
    }

    private static boolean isEmptyOrBlank(Object obj, boolean trim) {
        if (obj == null) {
            return true;
        } else if (obj instanceof String) {
            String ss = (String)obj;
            return (trim ? ss.trim() : ss).length() == 0;
        } else if (obj instanceof Object[]) {
            Object[] oo = (Object[])((Object[])obj);

            for(int i = 0; i < oo.length; ++i) {
                if (!isEmptyOrBlank(oo[i], trim)) {
                    return false;
                }
            }

            return true;
        } else if (obj instanceof Collection) {
            Collection<Object> oo = (Collection)obj;
            Iterator i = oo.iterator();

            do {
                if (!i.hasNext()) {
                    return true;
                }
            } while(isEmptyOrBlank(i.next(), trim));

            return false;
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }

    public static String[] splitIntoLines(String str) {
        if (str == null) {
            return null;
        } else {
            BufferedReader br = new BufferedReader(new StringReader(str));
            ArrayList linesList = new ArrayList();

            try {
                for(String line = br.readLine(); line != null; line = br.readLine()) {
                    linesList.add(line);
                }
            } catch (IOException var4) {
            }

            return (String[])((String[])linesList.toArray(new String[linesList.size()]));
        }
    }

    public static List<String> split(String s, char delimiter, boolean trim) {
        List<String> ret = new ArrayList();
        if (s == null) {
            return ret;
        } else {
            int lastIdx = 0;

            String s1;
            for(int idx = s.indexOf(delimiter); idx > 0; idx = s.indexOf(delimiter, lastIdx)) {
                s1 = s.substring(lastIdx, idx);
                if (trim) {
                    s1 = s1.trim();
                }

                ret.add(s1);
                lastIdx = idx + 1;
            }

            s1 = s.substring(lastIdx);
            if (trim) {
                s1 = s1.trim();
            }

            ret.add(s1);
            return ret;
        }
    }

    public static List<String> split(String s, char delimiter) {
        return split(s, delimiter, true);
    }

    public static List<String> split(String input) {
        return split(input, "[\\s,]+");
    }

    public static List<String> split(String input, String sep) {
        if (input == null) {
            return null;
        } else {
            int index = 0;
            List<String> matchList = new ArrayList();

            for(Matcher m = Pattern.compile(sep).matcher(input); m.find(); index = m.end()) {
                if (index < m.start()) {
                    String match = input.subSequence(index, m.start()).toString();
                    matchList.add(match);
                }
            }

            if (index < input.length()) {
                matchList.add(input.subSequence(index, input.length()).toString());
            }

            return matchList;
        }
    }

    public static final String replace(String input, String matchString, String newString) {
        int i = 0;
        if ((i = input.indexOf(matchString, i)) < 0) {
            return input;
        } else {
            char[] line2 = input.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = matchString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;

            int j;
            for(j = i; (i = input.indexOf(matchString, i)) > 0; j = i) {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
            }

            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
    }

    public static final String escapeHTMLTags(String input) {
        if (input != null && input.length() != 0) {
            StringBuffer buf = new StringBuffer(input.length());

            for(int i = 0; i < input.length(); ++i) {
                char ch = input.charAt(i);
                if (ch == '<') {
                    buf.append("&lt;");
                } else if (ch == '>') {
                    buf.append("&gt;");
                } else if (ch == '"') {
                    buf.append("&quot;");
                } else if (ch == '\'') {
                    buf.append("&apos;");
                } else if (ch == '&') {
                    buf.append("&amp;");
                } else if (ch == '\\') {
                    buf.append('\\');
                    buf.append(ch);
                } else if (ch == '\r') {
                    buf.append("\\r");
                } else if (ch == '\n') {
                    buf.append("\\n");
                } else {
                    buf.append(ch);
                }
            }

            return buf.toString();
        } else {
            return input;
        }
    }

    public static final String viewHTMLTags(String input) {
        if (input != null && input.length() != 0) {
            StringBuffer buf = new StringBuffer(input.length());
            for(int i = 0; i < input.length(); ++i) {
                char ch = input.charAt(i);
                if (ch == '<') {
                    buf.append("&lt;");
                } else if (ch == '>') {
                    buf.append("&gt;");
                } else if (ch == '"') {
                    buf.append("&quot;");
                } else if (ch == '\'') {
                    buf.append("&apos;");
                } else if (ch == '&') {
                    buf.append("&amp;");
                } else if (ch == '\\') {
                    buf.append(ch);
                } else if (ch == '\r') {
                    buf.append("<br>");
                } else if (ch != '\n') {
                    buf.append(ch);
                }
            }

            return buf.toString();
        } else {
            return input;
        }
    }

    public static final synchronized String hash(String data) {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var2) {
                System.err.println("Failed to load the MD5 MessageDigest. SCU will be unable to function normally.");
                var2.printStackTrace();
            }
        }

        digest.update(data.getBytes());
        return toHex(digest.digest());
    }

    public static final String toHex(byte[] hash) {
        StringBuffer buf = new StringBuffer(hash.length * 2);
        String stmp = "";

        for(int i = 0; i < hash.length; ++i) {
            stmp = Integer.toHexString(hash[i] & 255);
            if (stmp.length() == 1) {
                buf.append(0).append(stmp);
            } else {
                buf.append(stmp);
            }
        }

        return buf.toString();
    }

    public static final byte[] hexToBytes(String hex) {
        if (null == hex) {
            return new byte[0];
        } else {
            int len = hex.length();
            byte[] bytes = new byte[len / 2];
            String stmp = null;

            try {
                for(int i = 0; i < bytes.length; ++i) {
                    stmp = hex.substring(i * 2, i * 2 + 2);
                    bytes[i] = (byte)Integer.parseInt(stmp, 16);
                }

                return bytes;
            } catch (Exception var5) {
                return new byte[0];
            }
        }
    }

    public static final String escapeForXML(String input) {
        if (input != null && input.length() != 0) {
            char[] sArray = input.toCharArray();
            StringBuffer buf = new StringBuffer(sArray.length);

            for(int i = 0; i < sArray.length; ++i) {
                char ch = sArray[i];
                if (ch == '<') {
                    buf.append("&lt;");
                } else if (ch == '>') {
                    buf.append("&gt;");
                } else if (ch == '"') {
                    buf.append("&quot;");
                } else if (ch == '&') {
                    buf.append("&amp;");
                } else {
                    buf.append(ch);
                }
            }

            return buf.toString();
        } else {
            return input;
        }
    }

    public static final String unescapeFromXML(String input) {
        input = replace(input, "&lt;", "<");
        input = replace(input, "&gt;", ">");
        input = replace(input, "&quot;", "\"");
        return replace(input, "&amp;", "&");
    }

    public static final String compactSizeFormat(String number) {
        String[] end = new String[]{"B", "kB", "MB", "GB"};
        double num = 0.0D;
        int i = 0;

        try {
            num = (double)Integer.parseInt(number);
        } catch (Exception var6) {
            num = 0.0D;
        }

        while(num > 1024.0D && i < end.length) {
            num /= 1024.0D;
            ++i;
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(num) + " " + end[i];
    }

    public static String substr(String src, int beginIndex, int endIndex) {
        String dest = "";
        if (src == null) {
            return dest;
        } else {
            byte[] srcByte = src.getBytes();
            byte[] destByte ;
            int srclen = srcByte.length;
            if (srclen > beginIndex && beginIndex < endIndex) {
                if (srclen >= endIndex) {
                    destByte = new byte[endIndex - beginIndex];
                    System.arraycopy(srcByte, beginIndex, destByte, 0, endIndex - beginIndex);
                    dest = new String(destByte);
                    return dest;
                } else {
                    destByte = new byte[srclen - beginIndex];
                    System.arraycopy(srcByte, beginIndex, destByte, 0, srclen - beginIndex);
                    dest = new String(destByte);
                    return dest;
                }
            } else {
                return "";
            }
        }
    }

    public static String gbsubstr(String src, int beginIndex, int endIndex, boolean ifAdd) {
        String dest = "";
        dest = substr(src, beginIndex, endIndex);
        if (dest.length() == 0) {
            if (ifAdd) {
                dest = substr(src, beginIndex, endIndex + 1);
            } else {
                dest = substr(src, beginIndex, endIndex - 1);
            }
        }

        return dest;
    }

    public static String gbsubstr(String src, int beginIndex, int endIndex) {
        return gbsubstr(src, beginIndex, endIndex, false);
    }

    public static int gbStrLen(String str) {
        if (str == null) {
            return 0;
        } else {
            byte[] strByte;
            try {
                strByte = str.getBytes("GB18030");
            } catch (UnsupportedEncodingException var3) {
                strByte = str.getBytes();
            }

            return strByte.length;
        }
    }

    public static String replicateStr(char ch, int len) {
        String tmpstr = null;
        char[] tmparr = null;
        if (len <= 0) {
            return "";
        } else {
             tmparr = new char[len];

            for(int i = 0; i < len; ++i) {
                tmparr[i] = ch;
            }

            tmpstr = new String(tmparr);
            return tmpstr;
        }
    }

    public static String lFillStr(String src, char ch, int len) {
        String dest = src;
        int srclen = gbStrLen(src);
        if (srclen > len) {
            dest = gbsubstr(src, 0, len);
            srclen = gbStrLen(dest);
        }

        dest = dest + replicateStr(ch, len - srclen);
        return dest;
    }

    public static String rFillStr(String src, char ch, int len) {
        return rFillStr(src, ch, len, false);
    }

    public static String rFillStr(String src, char ch, int len, boolean gb) {
        String dest = src;
        int srclen = gb ? gbStrLen(src) : src.length();
        if (srclen > len) {
            dest = gbsubstr(src, 0, len);
            srclen = gb ? gbStrLen(dest) : dest.length();
        }

        dest = replicateStr(ch, len - srclen) + dest;
        return dest;
    }

    public static String maxstr(String s, int maxlength) {
        return maxstr(s, "UTF-8", maxlength);
    }

    public static String maxstr(String s, String encoding, int maxlength) {
        if (s == null) {
            return "";
        } else {
            try {
                byte[] bytes = encoding == null ? s.getBytes() : s.getBytes(encoding);
                return bytes.length <= maxlength ? s : new String(bytes, 0, maxlength - 3, encoding) + "...";
            } catch (UnsupportedEncodingException var4) {
                return null;
            }
        }
    }

    public static boolean equals(String val1, String val2) {
        if (val1 == null) {
            return val2 == null;
        } else {
            return val1.equals(val2);
        }
    }

    public static String getDefaultCharacterEncoding() {
        String charEnc = System.getProperty("file.encoding");
        if (charEnc != null) {
            return charEnc;
        } else {
            charEnc = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
            return charEnc != null ? charEnc : "<unknown charset encoding>";
        }
    }

    public static String capitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

    public static String unCapitalize(String name) {
        if (name != null && name.length() != 0) {
            char[] chars = name.toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            return new String(chars);
        } else {
            return name;
        }
    }

    /*
    public static String replaceProperties(String value, Map staticProp, PropertySource[] dynamicProp) {
        StringBuffer sb = new StringBuffer();
        int prev = 0;

        int pos;
        while((pos = value.indexOf("$", prev)) >= 0) {
            if (pos > 0) {
                sb.append(value.substring(prev, pos));
            }

            if (pos == value.length() - 1) {
                sb.append('$');
                prev = pos + 1;
                break;
            }

            if (value.charAt(pos + 1) != '{') {
                sb.append('$');
                prev = pos + 1;
            } else {
                int endName = value.indexOf(125, pos);
                if (endName < 0) {
                    sb.append(value.substring(pos));
                    prev = value.length();
                } else {
                    String n = value.substring(pos + 2, endName);
                    String v = null;
                    if (n != null && staticProp != null && staticProp.get(n) != null) {
                        v = staticProp.get(n).toString();
                    }

                    if (n != null && v == null && dynamicProp != null) {
                        for(int i = 0; i < dynamicProp.length; ++i) {
                            v = dynamicProp[i].getProperty(n);
                            if (v != null) {
                                break;
                            }
                        }
                    }

                    if (v == null) {
                        v = "${" + n + "}";
                    }

                    sb.append(v);
                    prev = endName + 1;
                }
            }
        }

        if (prev < value.length()) {
            sb.append(value.substring(prev));
        }

        return sb.toString();
    }*/

    public static int getStrLenByEncoding(String str, String dbEncoding) {
        if (str == null) {
            return 0;
        } else {
            byte[] strByte;
            try {
                strByte = str.getBytes(dbEncoding);
            } catch (UnsupportedEncodingException var4) {
                strByte = str.getBytes();
            }

            return strByte.length;
        }
    }

    public static int getStrLen(String str) {
        return str == null ? 0 : str.length();
    }
}
