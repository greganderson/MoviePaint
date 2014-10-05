package com.familybiz.greg.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import java.util.ArrayList;


public class MovieActivity extends Activity {

	static final String MOVIE_POSITION = "movie_position";
	static final String POINT_LIST = "point_list";

	ImageButton mPlayPause;
	boolean mPlay;
	ImageButton mStop;
	SeekBar mScrubber;
	MoviePaintAreaView mMoviePaintArea;
	ArrayList<PaintPoint> mPoints;
	int mCurrentPoint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.VERTICAL);

	    Bundle intent = getIntent().getExtras();
	    if (intent != null)
		    mPoints = intent.getParcelableArrayList(POINT_LIST);


	    // Paint view

	    mMoviePaintArea = new MoviePaintAreaView(this, mPoints);

	    // Player

	    LinearLayout player = new LinearLayout(this);
	    player.setOrientation(LinearLayout.HORIZONTAL);
	    LinearLayout.LayoutParams playerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	    playerParams.gravity = Gravity.CENTER_VERTICAL;
	    player.setLayoutParams(playerParams);

	    Button paintButton = new Button(this);
	    paintButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Intent resultIntent = new Intent();
			    //resultIntent.putExtra(MOVIE_POSITION, );
			    setResult(Activity.RESULT_OK, resultIntent);
			    finish();
		    }
	    });

	    mPlayPause = new ImageButton(this);
	    mPlay = false;
	    mPlayPause.setImageResource(R.drawable.ic_action_play);
	    mPlayPause.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    mPlay = !mPlay;
				mPlayPause.setImageResource(mPlay ? R.drawable.ic_action_play : R.drawable.ic_action_pause);
		    }
	    });

	    mStop = new ImageButton(this);
	    mStop.setImageResource(R.drawable.ic_action_stop);

	    mScrubber = new SeekBar(this);
	    mScrubber.setMax(mPoints.size());
	    mScrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		    @Override
		    public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
			    mMoviePaintArea.setPointPosition(value);
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {

		    }

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {

		    }
	    });

	    player.addView(paintButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mPlayPause, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mStop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mScrubber, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


	    rootLayout.addView(mMoviePaintArea, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
	    rootLayout.addView(player, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(rootLayout);
    }

	private void play() {
		while (mCurrentPoint < mPoints.size()) {
			mMoviePaintArea.setPointPosition(mCurrentPoint);
			mCurrentPoint++;
		}
	}

	private void pause() {
		//
	}

	private void stop() {
		mCurrentPoint = 0;
		mMoviePaintArea.setPointPosition(mCurrentPoint);
	}
}
