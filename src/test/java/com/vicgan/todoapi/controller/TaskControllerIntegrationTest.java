package com.vicgan.todoapi.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicgan.todoapi.controller.v1.TaskController;
import com.vicgan.todoapi.dtos.CreateTaskDto;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.UpdateTaskDto;
import com.vicgan.todoapi.entities.Task;
import com.vicgan.todoapi.services.impl.TaskServiceImpl;
import com.vicgan.todoapi.services.impl.UserServiceImpl;
import com.vicgan.todoapi.utils.JwtProvider;
import com.vicgan.todoapi.utils.TaskControllerFieldDescriptors;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;

import static com.vicgan.todoapi.utils.TaskControllerFieldDescriptors.listOfTaskApiResponseFd;
import static com.vicgan.todoapi.utils.TaskControllerFieldDescriptors.taskApiResponseFd;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerIntegrationTest {

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private TaskServiceImpl taskService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private UserServiceImpl userService;

    private RequestSpecification requestSpecification;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    private String token;

    @Before
    public void setUp(){
        this.requestSpecification = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(this.restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(modifyUris()
                                        .port(9000)
                                        .host("localhost")
                                        .scheme("http"), prettyPrint())
                                .withResponseDefaults(prettyPrint())
                )
                .setConfig(restAssuredConfig())
                .build();
        signIn_andAuthorizeUser_andGetToken();
    }

    @Test
    public void givenAUserExists_whenCreateATask_thenReturnStatusCreatedAndTask(){
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setDescription("Go for a walk");
        createTaskDto.setCompleted(false);
        RestAssured.given(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(createTaskDto, ObjectMapperType.GSON)
                .filter(document("task-controller/create-task", requestFields(
                                fieldWithPath("description").description("The description of the task"),
                                fieldWithPath("completed").description("The completed status of the task"),
                                fieldWithPath("created_at").description("Creation date and time").optional().ignored(),
                                fieldWithPath("updated_at").description("Date and time when the user was updated").optional().ignored()
                        ),
                        responseFields(taskApiResponseFd))
                )
                .when()
                .port(9000)
                .log().all()
                .when()
                .post("/api/v1/tasks")
                .then()
                .log().all()
                .assertThat().statusCode(is(201));
    }

    @Test
    public void givenAUserExists_whenGetTaskById_thenReturnStatusOkAndTask(){

        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", "34b7f465-ac3c-404e-ab1e-3a3e88415426")
                .filter(document("task-controller/get-task-by-id", pathParameters(parameterWithName("id").description("task id")), responseFields(taskApiResponseFd)))
                .when()
                .port(9000)
                .log().all()
                .when()
                .get("/api/v1/tasks/{id}")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAUserExists_whenGetAllTasks_thenReturnStatusOkAndListOfTasks(){
        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .filter(document("task-controller/get-all-tasks", responseFields(listOfTaskApiResponseFd)))
                .when()
                .port(9000)
                .then()
                .log().all()
                .when()
                .get("/api/v1/tasks/all?completed=&page=0&size=5")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAUserExists_whenUpdateTask_thenReturnStatusOkAndUpdatedTask(){
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setId("34b7f465-ac3c-404e-ab1e-3a3e88415426");
        updateTaskDto.setDescription("Clean the kitchen");
        updateTaskDto.setCompleted(true);

        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(updateTaskDto, ObjectMapperType.GSON)
                .filter(document("task-controller/update-task",
                        requestFields(
                        fieldWithPath("id").description("Task id"),
                        fieldWithPath("description").description("Task description"),
                        fieldWithPath("completed").description("Task completed or not")),
                        responseFields(taskApiResponseFd)
                ))
                .when()
                .port(9000)
                .then()
                .log().all()
                .when()
                .put("/api/v1/tasks")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    public RestAssuredConfig restAssuredConfig(){
        return RestAssuredConfig.config()
                .objectMapperConfig(ObjectMapperConfig.objectMapperConfig()
                        .gsonObjectMapperFactory(new GsonObjectMapperFactory() {
                            @Override
                            public Gson create(Type type, String s) {
                                return new GsonBuilder()
                                        .serializeNulls()
                                        .disableHtmlEscaping()
                                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                        .create();
                            }
                        }));
    }

    public void signIn_andAuthorizeUser_andGetToken(){
        token = RestAssured.given()
                .contentType("application/json")
                .body(new SignInDto("victor@gmail.com", "password"))
                .when()
                .post("http://localhost:9000/api/v1/auth/authenticate")
                .jsonPath()
                .get("token");
    }
}
