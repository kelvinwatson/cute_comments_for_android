package com.watsonlogic.cutecomment;

public class Comment {
    private long position = -1;
    private long insertId = -1;
    private boolean success;
    private String comment;
    private String color;
    private String date;

    public Comment() {

    }

    public Comment(boolean success, String comment, String color, String date) {
        this.success = success;
        this.comment = comment;
        this.color = color;
        this.date = date;
    }

    public Comment(boolean success, String comment, String color, String date, long position) {
        this.success = success;
        this.comment = comment;
        this.color = color;
        this.date = date;
        this.position = position;
    }

    public Comment(boolean success, String comment, String color, String date, long position, long insertId) {
        this.success = success;
        this.comment = comment;
        this.color = color;
        this.date = date;
        this.position = position;
        this.insertId = insertId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public long getInsertId() {
        return this.insertId;
    }

    public void setInsertId(long insertId) {
        this.insertId = insertId;
    }

    public static class CommentBuilder {
        long position = -1;
        long insertId = -1;
        private boolean success;
        private String comment;
        private String color;
        private String date;

        public CommentBuilder(){

        }

        public CommentBuilder setPosition(long position) {
            this.position = position;
            return this;
        }

        public CommentBuilder setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public CommentBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public CommentBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public CommentBuilder setDate(String date) {
            this.date = date;
            return this;
        }

        public CommentBuilder setInsertId(long insertId) {
            this.insertId = insertId;
            return this;
        }

        public Comment build() {
            return new Comment(this.success, this.comment, this.color, this.date, this.insertId);
        }

    }

}
