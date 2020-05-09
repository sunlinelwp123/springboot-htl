package com.open.boot.util.internal;


public interface DefaultEnum<T>  {
    String getId();

    T getValue();

    String getLongName();

    String toString();
}