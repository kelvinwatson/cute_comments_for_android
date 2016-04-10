package com.watsonlogic.cutecomment;

//data access object (DAO) to manage data
//handles database connection and data access/modification

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentsDataSource {
    private static final String TAG = "CommentsDataSource";
    private SQLiteDatabase db;
    private CommentsSQLiteHelper dbHelper;
    private String[] allColumns = {
            CommentsSQLiteHelper.COLUMN_NAMED_INSERT_ID,
            CommentsSQLiteHelper.COLUMN_NAMED_POSITION,
            CommentsSQLiteHelper.COLUMN_NAMED_COLOR,
            CommentsSQLiteHelper.COLUMN_NAMED_COMMENT,
            CommentsSQLiteHelper.COLUMN_NAMED_DATE,
            CommentsSQLiteHelper.COLUMN_NAMED_SUCCESS
    };

    public CommentsDataSource(Context context){
        dbHelper = new CommentsSQLiteHelper(context);
    }

    public void open() throws SQLException{
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Comment createComment(Comment comment){
        ContentValues values = new ContentValues();
        //values.put(CommentsSQLiteHelper.COLUMN_NAMED_INSERT_ID, comment.getInsertId());
        values.put(CommentsSQLiteHelper.COLUMN_NAMED_COLOR, comment.getColor());
        values.put(CommentsSQLiteHelper.COLUMN_NAMED_COMMENT, comment.getComment());
        values.put(CommentsSQLiteHelper.COLUMN_NAMED_DATE, comment.getDate());
        values.put(CommentsSQLiteHelper.COLUMN_NAMED_POSITION, comment.getPosition());
        values.put(CommentsSQLiteHelper.COLUMN_NAMED_SUCCESS, comment.isSuccess());


        long insertId = db.insert(CommentsSQLiteHelper.TABLE_NAME, null, values);

        List params = new ArrayList();
        params.add(CommentsSQLiteHelper.TABLE_NAME);
        params.add(allColumns);
        params.add(CommentsSQLiteHelper.COLUMN_NAMED_INSERT_ID +"="+insertId);
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);

        Cursor cursor = performQuery(params);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        newComment.setInsertId(insertId);
        return newComment;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment.CommentBuilder()
                .setInsertId(cursor.getLong(0))
                .setPosition(cursor.getLong(1))
                .setColor(cursor.getString(2))
                .setComment(cursor.getString(3))
                .setDate(cursor.getString(4))
                .setSuccess(cursor.getInt(5)==1?true:false)
                .build();
        return comment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getInsertId();
        Log.d(TAG, "Comment deleted with id: " + id);
        db.delete(CommentsSQLiteHelper.TABLE_NAME,
                CommentsSQLiteHelper.COLUMN_NAMED_INSERT_ID
                + " = " + id, null);
    }

    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();

        List params = new ArrayList();
        params.add(CommentsSQLiteHelper.TABLE_NAME);
        params.add(allColumns);
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);
        params.add(null);

        Cursor cursor = performQuery(params);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }


    private Cursor performQuery(List prms){
        return db.query(
                (String)prms.get(0), (String[])prms.get(1), (String)prms.get(2),
                (String[])prms.get(3), (String)prms.get(4),
                (String)prms.get(5), (String)prms.get(6));
    }

}
