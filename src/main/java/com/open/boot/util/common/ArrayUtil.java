package com.open.boot.util.common;

import java.lang.reflect.Array;
import java.util.Collection;

public class ArrayUtil {
    public ArrayUtil() {
    }

    /*
        数组比较相等
     */
    public static boolean arrayEquals(Object[] a1, Object[] a2) {
        return arrayEquals(a1, a2, (Object)null);
    }

    /*
        数组比较相等带 通配符(a1)
     */
    public static boolean arrayEquals(Object[] a1, Object[] a2, Object wildcardValue) {
        if (a1 == null && a2 == null) {
            return true;
        } else if (a1 != null && a2 != null) {
            if (a1.length != a2.length) {
                return false;
            } else {
                for(int i = 0; i < a1.length; ++i) {
                    if ((a1[i] != null || a2[i] != null) && (a1[i] == null || !a1[i].equals(wildcardValue))) {
                        if (a1[i] == null || a2[i] == null) {
                            return false;
                        }

                        if (!a1[i].equals(a2[i])) {
                            return false;
                        }
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }
    /*
           数组不包含
     */
    public static boolean notIn(Object o, Object arr) {
        return !in(o, arr);
    }
    /*
       数组包含
     */
    public static boolean in(Object o, Object arr) {
        if (o != null && arr != null) {
            Object[] os = asArray(o);
            Object[] os2 = asArray(arr);

            for(int i = 0; i < os.length; ++i) {
                for(int j = 0; j < os2.length; ++j) {
                    if (os[i] instanceof String && os2[j] instanceof String) {
                        if (((String)os[i]).trim().equals(((String)os2[j]).trim())) {
                            return true;
                        }
                    } else if (os[i] instanceof String && os2[j] instanceof Boolean) {
                        if (os[i].equals(os2[j].toString())) {
                            return true;
                        }
                    } else if (os[i].equals(os2[j])) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }

    /*
        默认逗号合成字符串
     */
    public static String join(Object o) {
        return join(o, ",");
    }
     /*
        合成字符串带分隔符
     */
    public static String join(Object o, String split) {
        if (o == null) {
            return null;
        } else {
            Object[] arr = asArray(o);
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < arr.length; ++i) {
                sb.append(arr[i]).append(split);
            }

            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    /*
      对象拆分转对象数组
     */
    public static Object[] asArray(Object o) {
        return asArray(o, true);
    }

    /*
      对象转对象数组，是否拆分标识
     */
    public static Object[] asArray(Object o, boolean splitString) {
        if (o == null) {
            return new Object[0];
        } else if (o instanceof Collection) {
            Collection<?> c = (Collection)o;
            return c.toArray(new Object[c.size()]);
        } else if (o instanceof String[] && ((String[])((String[])o)).length == 1) {
            return splitString ? ((String[])((String[])o))[0].split(",") : (String[])((String[])o);
        } else if (!o.getClass().isArray()) {
            if (o instanceof String && splitString) {
                String s = (String)o;
                return StringUtil.split(s).toArray();
            } else {
                return new Object[]{o};
            }
        } else {
            Object[] ret = new Object[Array.getLength(o)];

            for(int i = 0; i < ret.length; ++i) {
                ret[i] = Array.get(o, i);
            }

            return ret;
        }
    }

    /*
        找对象在数组中的下标
     */
    public static int indexOf(Object[] array, Object match) {
        int ret = -1;

        for(int i = 0; i < array.length; ++i) {
            if (array[i] != null && array[i].equals(match)) {
                return i;
            }
        }

        return ret;
    }

}
