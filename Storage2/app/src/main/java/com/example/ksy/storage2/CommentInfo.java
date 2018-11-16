package com.example.ksy.storage2;

import java.io.Serializable;

public class CommentInfo implements Serializable{
    private int id;
    private String username;
    private String date;
    private String comment;
    private int likeNum;

    //创建新的Item用的。
   /* public CommentInfo(String username,String date,String comment,int likeNum,boolean isLikeSelf){
        this.username = username;
        this.date = date;
        this.comment = comment;
        this.likeNum = likeNum;
        this.isLikeSelf = isLikeSelf;
    }*/
    public CommentInfo(int id,String username,String date,String comment,int likeNum){
        this.id = id;
        this.username = username;
        this.date = date;
        this.comment = comment;
        this.likeNum = likeNum;
    }

    public int GetId(){return id;}
    public String GetUsername(){
        return username;
    }
    public String GetDate(){
        return date;
    }
    public String GetComment(){
        return comment;
    }
    public int GetLikeNum(){
        return likeNum;
    }
    public void SetLikeNum(int num){this.likeNum = num;}
}
