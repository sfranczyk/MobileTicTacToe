package com.example.mobiletictactoe;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.util.*;

public class CellAdapter extends BaseAdapter {

    private final Context context;
    private Integer[] images = {R.drawable.circle, R.drawable.x};
    private GameState gameState;
    private int cellPadding;
    private int symbolDimension;

    HashMap<CellValue, Integer> map = new HashMap<>();

    public CellAdapter(Context c, GameState gameState, int cellDimension){
        context = c;
        this.gameState = gameState;
        cellPadding = cellDimension / 10;
        symbolDimension = cellDimension - cellPadding;

        map.put(CellValue.O, R.drawable.circle);
        map.put(CellValue.X, R.drawable.x);
        map.put(CellValue.FREE, R.drawable.white);
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int i) {
        return images[1];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);

        imageView.setImageResource(map.get(gameState.getCellValue(i)));

        imageView.setBackgroundColor(gameState.isEnd() && gameState.getWinLineIndex().contains(i)
                ? Color.rgb(119,221,119) : Color.rgb(255, 255, 255));

        imageView.setPadding(cellPadding, cellPadding, cellPadding, cellPadding);

        imageView.setScaleType((ImageView.ScaleType.CENTER_INSIDE));
        imageView.setLayoutParams(new GridView.LayoutParams(symbolDimension, symbolDimension));

        return imageView;
    }
}
