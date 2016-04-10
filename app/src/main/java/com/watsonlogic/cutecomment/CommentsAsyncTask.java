package com.watsonlogic.cutecomment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;
import java.util.Random;

public class CommentsAsyncTask extends AsyncTask<Void, Void, Integer> {

    private static final String TAG = "CommentAsyncTask";
    private Context context;
    private HTTPMethod method;
    private URL url;
    private Comment comment;
    public CommentsAsyncTask(Context context, HTTPMethod method, URL url, Comment comment) {
        this.context = context;
        this.method = method;
        this.url = url;
        this.comment = comment;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int x = 0;
        try {
            switch (method) {
                case GET: {
                    Thread.sleep(2000);
                }
                case POST: {
                    //random fail or success
                    x = new Random().nextInt(3) - 1; //generate either -1, 0 or 1 and treat 1 as success
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Integer(x);
    }

    @Override
    protected void onPostExecute(Integer v) {
        super.onPostExecute(v);

        if (v.intValue() < 1) {
            Log.i(TAG, "Server down. Retrying...");
            ((CommentActivity) this.context).postCommentDone(false, comment.getPosition());
        } else {
            Log.i(TAG, "Comment successfully posted");
            ((CommentActivity) this.context).postCommentDone(true, comment.getPosition());
        }

    }

    public enum HTTPMethod {
        GET, POST;
    }

    public static class CommentsAsyncTaskBuilder {
        private Context context;
        private HTTPMethod method;
        private URL url;

        //POST PARAMS
        private Comment comment;

        public CommentsAsyncTaskBuilder() {
        }

        public CommentsAsyncTaskBuilder(Context context, HTTPMethod method, URL url, Comment comment) {
            this.context = context;
            this.method = method;
            this.url = url;
            this.comment = comment;
        }

        public CommentsAsyncTask build() {
            return new CommentsAsyncTask(context, method, url, comment);
        }

        public Context getContext() {
            return context;
        }

        public CommentsAsyncTaskBuilder setContext(Context context) {
            this.context = context;
            return this;
        }

        public CommentsAsyncTaskBuilder setMethod(HTTPMethod method) {
            this.method = method;
            return this;
        }

        public CommentsAsyncTaskBuilder setUrl(URL url) {
            this.url = url;
            return this;
        }

        public CommentsAsyncTaskBuilder setComment(Comment comment) {
            this.comment = comment;
            return this;
        }

    }
}
