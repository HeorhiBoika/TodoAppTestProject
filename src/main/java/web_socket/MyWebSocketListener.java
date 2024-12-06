package web_socket;

import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MyWebSocketListener extends WebSocketListener {

    private final List<String> receivedMessages = new ArrayList<>();

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("WebSocket opened successfully");
        webSocket.send("Hello Server!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        log.info("Received message: {}", text);
        receivedMessages.add(text);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        log.error("Error: {}", t.getMessage());
        t.printStackTrace();
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("WebSocket closed: {}", reason);
    }

    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}
