package com.example.developCall.Service;

import com.amplifyframework.api.graphql.GraphQLResponse;
import com.amplifyframework.api.graphql.PaginatedResult;
import com.amplifyframework.datastore.generated.model.DetailChat;

import io.reactivex.rxjava3.core.Single;

public interface service {
    public String findScheduleFormat(String str);
    public String[][] getTokenizedArray(String item);
    public String[][] getModifiedChatArray(String[][] array1, String[][] array2);
    public Single<GraphQLResponse<PaginatedResult<DetailChat>>> getChatList(String chatId);
}