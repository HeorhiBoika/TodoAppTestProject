package web_socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.util.List;

public class WebSocketClient {
    private final OkHttpClient client;
    private WebSocket webSocket;
    private final MyWebSocketListener listener;

    public WebSocketClient() {
        client = new OkHttpClient();
        listener = new MyWebSocketListener();
    }

    public void startWebSocket(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = client.newWebSocket(request, listener);
    }

    public List<String> getReceivedMessages() {
        return listener.getReceivedMessages();
    }

    public void closeWebSocket() {
        if (webSocket != null) {
            webSocket.close(1000, "Closing the connection");
        }
    }
}