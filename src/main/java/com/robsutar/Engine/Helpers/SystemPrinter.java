package com.robsutar.Engine.Helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Date;

import static com.robsutar.Engine.Helpers.FileManager.resourcesPath;

public abstract class SystemPrinter implements PrintColor {

    public static final String failed = RED_BOLD+"failed ";
    public static final String loading = GREEN_BOLD+"Loading file: "+BLUE;
    public static final String failedToLoad = failed+"to load file: "+RED;
    public static final String writing = PURPLE_BOLD+"Writing file: "+GREEN;
    public static final String failedToWrite = failed+"to write file: "+RED;
    public static final String deleting = RED_BOLD_BRIGHT+"Deleting file: "+RED_BRIGHT;
    public static final String failedToDelete= failed+"to delete file "+RED;

    static boolean privacy = false;

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void print(Object o){
        String x = o.toString();
        LocalDateTime now = LocalDateTime.now();
        String replacement = YELLOW+dtf.format(now)+WHITE_BRIGHT+" ⛣ ";
        if (privacy){
            replacement+=(x.replace(resourcesPath,"resources\\"+"\033[1;97m"));
        }else {
            replacement+=x;
        }
        replacement+=RESET;
        System.out.println();
        System.out.print(replacement);
    }
    public static void write(Object o){
        String x = o.toString();
        String replacement;
        if (privacy){
            replacement=(x.replace(resourcesPath,"resources\\"+"\033[1;97m"));
        }else {
            replacement=x;
        }
        replacement+=RESET;
        System.out.print("");
        System.out.print(replacement);
    }
}
