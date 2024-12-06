package tests;

import model.TodoModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import retrofit2.Response;
import utils.RetrofitUtils;
import utils.TestDataUtils;

import java.io.IOException;

import static constants.ResponseMessageConstants.*;

public class UpdateTodoTest extends BaseApiTest {

    private TodoModel todoRequest;

    @BeforeMethod
    public void setUp() throws IOException {
        todoRequest = TestDataUtils.createRequest();
        todoApi.createTodo(todoRequest).execute();
    }

    @Test
    /*
     * Update existing TODO and check status code and response body
     */
    public void updateTodoTest() throws IOException {
        todoRequest.setText("update text");
        todoRequest.setCompleted(true);
        Response<Void> response = todoApi.updateTodo(todoRequest.getId(), todoRequest).execute();

        RetrofitUtils.checkResponse(response, 200, OK);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Update non existing TODO and check status code and response body
     */
    public void updateTodoWithInvalidIdTest() throws IOException {
        Response<Void> response = todoApi.updateTodo(-1L, todoRequest).execute();

        RetrofitUtils.checkResponse(response, 404, NOT_FOUND);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Update TODO and set existing Id of another entity and check status code and response body
     */
    public void updateTodoWithExistingIdTest() throws IOException {
        TodoModel todoRequestV1 = TestDataUtils.createRequest();
        TodoModel todoRequestV2 = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequestV1).execute();
        todoApi.createTodo(todoRequestV2).execute();

        Long todoId = todoRequestV2.getId();
        todoRequestV2.setId(todoRequestV1.getId());
        Response<Void> response = todoApi.updateTodo(todoId, todoRequestV2).execute();

        RetrofitUtils.checkResponse(response, 200, OK);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Update existing TODO with empty body and check status code and response body
     */
    public void updateTodoWithEmptyBodyTest() throws IOException {
        Response<Void> response = todoApi.updateTodo(todoRequest.getId(), TodoModel.builder().build()).execute();

        RetrofitUtils.checkResponse(response, 401, UNAUTHORIZED);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Update existing TODO with wrong data types and check status code and response body
     */
    public void createTodoWithWrongFieldsTypesTest() throws IOException {
        String invalidJson = "{ \"id\": \"12\", \"text\": 12345, \"completed\": \"false\" }";
        RequestBody body = RequestBody.create(invalidJson, MediaType.get("application/json"));

        Response<Void> response = todoApi.updateTodo(todoRequest.getId(), body).execute();

        RetrofitUtils.checkResponse(response, 401, UNAUTHORIZED);
        Assert.assertNull(response.body());
    }
}
