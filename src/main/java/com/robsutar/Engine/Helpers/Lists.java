package com.robsutar.Engine.Helpers;

import java.util.Collection;

public class Lists {
    private Lists(){}

    public static boolean indexInBounds(Collection<?> collection,int index){
        return index >= 0 && index < collection.size();
    }

    public static <T> T getSecureObject(Collection<T> collection, int index){
        if (indexInBounds(collection,index)){
            return ((T) collection.toArray()[index]);
        }
        return null;
    }

    public static <T> T getRotationObject(Collection<T> collection, int index){
        if (indexInBounds(collection,index)){
            return ((T) collection.toArray()[index]);
        }
        int multiplication = collection.size()/index;
        return (T) collection.toArray()[index-index*multiplication];
    }
}
