package com.vicgan.todoapi.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicgan.todoapi.controller.v1.AuthController;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.SignUpDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.services.impl.AuthenticationServiceImpl;
import com.vicgan.todoapi.services.impl.UserServiceImpl;
import com.vicgan.todoapi.utils.JwtProvider;
import com.vicgan.todoapi.utils.UserControllerFieldDescriptors;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vicgan.todoapi.utils.AuthControllerFieldDescriptors.registerUserFd;
import static com.vicgan.todoapi.utils.AuthControllerFieldDescriptors.signInFd;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;


@RunWith(SpringRunner.class)
@WebMvcTest(AuthController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthControllerIntegrationTest {

    private RequestSpecification requestSpecification;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserServiceImpl userService;


    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

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
    }

    @Test
    public void givenAuth_registerUser_thenReturnStatusOkAndCreatedUser(){

        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setFirstName("Emmanuel");
        signUpDto.setLastName("Gyimah");
        signUpDto.setEmail("gyimah@gmail.com");
        signUpDto.setPassword("1234");
        signUpDto.setCreatedAt(String.valueOf(Timestamp.valueOf(LocalDateTime.now())));
        signUpDto.setUpdatedAt(String.valueOf(Timestamp.valueOf(LocalDateTime.now())));

         RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .body(signUpDto, ObjectMapperType.GSON)
                .filter(document("authentication-controller/signup", requestFields(registerUserFd), responseFields(UserControllerFieldDescriptors.userApiResponseFd)))
                .when()
                .port(9000)
                .log().all()
                .when()
                .post("/api/v1/auth/signup")
                .then()
                .log().all()
                .assertThat().statusCode(is(201));

    }

    @Test
    public void givenAuth_signInUser_thenReturnStatusOkAndToken(){

        FieldDescriptor[] tokenResponseFd = new FieldDescriptor[]{
            fieldWithPath("token").description("generated token for authentication and authorization").type(JsonFieldType.STRING)
        };

        SignInDto signInDto = new SignInDto();
        signInDto.setEmail("gyimah@gmail.com");
        signInDto.setPassword("1234");

        RestAssured.given()
                .spec(requestSpecification)
                .contentType("application/json")
                .body(signInDto, ObjectMapperType.GSON)
                .filter(document("authentication-controller/signin", requestFields(signInFd), responseFields(tokenResponseFd)))
                .when()
                .port(9000)
                .log().all()
                .when()
                .post("/api/v1/auth/authenticate")
                .then()
                .log().all()
                .statusCode(is(200));
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
}
