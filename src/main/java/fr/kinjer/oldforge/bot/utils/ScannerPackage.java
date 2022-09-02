package fr.kinjer.oldforge.bot.utils;

import org.reflections.Reflections;

import java.util.Set;

public class ScannerPackage {

    public static <T> Set<Class<? extends T>> getClassOfPackage(Class<T> clazz, String path){
        try {
            return new Reflections(path).getSubTypesOf(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
