package com.familybiz.greg.moviepaint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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


public class PaintActivity extends Activity {

	private PaintAreaView mPaintArea;
	static final int PICK_COLOR_REQUEST = 1;
	static final int WATCH_MOVIE = 2;
	static final String SAVED_POINTS_LIST = "saved_points_list";
	private ImageButton mColorChangeButton;
	private String filename = "data.txt";

	private int[] mListOfColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
	    if (mListOfColors == null || mListOfColors.length == 0) {
			mListOfColors = new int[]{
					Color.BLACK,
					Color.WHITE,
					Color.RED,
					Color.YELLOW,
					Color.BLUE,
					Color.GREEN
			};
	    }

		mPaintArea = new PaintAreaView(this);

		mPaintArea.setColor(Color.RED);

	    LinearLayout controls = new LinearLayout(this);
	    controls.setOrientation(LinearLayout.HORIZONTAL);

	    mColorChangeButton = new ImageButton(this);
	    mColorChangeButton.setImageResource(R.drawable.paint_brush);
	    LinearLayout.LayoutParams colorChangeButtonParams = new LinearLayout.LayoutParams(
			    ViewGroup.LayoutParams.WRAP_CONTENT,
			    ViewGroup.LayoutParams.MATCH_PARENT);
	    colorChangeButtonParams.setMargins(5, 5, 5, 5);
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

	    ImageButton watchButton = new ImageButton(this);
	    watchButton.setImageResource(R.drawable.ic_action_video);
	    LinearLayout.LayoutParams watchButtonParams = new LinearLayout.LayoutParams(
			    ViewGroup.LayoutParams.WRAP_CONTENT,
			    ViewGroup.LayoutParams.WRAP_CONTENT);
	    watchButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Intent movieIntent = new Intent();
			    movieIntent.putParcelableArrayListExtra(MovieActivity.POINT_LIST, mPaintArea.getPointList());
			    movieIntent.setClass(PaintActivity.this, MovieActivity.class);
			    startActivityForResult(movieIntent, WATCH_MOVIE);
		    }
	    });

	    controls.addView(mColorChangeButton, colorChangeButtonParams);
	    controls.addView(watchButton, watchButtonParams);

		rootLayout.addView(mPaintArea, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
	    rootLayout.addView(controls, new LinearLayout.LayoutParams(
			    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(rootLayout);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_COLOR_REQUEST) {
			int color = data.getIntExtra(PaletteActivity.SELECTED_COLOR, Color.BLACK);
			mPaintArea.setColor(color);
			mColorChangeButton.setBackgroundColor(color);
			mListOfColors = data.getIntArrayExtra(PaletteActivity.LIST_OF_COLORS);
		}
		saveData();
	}

	@Override
	protected void onStart() {
		super.onStart();

		try {
			File file = new File(getFilesDir(), filename);
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String content = "";
			String input = "";
			while ((input = bufferedReader.readLine()) != null)
				content += input;

			Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonObject data = parser.parse(content).getAsJsonObject();

			loadColorList();

			Type pointListType = new TypeToken<ArrayList<PaintPoint>>(){}.getType();
			ArrayList<PaintPoint> points = gson.fromJson(data.get(SAVED_POINTS_LIST), pointListType);
			mPaintArea.setPointList(points);

			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadColorList() {
		try {
			File file = new File(getFilesDir(), PaletteActivity.COLOR_LIST_FILENAME);
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			String content = "";
			String input = "";
			while ((input = bufferedReader.readLine()) != null)
				content += input;

			Gson gson = new Gson();

			JsonParser parser = new JsonParser();
			JsonObject data = parser.parse(content).getAsJsonObject();
			Type colorListType = new TypeToken<int[]>(){}.getType();
			mListOfColors = gson.fromJson(data.get(PaletteActivity.SAVED_COLOR_LIST), colorListType);

			bufferedReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		saveData();
	}

	private void saveData() {
		try {
			Gson gson = new Gson();

			String jsonPointList = gson.toJson(mPaintArea.getPointList());

			String result = "{\"" + SAVED_POINTS_LIST + "\":" + jsonPointList + "}";

			File file = new File(getFilesDir(), filename);
			FileWriter writer = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write(result);

			bufferedWriter.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
