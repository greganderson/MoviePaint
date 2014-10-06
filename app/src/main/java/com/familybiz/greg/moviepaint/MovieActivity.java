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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MovieActivity extends Activity {

	static final String MOVIE_POSITION = "movie_position";
	static final String SAVED_MOVIE_POSITION_FILENAME = "movie_position.txt";
	static final String POINT_LIST = "point_list";
	private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> mMovieThread;

	ImageButton mPlayPause;
	boolean mPlay;
	boolean mPause;
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
	    paintButton.setText("Paint!");
	    paintButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    stop();
			    Intent resultIntent = new Intent();
			    setResult(Activity.RESULT_OK, resultIntent);
			    finish();
		    }
	    });

	    mPlayPause = new ImageButton(this);
	    mPlay = false;
	    mPlay = false;
	    mPlayPause.setImageResource(R.drawable.ic_action_play);
	    mPlayPause.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    if (mPlay) {
				    mPlay = false;
				    mPause = true;
			    }
			    else {
				    mPlay = true;
				    mPause = false;
			    }
			    setPlayPauseButton();
			    play();
		    }
	    });

	    mStop = new ImageButton(this);
	    mStop.setImageResource(R.drawable.ic_action_stop);
	    mStop.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    stop();
		    }
	    });

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

	    player.addView(paintButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
	    player.addView(mPlayPause, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mStop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	    player.addView(mScrubber, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


	    rootLayout.addView(mMoviePaintArea, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
	    rootLayout.addView(player, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

	    setContentView(rootLayout);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveSpotInMovie();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadSpotInMovie();
	}

	private void setPlayPauseButton() {
		mPlayPause.setImageResource(mPlay ? R.drawable.ic_action_pause : R.drawable.ic_action_play);
	}

	private void play() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				while (mCurrentPoint < mPoints.size()) {
					if (!mPlay) {
						if (!mPause)
							mScrubber.setProgress(0);
						break;
					}
					mScrubber.incrementProgressBy(1);
					mCurrentPoint++;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				mMovieThread.cancel(true);
			}
		};
		 mMovieThread = worker.schedule(task, 0, TimeUnit.SECONDS);
	}

	private void stop() {
		mPlay = false;
		mPause = false;
		mCurrentPoint = 0;
		mScrubber.setProgress(0);
		setPlayPauseButton();
		saveSpotInMovie();
	}

	private void saveSpotInMovie() {
		try {
			Gson gson = new Gson();

			String jsonSpotInMovie = gson.toJson(mScrubber.getProgress());

			String result = "{\"" + MOVIE_POSITION + "\":" + jsonSpotInMovie + "}";

			File file = new File(getFilesDir(), SAVED_MOVIE_POSITION_FILENAME);
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(result);

			bufferedWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadSpotInMovie() {
		try {
			File file = new File(getFilesDir(), SAVED_MOVIE_POSITION_FILENAME);
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String content = "";
			String input = "";
			while ((input = bufferedReader.readLine()) != null)
				content += input;

			Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonObject data = parser.parse(content).getAsJsonObject();
			Type moviePositionType = new TypeToken<Integer>(){}.getType();
			int progress = gson.fromJson(data.get(MOVIE_POSITION), moviePositionType);
			mScrubber.setProgress(progress);

			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class Player extends TimerTask {

		@Override
		public void run() {
			if (mCurrentPoint < mPoints.size())
				mMoviePaintArea.setPointPosition(mCurrentPoint++);
		}
	}
}
