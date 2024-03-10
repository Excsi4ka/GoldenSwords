package com.excsi.util;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class GoldenSwordsConfig{
public static Configuration config;
public static int potionId;
    public static void init(File file){
        config = new Configuration(file);
        sync();
    }
    public static void sync(){
        GoldenSwordsConfig.potionId=config.getInt("potionID","goldenswords.main",128,0,Integer.MAX_VALUE,"ID of the Knight's Burden potion");
        config.save();
    }
}