package tests;

import model.TodoModel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import retrofit2.Response;
import utils.RetrofitUtils;
import utils.TestDataUtils;

import java.io.IOException;
import java.util.List;

import static constants.ResponseMessageConstants.*;

public class GetTodoTest extends BaseApiTest {

    private TodoModel todoRequest;

    @BeforeMethod
    public void setUp() throws IOException {
        todoRequest = TestDataUtils.createRequest();
        todoApi.createTodo(todoRequest).execute();
    }

    @Test
    /*
     * Get list of all TODO's and check status code and response body
     */
    public void getAllTodosTest() throws IOException {
        Response<List<TodoModel>> response = todoApi.getTodos(null, null).execute();

        RetrofitUtils.checkResponse(response, 200, OK);

        List<TodoModel> todos = response.body();
        Assert.assertNotEquals(todos.size(), 0);

        TodoModel todoModel = todos.stream()
                .filter(todo -> todo.getId().equals(todoRequest.getId()))
                .findFirst()
                .get();
        Assert.assertEquals(todoModel.getText(), todoRequest.getText());
        Assert.assertEquals(todoModel.getCompleted(), todoRequest.getCompleted());
    }

    @Test
    /*
     * Get list of TODO's wth offset and check status code and response body
     */
    public void getTodosWithOffsetTest() throws IOException {
        todoApi.createTodo(TestDataUtils.createRequest()).execute();

        Response<List<TodoModel>> allTodoResponse = todoApi.getTodos(null, null).execute();
        List<TodoModel> allTodos = allTodoResponse.body();

        Response<List<TodoModel>> notAllTodoResponse = todoApi.getTodos(1, null).execute();
        RetrofitUtils.checkResponse(notAllTodoResponse, 200, OK);
        List<TodoModel> notAllTodos = notAllTodoResponse.body();

        Assert.assertEquals(allTodos.size() - notAllTodos.size(), 1);
        Assert.assertEquals(allTodos.get(1).getId(), notAllTodos.get(0).getId());
        Assert.assertEquals(allTodos.get(1).getText(), notAllTodos.get(0).getText());
        Assert.assertEquals(allTodos.get(1).getCompleted(), notAllTodos.get(0).getCompleted());
    }

    @Test
    /*
     * Get list of TODO's wth limit and check status code and response body
     */
    public void getTodosWithLimitTest() throws IOException {
        todoApi.createTodo(TestDataUtils.createRequest()).execute();

        Response<List<TodoModel>> allTodoResponse = todoApi.getTodos(null, null).execute();
        List<TodoModel> allTodos = allTodoResponse.body();

        Response<List<TodoModel>> notAllTodoResponse = todoApi.getTodos(null, 1).execute();
        RetrofitUtils.checkResponse(notAllTodoResponse, 200, OK);
        List<TodoModel> notAllTodos = notAllTodoResponse.body();

        Assert.assertEquals(notAllTodos.size(), 1);
        Assert.assertEquals(allTodos.get(0).getId(), notAllTodos.get(0).getId());
        Assert.assertEquals(allTodos.get(0).getText(), notAllTodos.get(0).getText());
        Assert.assertEquals(allTodos.get(0).getCompleted(), notAllTodos.get(0).getCompleted());
    }

    @Test
    /*
     * Get list of TODO's wth offset and limit and check status code and response body
     */
    public void getTodosWithOffsetAndLimitTest() throws IOException {
        todoApi.createTodo(TestDataUtils.createRequest()).execute();

        Response<List<TodoModel>> allTodoResponse = todoApi.getTodos(null, null).execute();
        List<TodoModel> allTodos = allTodoResponse.body();

        int lastElementIndex = allTodos.size()-1;
        Response<List<TodoModel>> notAllTodoResponse = todoApi.getTodos(lastElementIndex, 1).execute();
        RetrofitUtils.checkResponse(notAllTodoResponse, 200, OK);
        List<TodoModel> notAllTodos = notAllTodoResponse.body();

        Assert.assertEquals(notAllTodos.size(), 1);
        Assert.assertEquals(allTodos.get(lastElementIndex).getId(), notAllTodos.get(0).getId());
        Assert.assertEquals(allTodos.get(lastElementIndex).getText(), notAllTodos.get(0).getText());
        Assert.assertEquals(allTodos.get(lastElementIndex).getCompleted(), notAllTodos.get(0).getCompleted());
    }

    @Test
    /*
     * Get list of TODO's wth wrong offset and limit and check status code and response body
     */
    public void getTodosWithInvalidOffsetAndLimitTest() throws IOException {
        Response<List<TodoModel>> response = todoApi.getTodos(-1, -1).execute();

        RetrofitUtils.checkResponse(response, 400, BAD_REQUEST);
        Assert.assertNull(response.body());
    }
}
