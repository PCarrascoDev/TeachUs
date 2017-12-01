package com.ppcarrasco.teachus.models;

/**
 * Created by pedro on 30-11-2017.
 */

public class UnsplashUser {

    private String name;
    private String bio;
    private String location;
    private String username;
    private int numeric_id;
    private int total_photos;
    private boolean followed_by_user;
    private String last_name;
    private int total_likes;
    private String portfolio_url;
    private Object badge;
    private String updated_at;
    private int following_count;
    private int downloads;
    private int followers_count;
    private int total_collections;
    private boolean completed_onboarding;
    private String id;
    private String first_name;

    public UnsplashUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumeric_id() {
        return numeric_id;
    }

    public void setNumeric_id(int numeric_id) {
        this.numeric_id = numeric_id;
    }

    public int getTotal_photos() {
        return total_photos;
    }

    public void setTotal_photos(int total_photos) {
        this.total_photos = total_photos;
    }

    public boolean isFollowed_by_user() {
        return followed_by_user;
    }

    public void setFollowed_by_user(boolean followed_by_user) {
        this.followed_by_user = followed_by_user;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(int total_likes) {
        this.total_likes = total_likes;
    }

    public String getPortfolio_url() {
        return portfolio_url;
    }

    public void setPortfolio_url(String portfolio_url) {
        this.portfolio_url = portfolio_url;
    }

    public Object getBadge() {
        return badge;
    }

    public void setBadge(Object badge) {
        this.badge = badge;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(int following_count) {
        this.following_count = following_count;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getTotal_collections() {
        return total_collections;
    }

    public void setTotal_collections(int total_collections) {
        this.total_collections = total_collections;
    }

    public boolean isCompleted_onboarding() {
        return completed_onboarding;
    }

    public void setCompleted_onboarding(boolean completed_onboarding) {
        this.completed_onboarding = completed_onboarding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
}