package com.watsonlogic.cutecomment;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.watsonlogic.searchproject2.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class CommentController {
    private Comment mComment;
    private Activity activity;
    private BaseAdapter adapter;
    private List<Comment> commentsList;
    private String myCommentString;
    private URL apiURL;

    public CommentController(Activity activity) {
        this.activity = activity;
        try {
            apiURL = new URL("http://www.example.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean prepareToPostComment(EditText commentField) {
        if (!hasComment(commentField)) {
            editTextErr(commentField);
            return false;
        }
        return true;
    }

    public void setDummyData(ListView listView) {
        commentsList = new ArrayList<>();
        commentsList.add(new Comment(true, "cute", "#006699", new CommentDate().getCurrentDate()));
        commentsList.add(new Comment(true, "very very cute!", "#006699", new CommentDate().getCurrentDate()));
        commentsList.add(new Comment(true, "awwww!", "#006699", new CommentDate().getCurrentDate()));
        adapter = (new CustomAdapter(activity, R.layout.comment_row, commentsList));
        listView.setAdapter(adapter);
    }

    public void attemptPostCommentToAPI() {
        new CommentsAsyncTask.CommentsAsyncTaskBuilder()
                .setContext(activity)
                .setMethod(CommentsAsyncTask.HTTPMethod.POST)
                .setUrl(apiURL)
                .setComment(mComment)
                .build()
                .execute();
    }

    public void addPendingCommentToUI() {
        Date date = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        //System.out.println(dateFormat.format(date)); //04-09-2016 17:26:48
        mComment = new Comment(true, myCommentString, "#DCDCDC", new CommentDate().getCurrentDate());
        commentsList.add(mComment);
        adapter.notifyDataSetChanged();
    }

    public void updateColor(long position, boolean success, String color) {
        Comment c = commentsList.get((int)position);
        c.setColor(color);
        c.setSuccess(success);
        adapter.notifyDataSetChanged();
    }

    public void hideKeyboard(EditText et) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((null == activity.getCurrentFocus()) ? null : activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        et.setText("");
    }

    public void editTextErr(EditText et) {
        et.setError("Please enter a comment before posting.");
    }

    public boolean hasComment(EditText et) {
        myCommentString = et.getText().toString();
        return TextUtils.isEmpty(myCommentString) ? false : true;
    }
}
