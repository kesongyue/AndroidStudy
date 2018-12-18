package com.example.ksy.webapi;

public class RepoMessage {
    int id;
    String name;
    int open_issues_count;
    String description;
    boolean has_issues;

    public int GetId(){return id;}
    public String GetName(){return name;}
    public int GetIssuesCount(){return open_issues_count;}
    public String GetDescription(){return description;}
    public boolean GetHasIssues(){return has_issues;}
}
