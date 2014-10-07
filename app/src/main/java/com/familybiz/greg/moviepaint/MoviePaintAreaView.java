package com.familybiz.greg.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;

public class MoviePaintAreaView extends View{

	// List of all the different sets of poly lines that have been drawn
	private ArrayList<PaintPoint> mPointList = new ArrayList<PaintPoint>();

	private long mPointPosition;

	public MoviePaintAreaView(Context context) {
		super(context);
		mPointPosition = 0;
	}

	public MoviePaintAreaView(Context context, ArrayList<PaintPoint> points) {
		super(context);
		mPointList = points;
		mPointPosition = 0;
	}

	public void setPointPosition(int newPosition) {
		mPointPosition = newPosition;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		polylinePaint.setStyle(Paint.Style.STROKE);
		polylinePaint.setStrokeWidth(8.0f);
		Path polylinePath = new Path();
		int color = 0;
		if (mPointList.size() > 0) {
			PaintPoint point = mPointList.get(0);
			color = point.color;
			polylinePaint.setColor(color);
			polylinePath.moveTo(point.x * getWidth(), point.y * getHeight());
		}

		for (int i = 1; i < mPointPosition; i++) {
			PaintPoint point = mPointList.get(i);
			if (point.color != color) {
				// Complete the line
				canvas.drawPath(polylinePath, polylinePaint);

				// Start new paint
				polylinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
				polylinePaint.setStyle(Paint.Style.STROKE);
				polylinePaint.setStrokeWidth(8.0f);
				color = point.color;
				polylinePaint.setColor(color);

				// Start new path
				polylinePath = new Path();
				polylinePath.moveTo(point.x * getWidth(), point.y * getHeight());
				continue;
			}
			polylinePath.lineTo(point.x * getWidth(), point.y * getHeight());
		}

		// Complete the line
		canvas.drawPath(polylinePath, polylinePaint);
	}
}
