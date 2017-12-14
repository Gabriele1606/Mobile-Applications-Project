package com.example.gabri.firstapp.Model;

/**
 * Created by Gabri on 13/12/17.
 */

public class Comment {

    private String username;
    private String review;
    private int rate;

    public Comment(String username, String review, int rate) {
        this.username = username;
        this.review = review;
        this.rate = rate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
