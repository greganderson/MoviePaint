package com.familybiz.greg.moviepaint;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

public class PaintPoint implements Parcelable {

	public final float x;
	public final float y;
	public final int color;

	public PaintPoint(PointF point, int color) {
		this.color = color;
		x = point.x;
		y = point.y;
	}

	public PaintPoint(Parcel in) {
		x = in.readFloat();
		y = in.readFloat();
		color = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeFloat(x);
		parcel.writeFloat(y);
		parcel.writeInt(color);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public PaintPoint createFromParcel(Parcel in) {
			return new PaintPoint(in);
		}

		public PaintPoint[] newArray(int size) {
			return new PaintPoint[size];
		}
	};
}
