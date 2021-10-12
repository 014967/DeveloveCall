package com.example.developCall.Service;

import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.datastore.generated.model.Chat;
import com.amplifyframework.datastore.generated.model.DetailChat;
import com.amplifyframework.datastore.generated.model.Friend;
import com.amplifyframework.datastore.generated.model.User;

import io.reactivex.rxjava3.core.Single;

public interface service {
    public String findScheduleFormat(String str);
    public String[][] getTokenizedArray(String item);
    public String[][] getModifiedChatArray(String[][] array1, String[][] array2);
    public Single<GraphQLResponse<PaginatedResult<DetailChat>>> getChatList(String chatId);
    public Single<GraphQLResponse<PaginatedResult<User>>> getFriendList(String userId);
    public Single<GraphQLResponse<PaginatedResult<Chat>>> getChat(String userId);
    public GraphQLRequest<User> getUserRequest(String userId,String content);
    public Single<GraphQLResponse<PaginatedResult<Friend>>> getFriendName(String friendId);
    public GraphQLRequest<User> getSearchChat(String userId,String content);
    public Single<GraphQLResponse<User>> getdummy(String userId, String searchKey);


}