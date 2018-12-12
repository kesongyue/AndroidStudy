package com.example.ksy.webapi;

public class APIInfo {
    private int aid;
    private int state;
    private String cover;
    private String title;
    private String content;
    private int play;
    private String duration;
    private int video_review;
    private String create;
    private String rec;
    private int count;

    public int GetAid(){return aid;}
    public String GetCoverURL(){ return cover; }
    public int GetPlay(){return play;}
    public String GetDuration(){return duration;}
    public int GetVideoReview(){return video_review;}
    public String GetCreateTime(){return create;}
    public String GetContent(){return content;}
    public String GetTitle(){return title;}
}
