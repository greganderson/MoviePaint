package com.familybiz.greg.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class PaintActivity extends Activity {

	private PaintAreaView mPaintArea;
	static final int PICK_COLOR_REQUEST = 1;
	static final int WATCH_MOVIE = 2;
	private Button mColorChangeButton;

	private int[] mListOfColors = {
			Color.BLACK,
			Color.WHITE,
			Color.RED,
			Color.YELLOW,
			Color.BLUE,
			Color.GREEN
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);

		mPaintArea = new PaintAreaView(this);

		mPaintArea.setColor(Color.RED);

	    LinearLayout controls = new LinearLayout(this);
	    controls.setOrientation(LinearLayout.HORIZONTAL);
	    mColorChangeButton = new Button(this);
	    mColorChangeButton.setBackgroundColor(mPaintArea.getColor());
	    mColorChangeButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Intent paletteIntent = new Intent();
			    paletteIntent.putExtra(PaletteActivity.SELECTED_COLOR, mPaintArea.getColor());
			    paletteIntent.putExtra(PaletteActivity.LIST_OF_COLORS, mListOfColors);
			    paletteIntent.setClass(PaintActivity.this, PaletteActivity.class);
			    startActivityForResult(paletteIntent, PICK_COLOR_REQUEST);
		    }
	    });

	    Button watchButton = new Button(this);
	    watchButton.setText("Watch!");
	    watchButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Intent movieIntent = new Intent();
			    movieIntent.putParcelableArrayListExtra(MovieActivity.POINT_LIST, mPaintArea.getPointList());
			    movieIntent.setClass(PaintActivity.this, MovieActivity.class);
			    startActivityForResult(movieIntent, WATCH_MOVIE);
		    }
	    });

	    controls.addView(mColorChangeButton);
	    controls.addView(watchButton);

		rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
	    rootLayout.addView(controls, new LinearLayout.LayoutParams(
			    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(rootLayout);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		int color = data.getIntExtra(PaletteActivity.SELECTED_COLOR, Color.BLACK);
		mPaintArea.setColor(color);
		mColorChangeButton.setBackgroundColor(color);
		mListOfColors = data.getIntArrayExtra(PaletteActivity.LIST_OF_COLORS);
	}
}
