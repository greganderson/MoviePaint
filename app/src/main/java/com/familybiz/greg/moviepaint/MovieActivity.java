package com.familybiz.greg.moviepaint;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;


public class MovieActivity extends Activity {

	ImageButton mPlayPause;
	boolean mPlay;
	ImageButton mStop;
	SeekBar mScrubber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    LinearLayout rootLayout = new LinearLayout(this);
	    rootLayout.setOrientation(LinearLayout.HORIZONTAL);

	    LinearLayout player = new LinearLayout(this);
	    player.setOrientation(LinearLayout.HORIZONTAL);
	    LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, )
	    player.setLayoutParams(params);

	    mPlayPause = new ImageButton(this);
	    mPlay = true;
	    mPlayPause.setImageResource(R.drawable.ic_action_play);
	    mPlayPause.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    mPlay = !mPlay;
			    if (mPlay)
				    mPlayPause.setImageResource(R.drawable.ic_action_play);
			    else
				    mPlayPause.setImageResource(R.drawable.ic_action_pause);
		    }
	    });

	    mStop = new ImageButton(this);
	    mStop.setImageResource(R.drawable.ic_action_stop);

	    mScrubber = new SeekBar(this);
	    mScrubber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
		    @Override
		    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
			    Log.i("SEEKER", "Value: " + i);
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {

		    }

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {

		    }
	    });

	    player.addView(mPlayPause, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mStop, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mScrubber, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

	    rootLayout.addView(player, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setContentView(rootLayout);
    }


}
