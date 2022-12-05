package com.example.kmapp.custom_fonts;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {

    private static Typeface theBombTypeface;
    private static Typeface theSpaceQuestTypeface;

    public static Typeface getTheBombTypeface(Context context) {
        if(theBombTypeface == null){
            theBombTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/TheBomb-7B9gw.ttf");
        }
        return theBombTypeface;
    }

    public static void setTheBombTypeface(Typeface theBombTypeface) {
        Utils.theBombTypeface = theBombTypeface;
    }



    public static Typeface getTheSpaceQuestTypeface(Context context) {
        if(theBombTypeface == null){
            theBombTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/SpaceQuest-Xj4o.ttf");
        }
        return theSpaceQuestTypeface;
    }

    public static void setTheSpaceQuestTypeface(Typeface theSpaceQuestTypeface) {
        Utils.theSpaceQuestTypeface = theSpaceQuestTypeface;
    }
}
