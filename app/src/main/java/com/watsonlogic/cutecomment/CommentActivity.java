package com.watsonlogic.cutecomment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.watsonlogic.searchproject2.R;

import java.sql.SQLException;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CommentActivity";
    private CommentController commentController;
    private EditText commentEditTxt;
    private Button postCommentBtn;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLayouts();
        getWidgets();
        commentController = new CommentController(this);
        commentController.setDummyData(listView);
    }

    @Override
    protected void onStop() {
        commentController.closeSQLDataSource();
        super.onStop();
    }


    @Override
    protected void onStart() {
        commentController.openSQLDataSource();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_comment_btn: {
                if (commentController.prepareToPostComment(commentEditTxt)) {
                    long position = commentController.addPendingCommentToUI();
                    commentController.hideKeyboard(commentEditTxt);
                    commentController.attemptPostCommentToAPI(position);
                } else {
                    return;
                }
            }
        }
    }

    private void getLayouts() {
        listView = (ListView) findViewById(R.id.list_view);
    }

    private void getWidgets() {
        postCommentBtn = (Button) findViewById(R.id.post_comment_btn);
        commentEditTxt = (EditText) findViewById(R.id.comment_edit_text);
    }


    public void postCommentDone(boolean success, long position) {
        if (!success) {
            commentController.attemptPostCommentToAPI(position);
        } else {
            commentController.updateColor(position, true, "#006699");
        }
    }
}
