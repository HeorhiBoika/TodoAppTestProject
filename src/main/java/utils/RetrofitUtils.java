package utils;

import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;

@Slf4j
public class RetrofitUtils {

    public static <T> void checkResponse(Response<T> response, int statusCode, String message) {
        log.info("Response have status code = {}, message = {}", statusCode, message);
        if (!(response.code() == statusCode && response.message().equals(message))) {
            throw new RuntimeException("Endpoint return status code = " + statusCode + ", message = " + response);
        }
    }
}
