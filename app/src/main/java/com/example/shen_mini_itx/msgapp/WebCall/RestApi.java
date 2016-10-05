package com.example.shen_mini_itx.msgapp.WebCall;

import com.example.shen_mini_itx.msgapp.Models.CommentModel;
import com.example.shen_mini_itx.msgapp.Models.PostModel;
import com.example.shen_mini_itx.msgapp.Models.UserModel;
import com.example.shen_mini_itx.msgapp.Requests.CommentRequest;
import com.example.shen_mini_itx.msgapp.Requests.PostRequest;
import com.example.shen_mini_itx.msgapp.Requests.UserSyncRequest;
import com.example.shen_mini_itx.msgapp.Responses.CommonResponse;
import com.example.shen_mini_itx.msgapp.Responses.UserSyncResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by shen-mini-itx on 20-Jul-16.
 */
public interface RestApi {

    @POST("/sync")
    Call<UserSyncResponse> syncUser(@Body UserSyncRequest user);

    @PUT("/login/{api}/{id}")
    Call<CommonResponse> login(@Path("api") String api,
                               @Path("id") String id);

    @GET("/posts/{api}/{id}/{lat}/{lng}/{radius}")
    Call<List<PostModel>> getPostsBasedLocation(@Path("api") String api,
                                                @Path("id") String id,
                                                @Path("lat") Double lat,
                                                @Path("lng") Double lng,
                                                @Path("radius") Double radius);

    @POST("/post/{api}/{id}")
    Call<CommonResponse> newPostBasedLocation(@Path("api") String api,
                                              @Path("id") String id,
                                              @Body PostRequest post);

    @POST("/likepost/{api}/{id}/{postid}")
    Call<CommonResponse> setLikePostStatus(@Path("api") String api,
                                           @Path("id") String id,
                                           @Path("postid") String postid);

    @GET("/post/{api}/{id}/{post_id}")
    Call<PostModel> getPostDetailById(@Path("api") String api,
                                      @Path("id") String id,
                                      @Path("post_id") String post_id);

    @POST("/comment/{api}/{id}/")
    Call<CommonResponse> newCommentOnPost(@Path("api") String api,
                                          @Path("id") String userId,
                                          @Body CommentRequest comment);

    @GET("/comment/{api}/{id}/{post_id}")
    Call<List<CommentModel>> getCommentsByPostId(@Path("api") String api,
                                                 @Path("id") String userId,
                                                 @Path("post_id") String post_id);

    @GET("/friend/{api}/{id}")
    Call<List<UserModel>> getFriendListByUserId(@Path("api") String api,
                                                @Path("id") String userId);

    @GET("/friend/search/{api}/{id}/{search_term}")
    Call<List<UserModel>> searchFriendList(@Path("api") String api,
                                           @Path("id") String userId,
                                           @Path("search_term") String term);

    @PUT("/friend/add/{api}/{id}/{friend_id}")
    Call<CommonResponse> addFriend(@Path("api") String api,
                                   @Path("id") String userId,
                                   @Path("friend_id") String friend_id);


}
