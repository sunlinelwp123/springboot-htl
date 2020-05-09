package com.open.boot.util.common;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class LangUtil {
    public LangUtil() {
    }

    public static RuntimeException wrapThrow(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException)e;
        } else {
            return e instanceof InvocationTargetException ? wrapThrow(((InvocationTargetException)e).getTargetException()) : new RuntimeException(e);
        }
    }

    public static RuntimeException wrapThrow(String message, Throwable e) {
        return new RuntimeException(message, e);
    }

    public static RuntimeException wrapThrow(String format, String... args) {
        throw new RuntimeException(String.format(format, args));
    }

    public static RuntimeException wrapThrow(String format, Throwable e, String... args) {
        return new RuntimeException(String.format(format, args), e);
    }

    public static Throwable unwrapThrow(Throwable e) {
        if (e == null) {
            return null;
        } else {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException itE = (InvocationTargetException)e;
                if (itE.getTargetException() != null) {
                    return unwrapThrow(itE.getTargetException());
                }
            }

            return e.getCause() != null ? unwrapThrow(e.getCause()) : e;
        }
    }

    public static Throwable unwrapRuntimeException(Throwable e) {
        if (e == null) {
            return null;
        } else {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException itE = (InvocationTargetException)e;
                if (itE.getTargetException() != null) {
                    return unwrapRuntimeException(itE.getTargetException());
                }
            }

            return e instanceof RuntimeException && e.getCause() != null ? unwrapRuntimeException(e.getCause()) : e;
        }
    }

    public static List<StackTraceElement> getStackTrace(Throwable t) {
        ArrayList ret;
        for(ret = new ArrayList(); t != null; t = t.getCause()) {
            StackTraceElement[] stes = t.getStackTrace();
            if (stes == null || stes.length == 0) {
                break;
            }

            ret.add(stes[0]);
        }

        return ret;
    }

    public static String getStackTraceMessage(Throwable t) {
        StringBuilder sb;
        for(sb = new StringBuilder(""); t != null; t = t.getCause()) {
            StackTraceElement[] stes = t.getStackTrace();
            if (stes == null || stes.length == 0) {
                break;
            }

            String message = t.getLocalizedMessage();
            sb.append(t.getClass().getName() + ":" + message + "\n\t" + "at " + stes[0].getMethodName() + "(" + stes[0].getFileName() + ":" + stes[0].getLineNumber() + ")");
            sb.append("\n");
            if (t.getCause() == null) {
                for(int i = 1; i < stes.length; ++i) {
                    sb.append("\tat " + stes[i].getMethodName() + "(" + stes[i].getFileName() + ":" + stes[i].getLineNumber() + ")");
                    sb.append("\n");
                }
            }
        }

        return sb.toString();
    }

    /*
    public static <T> T getException(Class<T> targetException, Throwable t) {
        if (t == null) {
            return null;
        } else {
            return targetException.isAssignableFrom(t.getClass()) ? t : getException(targetException, t.getCause());
        }
    }*/
}
