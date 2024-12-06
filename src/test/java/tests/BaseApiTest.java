package tests;

import api.TodoApi;
import api.RetrofitClient;
import constants.ProjectConstants;

public class BaseApiTest {

    protected TodoApi todoApi;

    public BaseApiTest(){
        this.todoApi = RetrofitClient.getInstance().getRetrofitObject(TodoApi.class, ProjectConstants.APP_BASE_URL);
    }
}
