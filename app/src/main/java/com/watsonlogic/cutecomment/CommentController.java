package com.watsonlogic.cutecomment;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.watsonlogic.searchproject2.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CommentController {
    private static final String TAG = "CommentController";
    private Comment mComment;
    private Activity activity;
    private BaseAdapter adapter;
    private List<Comment> commentsList;
    private String myCommentString;
    private URL apiURL;
    private CommentsDataSource SQLDataSource;
    private ListView listView;

    public CommentController(Activity activity, ListView listView) {
        this.activity = activity;
        this.listView = listView;
        commentsList = new ArrayList<>();
        try {
            apiURL = new URL("http://www.example.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        SQLDataSource = new CommentsDataSource(activity.getApplicationContext());
        openSQLDataSource();
    }

    public void openSQLDataSource() {
        try {
            SQLDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        testPrintSQLite();
        testDeleteSQLite();
    }

    //test
    public void testPrintSQLite() {
        Log.d(TAG, "SQLITE CONTAINS:");
        List<Comment> lis = SQLDataSource.getAllComments();
        for (Comment c : lis) {
            Log.d(TAG, String.valueOf(c.getInsertId()));
            Log.d(TAG, c.getComment());
        }
    }

    public void testDeleteSQLite(){
        Log.d(TAG, "SQLITE DELETING:");
        List<Comment> lis = SQLDataSource.getAllComments();
        Iterator<Comment> itr = lis.iterator();
        while(itr.hasNext()){
            Comment c = itr.next();
            SQLDataSource.deleteComment(c);
        }
    }

    public void closeSQLDataSource() {
        SQLDataSource.close();
    }

    public boolean prepareToPostComment(EditText commentField) {
        if (!hasComment(commentField)) {
            editTextErr(commentField);
            return false;
        }
        return true;
    }

    public int commentsListSize() {
        try {
            int sz = commentsList.size();
            return sz;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean setCustomAdapter(ListView listView) {
        adapter = (new CustomAdapter(activity, R.layout.comment_row, commentsList));
        if (commentsList.size() > 0) {
            listView.setAdapter(adapter);
            return true;
        } else {
            View emptyView = activity.findViewById(R.id.empty_list_item);
            listView.setEmptyView(emptyView);
            return false;
        }
    }

    public void setDummyData(ListView listView) {
        Comment c1 = new Comment(true, "cute", "#006699", new CommentDate().getCurrentDate());
        //Comment c2 = new Comment(true, "very very cute!", "#006699", new CommentDate().getCurrentDate());
        //Comment c3 = new Comment(true, "awwww!", "#006699", new CommentDate().getCurrentDate());

        commentsList.add(c1);
        //commentsList.add(c2);
        //commentsList.add(c3);

        synchronizeSQLDataSourceInitial(commentsList);

        adapter = (new CustomAdapter(activity, R.layout.comment_row, commentsList));
        listView.setAdapter(adapter);
    }

    private void synchronizeSQLDataSourceInitial(List<Comment> commentsList) {
        for (Comment c : commentsList) {
            Comment d = SQLDataSource.createComment(c);
        }
    }

    private Comment synchronizeSQLDataSource(Comment c) {
        return SQLDataSource.createComment(c);
    }

    public void attemptPostCommentToAPI(long position) {
        new CommentsAsyncTask.CommentsAsyncTaskBuilder()
                .setContext(activity)
                .setMethod(CommentsAsyncTask.HTTPMethod.POST)
                .setUrl(apiURL)
                .setComment(commentsList.get((int) position))
                .build()
                .execute();
    }

    public long addPendingCommentToUI() {
        Date date = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        //System.out.println(dateFormat.format(date)); //04-09-2016 17:26:48
        mComment = new Comment(true, myCommentString, "#DCDCDC", new CommentDate().getCurrentDate());
        commentsList.add(mComment);
        int position = commentsList.size() - 1;
        mComment.setPosition(position);
        if (position == 0) {
            listView.setAdapter(adapter); //set adapter for the first time (if first element)
        }
        mComment = synchronizeSQLDataSource(mComment);
        commentsList.set(position, mComment);
        adapter.notifyDataSetChanged();
        return position;
    }

    public void updateColor(long position, boolean success, String color) {
        Comment c = commentsList.get((int) position);
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
