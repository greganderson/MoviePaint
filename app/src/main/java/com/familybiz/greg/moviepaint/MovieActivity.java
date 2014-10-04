package com.familybiz.greg.moviepaint;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;


public class MovieActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    LinearLayout rootLayout = new LinearLayout(this);
        setContentView(rootLayout);
    }
}
