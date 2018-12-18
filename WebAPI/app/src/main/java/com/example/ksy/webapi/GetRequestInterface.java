package com.example.ksy.webapi;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetRequestInterface{
    @GET("/users/{username}/repos")
    Observable<List<RepoMessage>> getRepo(@Path("username") String username);
}
