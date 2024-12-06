package tests;

import model.TodoModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.testng.Assert;
import org.testng.annotations.Test;
import retrofit2.Response;
import utils.RetrofitUtils;
import utils.TestDataUtils;

import java.io.IOException;

import static constants.ResponseMessageConstants.BAD_REQUEST;
import static constants.ResponseMessageConstants.CREATED;

public class CreateTodoTest extends BaseApiTest {

    @Test
    /*
     * Create TODO entity and check status code and response body
     */
    public void createTodoTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        Response<Void> response = todoApi.createTodo(todoRequest).execute();

        RetrofitUtils.checkResponse(response, 201, CREATED);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Create several times the same TODO entities and check status code and response body
     */
    public void duplicateTodoTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        Response<Void> response = todoApi.createTodo(todoRequest).execute();

        RetrofitUtils.checkResponse(response, 400, BAD_REQUEST);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Create TODO entity with negative Id and check status code and response body
     */
    public void createTodoWithNegativeIdTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest(-1, "negative id", true);

        Response<Void> response = todoApi.createTodo(todoRequest).execute();

        RetrofitUtils.checkResponse(response, 400, BAD_REQUEST);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Create TODO entity with empty body and check status code and response body
     */
    public void createTodoWithNullTextTest() throws IOException {
        Response<Void> response = todoApi.createTodo(TodoModel.builder().build()).execute();

        RetrofitUtils.checkResponse(response, 400, BAD_REQUEST);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Create TODO entity with wrong data types and check status code and response body
     */
    public void createTodoWithWrongFieldsTypesTest() throws IOException {
        String invalidJson = "{ \"id\": \"12\", \"text\": 12345, \"completed\": \"false\" }";
        RequestBody body = RequestBody.create(invalidJson, MediaType.get("application/json"));

        Response<Void> response = todoApi.createTodo(body).execute();

        RetrofitUtils.checkResponse(response, 400, BAD_REQUEST);
        Assert.assertNull(response.body());
    }
}
