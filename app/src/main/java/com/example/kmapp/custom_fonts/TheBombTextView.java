package com.example.kmapp.custom_fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class TheBombTextView extends AppCompatTextView {
    public TheBombTextView(@NonNull Context context) {
        super(context);
        setFontsTextView();
    }

    public TheBombTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFontsTextView();
    }

    public TheBombTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFontsTextView();
    }
    private void setFontsTextView(){
        Typeface typeface = Utils.getTheBombTypeface(getContext());
        setTypeface(typeface);
    }
}
