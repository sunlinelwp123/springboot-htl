package com.open.boot.util.common;


import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Convert {
    private static final String HEX_PREFIX = "0x";
    private static final int HEX_RADIX = 16;
    private static final Class[] CONSTR_ARGS = new Class[]{String.class};

    public Convert() {
    }

    public static Boolean toBoolean(Object value, Boolean defaultVal) {
        if (value == null) {
            return defaultVal;
        } else if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            return "true".equalsIgnoreCase((String)value) ? Boolean.TRUE : Boolean.FALSE;
        } else {
            return defaultVal;
        }
    }

    public static Boolean toBoolean(Object value) {
        return toBoolean(value, Boolean.FALSE);
    }

    public static Date toDate(Object value, String pattern) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof Date) {
            return (Date)value;
        } else if (value instanceof Calendar) {
            return ((Calendar)value).getTime();
        } else if (value instanceof String) {
            String strVal = (String)value;
            if (strVal.trim().length() == 0) {
                return null;
            } else {
                try {
                    DateFormat _formater = new SimpleDateFormat(pattern);
                    Date _date = _formater.parse((String)value);
                    if (strVal.equals(_formater.format(_date))) {
                        return _date;
                    } else {
                        throw new IllegalArgumentException("模式:[" + pattern + "]与时间串:[" + value + "]不符");
                    }
                } catch (Exception var5) {
                    throw new IllegalArgumentException("不能使用模式:[" + pattern + "]格式化时间串:[" + value + "]");
                }
            }
        } else {
            throw new IllegalArgumentException("不能使用模式:[" + pattern + "]格式化未知对象:[" + value + "]" + value.getClass().getName());
        }
    }

    public static Date toDate(Object value, String pattern, Date defaultVal) {
        Date ret = null;

        try {
            ret = toDate(value, pattern);
        } catch (IllegalArgumentException var5) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Date toDate(Object value) {
        return toDate(value, "yyyyMMdd", (Date)null);
    }

    public static Calendar toCalendar(Object value, String pattern) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof Calendar) {
            return (Calendar)value;
        } else if (value instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)value);
            return calendar;
        } else if (value instanceof String) {
            String strVal = (String)value;
            if (strVal.trim().length() == 0) {
                return null;
            } else {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime((new SimpleDateFormat(pattern)).parse(strVal));
                    return calendar;
                } catch (ParseException var4) {
                    throw new IllegalArgumentException("The value " + value + " can't be converted to a Calendar");
                }
            }
        } else {
            throw new IllegalArgumentException("The value " + value + " can't be converted to a Calendar");
        }
    }

    public static Timestamp toTimestamp(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Timestamp) {
            return (Timestamp)value;
        } else {
            Date _date = toDate(value, "yyyy-MM-dd HH:mm:ss", (Date)null);
            return _date == null ? null : new Timestamp(_date.getTime());
        }
    }

    public static java.sql.Date toSqlDate(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof java.sql.Date) {
            return (java.sql.Date)value;
        } else {
            Date _date = toDate(value, "yyyy-MM-dd", (Date)null);
            return _date == null ? null : new java.sql.Date(_date.getTime());
        }
    }

    public static Time toSqlTime(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Time) {
            return (Time)value;
        } else {
            Date _date = toDate(value, "yyyy-MM-dd HH:mm:ss", (Date)null);
            return _date == null ? null : new Time(_date.getTime());
        }
    }

    static Number toNumber(Object value, Class targetClass) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return (Number)value;
        } else {
            String str = value.toString();
            if (str.startsWith("0x")) {
                try {
                    return new BigInteger(str.substring("0x".length()), 16);
                } catch (NumberFormatException var4) {
                    throw new IllegalArgumentException("Could not convert '" + str + "' to " + targetClass.getName() + "! Invalid hex number.");
                }
            } else {
                try {
                    Constructor constr = targetClass.getConstructor(CONSTR_ARGS);
                    return (Number)constr.newInstance(str);
                } catch (InvocationTargetException var5) {
                    throw new IllegalArgumentException("Could not convert '" + str + "' to " + targetClass.getName());
                } catch (Exception var6) {
                    throw new IllegalArgumentException("Conversion error when trying to convert " + str + " to " + targetClass.getName());
                }
            }
        }
    }

    public static Byte toByte(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Byte.class);
            return n instanceof Byte ? (Byte)n : new Byte(n.byteValue());
        }
    }

    public static Byte toByte(Object value, Byte defaultVal) {
        Byte ret = null;

        try {
            ret = toByte(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Short toShort(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Short.class);
            return n instanceof Short ? (Short)n : new Short(n.shortValue());
        }
    }

    public static Short toShort(Object value, Short defaultVal) {
        Short ret = null;

        try {
            ret = toShort(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Float toFloat(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Float.class);
            return n instanceof Float ? (Float)n : new Float(n.floatValue());
        }
    }

    public static Float toFloat(Object value, Float defaultVal) {
        Float ret = null;

        try {
            ret = toFloat(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Long toLong(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Long.class);
            return n instanceof Long ? (Long)n : new Long(n.longValue());
        }
    }

    public static Long toLong(Object value, Long defaultVal) {
        Long ret = null;

        try {
            ret = toLong(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Integer toInteger(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Integer.class);
            return n instanceof Integer ? (Integer)n : new Integer(n.intValue());
        }
    }

    public static Integer toInteger(Object value, Integer defaultVal) {
        Integer ret = null;

        try {
            ret = toInteger(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static Double toDouble(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, Double.class);
            return n instanceof Double ? (Double)n : new Double(n.doubleValue());
        }
    }

    public static Double toDouble(Object value, Double defaultVal) {
        Double ret = null;

        try {
            ret = toDouble(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static BigDecimal toBigDecimal(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else {
            Number n = toNumber(value, BigDecimal.class);
            return n instanceof BigDecimal ? (BigDecimal)n : new BigDecimal(n.doubleValue());
        }
    }

    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultVal) {
        BigDecimal ret = null;

        try {
            ret = toBigDecimal(value);
        } catch (Exception var4) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static BigDecimal toBigDecimal(Object value, String pattern) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        } else if (value instanceof Number) {
            return new BigDecimal(((Number)value).doubleValue());
        } else {
            String str = value.toString();
            DecimalFormat df = new DecimalFormat(pattern);

            Number num;
            try {
                num = df.parse(str);
            } catch (ParseException var7) {
                throw new IllegalArgumentException("不能使用模式:[" + pattern + "]解析数字串:[" + value + "]");
            }

            BigDecimal ret;
            if (num instanceof BigDecimal) {
                ret = (BigDecimal)num;
            } else {
                ret = new BigDecimal(num.doubleValue());
            }

            return ret;
        }
    }

    public static BigDecimal toBigDecimal(Object value, String pattern, BigDecimal defaultVal) {
        BigDecimal ret = null;

        try {
            ret = toBigDecimal(value, pattern);
        } catch (Exception var5) {
            ret = defaultVal;
        }

        return ret == null ? defaultVal : ret;
    }

    public static BigDecimal toAmount(Object value) {
        return toBigDecimal(value, "#,##0.##", (BigDecimal)null);
    }

    public static byte[] charsToBytes(char[] source, int srclen) {
        if (source == null) {
            return null;
        } else {
            int len = source.length;
            if (len > srclen) {
                len = srclen;
            }

            byte[] dest = new byte[len];

            for(int i = 0; i < len; ++i) {
                dest[i] = (byte)source[i];
            }

            return dest;
        }
    }

    public static char[] bytesToChars(byte[] source, int srclen) {
        if (source == null) {
            return null;
        } else {
            int len = source.length;
            if (len > srclen) {
                len = srclen;
            }

            char[] destChar = new char[len];

            for(int i = 0; i < len; ++i) {
                if (source[i] >= 0) {
                    destChar[i] = (char)source[i];
                } else {
                    destChar[i] = (char)(256 + source[i]);
                }
            }

            return destChar;
        }
    }

    public static Locale toLocale(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof Locale) {
            return (Locale)value;
        } else if (!(value instanceof String)) {
            throw new IllegalArgumentException("The value " + value + " can't be converted to a Locale");
        } else {
            List<String> elements = StringUtil.split((String)value, '_');
            int size = elements.size();
            if (size >= 1 && (((String)elements.get(0)).length() == 2 || ((String)elements.get(0)).length() == 0)) {
                String language = (String)elements.get(0);
                String country = (String)((String)(size >= 2 ? elements.get(1) : ""));
                String variant = (String)((String)(size >= 3 ? elements.get(2) : ""));
                return new Locale(language, country, variant);
            } else {
                throw new IllegalArgumentException("The value " + value + " can't be converted to a Locale");
            }
        }
    }

    public static Color toColor(Object value) throws IllegalArgumentException {
        if (value == null) {
            return null;
        } else if (value instanceof Color) {
            return (Color)value;
        } else if (value instanceof String && !StringUtil.isBlank((String)value)) {
            String color = ((String)value).trim();
            int[] components = new int[3];
            int minlength = components.length * 2;
            if (color.length() < minlength) {
                throw new IllegalArgumentException("The value " + value + " can't be converted to a Color");
            } else {
                if (color.startsWith("#")) {
                    color = color.substring(1);
                }

                try {
                    int alpha;
                    for(alpha = 0; alpha < components.length; ++alpha) {
                        components[alpha] = Integer.parseInt(color.substring(2 * alpha, 2 * alpha + 2), 16);
                    }

                    if (color.length() >= minlength + 2) {
                        alpha = Integer.parseInt(color.substring(minlength, minlength + 2), 16);
                    } else {
                        alpha = Color.black.getAlpha();
                    }

                    return new Color(components[0], components[1], components[2], alpha);
                } catch (Exception var5) {
                    throw new IllegalArgumentException("The value " + value + " can't be converted to a Color");
                }
            }
        } else {
            throw new IllegalArgumentException("The value " + value + " can't be converted to a Color");
        }
    }
}