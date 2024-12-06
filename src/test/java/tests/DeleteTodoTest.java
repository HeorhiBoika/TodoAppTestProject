package tests;

import model.TodoModel;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Response;
import utils.RetrofitUtils;
import utils.TestDataUtils;

import java.io.IOException;
import java.util.Base64;

import static constants.ProjectConstants.BASIC_AUTH_CREDENTIALS;
import static constants.ResponseMessageConstants.*;

public class DeleteTodoTest extends BaseApiTest {

    public String authHeader;

    @BeforeClass
    public void setUp() {
        authHeader = "Basic " + Base64.getEncoder().encodeToString(BASIC_AUTH_CREDENTIALS.getBytes());
    }

    @Test
    /*
     * Delete TODO entity and check status code and response body
     */
    public void deleteTodoTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        Response<Void> response = todoApi.deleteTodo(authHeader, todoRequest.getId()).execute();

        RetrofitUtils.checkResponse(response, 204, NO_CONTENT);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Delete non existing TODO entity and check status code and response body
     */
    public void deleteTodoWithNonExistingIdTest() throws IOException {
        Response<Void> response = todoApi.deleteTodo(authHeader, TestDataUtils.getRandomId()).execute();

        RetrofitUtils.checkResponse(response, 404, NOT_FOUND);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Delete TODO entity with wrong credentials and check status code and response body
     */
    public void deleteTodoWithWrongCredentialsTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        String wrongAuthHeader = "Basic " + Base64.getEncoder().encodeToString("user:user".getBytes());
        Response<Void> response = todoApi.deleteTodo(wrongAuthHeader, TestDataUtils.getRandomId()).execute();

        RetrofitUtils.checkResponse(response, 401, UNAUTHORIZED);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Delete TODO entity without authorization and check status code and response body
     */
    public void deleteTodoWithoutAuthTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        Response<Void> response = todoApi.deleteTodo(null, TestDataUtils.getRandomId()).execute();

        RetrofitUtils.checkResponse(response, 401, UNAUTHORIZED);
        Assert.assertNull(response.body());
    }

    @Test
    /*
     * Delete TODO with id in another data type and check status code and response body
     */
    public void deleteTodoWithIdInStringTypeTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        Response<Void> response = todoApi.deleteTodo(authHeader, "null").execute();

        RetrofitUtils.checkResponse(response, 404, NOT_FOUND);
        Assert.assertNull(response.body());
    }
}
