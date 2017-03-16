package com.example.vraun.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vraun.inventory.Data.ProductContract;

import static android.content.ContentValues.TAG;

/**
 * Created by vraun on 16-03-2017.
 */

public class ItemsCursorAdapter extends CursorAdapter {

    public ItemsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvPrice = (TextView) view.findViewById(R.id.price);
        TextView tvCount = (TextView) view.findViewById(R.id.count);

        final int productId = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
        String name = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME));
        Double price = cursor.getDouble(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE));
        final int count = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_COUNT));

        tvName.setText(name);
        tvPrice.setText("Cost: $" + String.format("%.2f", price));
        tvCount.setText("Availability: " + String.valueOf(count));

        //Bind buy event to button
        Button btnBuy = (Button) view.findViewById(R.id.buy_button);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri itemUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);
                buyProduct(context, itemUri, count);
            }
        });
    }

    // Decrease product count by 1
    private void buyProduct(Context context, Uri itemUri, int currentCount) {
        int newCount = (currentCount >= 1) ? currentCount - 1 : 0;
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_COUNT, newCount );
        int numRowsUpdated = context.getContentResolver().update(itemUri, values, null, null);

        if (numRowsUpdated > 0) {
            Log.i(TAG, "Buy product successful");
        } else {
            Log.i(TAG, "Could not update buy product");
        }
    }

}
