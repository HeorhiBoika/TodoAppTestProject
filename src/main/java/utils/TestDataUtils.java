package utils;

import model.TodoModel;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class TestDataUtils {

    public static long getRandomId(){
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    public static TodoModel createRequest(){
        long id = getRandomId();
        String text = RandomStringUtils.random(10, true, false);
        boolean completed = false;
        return createRequest(id, text, completed);
    }

    public static TodoModel createRequest(long id, String text, boolean completed){
        return TodoModel.builder()
                .id(id)
                .text(text)
                .completed(completed)
                .build();
    }
}
