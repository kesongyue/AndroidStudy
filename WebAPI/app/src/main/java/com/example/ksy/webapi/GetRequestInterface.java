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

    @Headers({"Content-type: application/json;charset=UTF-8","Authorization: token 8ff45a4a70f5f3c8854ca30e7bdede01fd92d659"})
    @POST("/issues")
    Observable<IssueInfo> createIssue(@Body RequestBody route);
    //https://www.jianshu.com/p/32bfd5fd8b48
    //https://segmentfault.com/a/1190000015144126?utm_source=tag-newest
    // https://blog.csdn.net/carson_ho/article/details/73732076
    //https://blog.csdn.net/carson_ho/article/details/79125101
}
