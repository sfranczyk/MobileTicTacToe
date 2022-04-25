package com.example.mobiletictactoe;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class GameStateParcelable extends GameState implements Parcelable {

    public GameStateParcelable() {
        super();
    }

    protected GameStateParcelable(Parcel in) {
        cellValues = (CellValue[]) in.readArray(CellValue.class.getClassLoader());
        currentTurn = CellValue.values()[in.readInt()];
        numberToWin = in.readInt();
        winner = CellValue.values()[in.readInt()];
        winLine = new ArrayList<>(Arrays.asList(
                (int[][]) in.readArray(winLine.toArray().getClass().getClassLoader())));
    }

    public static final Creator<GameStateParcelable> CREATOR = new Creator<GameStateParcelable>() {
        @Override
        public GameStateParcelable createFromParcel(Parcel in) {
            return new GameStateParcelable(in);
        }

        @Override
        public GameStateParcelable[] newArray(int size) {
            return new GameStateParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(cellValues);
        parcel.writeInt(currentTurn.ordinal());
        parcel.writeInt(numberToWin);
        parcel.writeInt(winner.ordinal());
        parcel.writeArray(winLine.toArray());
    }
}
