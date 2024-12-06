package api;

import model.TodoModel;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface TodoApi {

    @GET("/todos")
    Call<List<TodoModel>> getTodos(@Query("offset") Integer offset, @Query("limit") Integer limit);

    @POST("/todos")
    Call<Void> createTodo(@Body TodoModel body);

    @POST("/todos")
    Call<Void> createTodo(@Body RequestBody body);

    @PUT("/todos/{id}")
    Call<Void> updateTodo(@Path(value = "id") Long id, @Body TodoModel body);

    @PUT("/todos/{id}")
    Call<Void> updateTodo(@Path(value = "id") Long id, @Body RequestBody body);

    @DELETE("/todos/{id}")
    Call<Void> deleteTodo(@Header("Authorization") String authHeader, @Path(value = "id") Object id);
}
