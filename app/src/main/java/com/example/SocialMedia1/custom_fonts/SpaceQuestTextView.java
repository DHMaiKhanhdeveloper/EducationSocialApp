package com.example.SocialMedia1.custom_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class SpaceQuestTextView extends AppCompatTextView {

    public SpaceQuestTextView(@NonNull Context context) {
        super(context);
        setFontsTextView();
    }

    public SpaceQuestTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextView();
    }

    public SpaceQuestTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextView();
    }
    private void setFontsTextView(){
        Typeface typeface = Utils.getTheSpaceQuestTypeface(getContext());
        setTypeface(typeface);
    }
}
