package tests;

import constants.ProjectConstants;
import model.TodoModel;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import retrofit2.Response;
import utils.RetrofitUtils;
import utils.TestDataUtils;
import web_socket.WebSocketClient;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static constants.ResponseMessageConstants.CREATED;

public class WebSocketTodoTest extends BaseApiTest {

    private WebSocketClient webSocketClient;

    @BeforeClass
    public void setUp() {
        webSocketClient = new WebSocketClient();
        webSocketClient.startWebSocket(ProjectConstants.WEB_SOCKET_APP_BASE_URL);
    }

    @AfterClass
    public void turnDown() {
        webSocketClient.closeWebSocket();
    }

    @Test
    /*
     * Create connection and receive new TODO
     */
    public void createTodoAndFindInReceivedMessagesTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        Response<Void> response = todoApi.createTodo(todoRequest).execute();
        RetrofitUtils.checkResponse(response, 201, CREATED);

        List<String> webSocketReceivedMessages = webSocketClient.getReceivedMessages();

        Assert.assertTrue(webSocketReceivedMessages.stream()
                .anyMatch(message -> message.contains(todoRequest.getId().toString())));
    }

    @Test
    /*
     * Create a connection and don't get a message about duplication of existing TODO
     */
    public void createDuplicationAndNotFindInReceivedMessagesTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();
        todoApi.createTodo(todoRequest).execute();

        List<String> webSocketReceivedMessages = webSocketClient.getReceivedMessages();

        List<String> todos = webSocketReceivedMessages.stream()
                .filter(message -> message.contains(todoRequest.getId().toString()))
                .collect(Collectors.toList());
        Assert.assertEquals(todos.size(), 1);
    }

    @Test
    /*
     * Create a connection and don't get a message about updating an existing TODO
     */
    public void updateTodoAndNotFindInReceivedMessagesTest() throws IOException {
        TodoModel todoRequest = TestDataUtils.createRequest();

        todoApi.createTodo(todoRequest).execute();

        todoRequest.setText("update text");
        todoRequest.setCompleted(true);
        todoApi.updateTodo(todoRequest.getId(), todoRequest).execute();

        List<String> webSocketReceivedMessages = webSocketClient.getReceivedMessages();

        Assert.assertTrue(webSocketReceivedMessages.stream()
                .anyMatch(message -> message.contains(todoRequest.getId().toString())));
        Assert.assertFalse(webSocketReceivedMessages.stream()
                .anyMatch(message -> message.contains(todoRequest.getText())));
    }
}
