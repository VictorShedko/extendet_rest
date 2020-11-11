package com.epam.esm.gift_extended.genertors.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtil {
    private static final Random rand = new Random();
    public static <T> T getRandomElementFromList(List<T> list){
        return list.get(rand.nextInt(list.size()));
    }
    public static int getRandInt(int max){
        if(max>0) {
            return rand.nextInt(max);
        }else {
            return 0;
        }
    }

    public static float getRandFloat(){
        return rand.nextFloat();
    }
    public static boolean getBooleanWithTrueProbability(int probabilityPercent){
        return getRandInt(100)<probabilityPercent;
    }

    public static<T> List<T> getRandomSubListWithSize(int size,List<T> list){
        List<T> listToShuffle=new ArrayList<>(list);
        Collections.shuffle(list);
        return listToShuffle.subList(0,size);

    }

}
