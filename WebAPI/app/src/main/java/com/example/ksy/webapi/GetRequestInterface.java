package com.example.ksy.webapi;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetRequestInterface{
    @GET("/users/{username}/repos")
    Observable<List<RepoMessage>> getRepo(@Path("username") String username);

    @GET("/repos/{username}/{repo}/issues")
    Observable<List<IssueInfo>> getIssue(@Path("username")String username,@Path("repo")String repoName);

    @Headers({"Content-type:application/json;charset=UTF-8","Authorization: token 860269799c3899a9da009c049474dbbd45355b1f"})
    @POST("/repos/{username}/{repo}/issues")
    Observable<IssueInfo> createIssue(@Body RequestBody route,@Path("username")String username,@Path("repo")String repoName);

}
