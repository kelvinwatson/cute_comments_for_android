package com.watsonlogic.cutecomment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.watsonlogic.searchproject2.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Comment> {

    public CustomAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customRow = inflater.inflate(R.layout.comment_row, parent, false);
        final Comment commentObj = getItem(position);
        commentObj.setPosition(position);
        TextView customRowComment = (TextView) customRow.findViewById(R.id.custom_row_comment);
        customRowComment.setText(commentObj.getComment());
        customRowComment.setTextColor(Color.parseColor(commentObj.getColor()));
        TextView customRowDate = (TextView) customRow.findViewById(R.id.custom_row_date);
        customRowDate.setText(commentObj.getDate());
        customRowDate.setTextColor(Color.parseColor(commentObj.getColor()));
        return customRow;
    }
}