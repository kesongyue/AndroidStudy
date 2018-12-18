package com.example.ksy.webapi;

public class IssueInfo {
    String title;
    String created_at;
    String state;
    String body;

    public String GetBody() {
        return body;
    }

    public String GetCreated_at() {
        return created_at;
    }

    public String GetState() {
        return state;
    }

    public String GetTitle() {
        return title;
    }
}
