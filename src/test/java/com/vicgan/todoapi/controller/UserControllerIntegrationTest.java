package com.vicgan.todoapi.controller;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vicgan.todoapi.controller.v1.AuthController;
import com.vicgan.todoapi.controller.v1.UserController;
import com.vicgan.todoapi.dtos.SignInDto;
import com.vicgan.todoapi.dtos.SignUpDto;
import com.vicgan.todoapi.dtos.UpdateUserProfileDto;
import com.vicgan.todoapi.dtos.UserDto;
import com.vicgan.todoapi.repositories.UserRepository;
import com.vicgan.todoapi.response.AuthenticationResponse;
import com.vicgan.todoapi.services.impl.AuthenticationServiceImpl;
import com.vicgan.todoapi.services.impl.ImageServiceImpl;
import com.vicgan.todoapi.services.impl.UserServiceImpl;
import com.vicgan.todoapi.utils.JwtProvider;
import com.vicgan.todoapi.utils.UserControllerFieldDescriptors;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.lang.reflect.Type;
import java.util.UUID;

import static com.vicgan.todoapi.utils.UserControllerFieldDescriptors.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;


@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    private RequestSpecification requestSpecification;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private ImageServiceImpl imageService;

    @MockBean
    private JwtProvider jwtProvider;

    private String token;


    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    RestAssuredConfig restAssuredConfig = RestAssured.config()
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

    @Before
    public void setUp(){
        this.requestSpecification = new RequestSpecBuilder()
                .addFilter(
                        documentationConfiguration(this.restDocumentation)
                                .operationPreprocessors()
                                .withRequestDefaults(
                                        modifyUris()
                                                .host("localhost")
                                                .port(9000)
                                                .scheme("http"), prettyPrint())
                                .withResponseDefaults(prettyPrint())
                )
                .setConfig(restAssuredConfig)
                .build();
        signIn_andAuthorizeUser_andGetToken();
    }

    @Test
    public void givenThereAreUsers_whenGetAllUsers_thenReturnStatusOkAndListCustomers() {

        RestAssured.given(this.requestSpecification)
                .accept("application/json")
                .header("Authorization", "Bearer " + token)
                .filter(document("user-controller/get-all-users", queryParameters(
                                parameterWithName("first_name").description("filter by user's first name"),
                                parameterWithName("last_name").description("filter by user's last name'"),
                                parameterWithName("email").description("filter by user's email"),
                                parameterWithName("page").description("The page to retrieve"),
                                parameterWithName("size").description("Entries per page")), responseFields(listOfUsersApiResponseFd)))
                .when()
                .port(9000)
                .log().all()
                .when()
                .get("/api/v1/users/all?first_name=&last_name=&email=&page=0&size=1")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAUserExists_whenUpdateUser_thenReturnStatusOkAndUpdatedUser(){
        UpdateUserProfileDto updateUser = new UpdateUserProfileDto();
        updateUser.setId("c59f4f9c-d7b0-4cd2-a2b4-43c25c3206cb");
        updateUser.setFirstName("Nana");
        updateUser.setLastName("Doe");

        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body(updateUser, ObjectMapperType.GSON)
                .filter(document("user-controller/update-user", requestFields(updateUserFd), responseFields(userApiResponseFd)))
                .when()
                .port(9000)
                .log().all()
                .when()
                .put("/api/v1/users")
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAUserExists_whenGetAUserById_thenReturnStatusOkAndUser(){

        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .pathParam("id", "c59f4f9c-d7b0-4cd2-a2b4-43c25c3206cb")
                .filter(
                        document("user-controller/get-user-by-id",
                        pathParameters(parameterWithName("id").description("user's id")), responseFields(userApiResponseFd)))
                .log().all()
                .when()
                .port(9000)
                .when()
                .get("/api/v1/users/{id}" )
                .then()
                .log().all()
                .assertThat().statusCode(is(200));
    }

    @Test
    public void givenAUserExists_whenUploadImage_thenReturnStatusCreatedAndMessage_ImageUploadedSuccessfully(){
        RestAssured.given()
                .spec(this.requestSpecification)
                .contentType("multipart/form-data")
                .header("Authorization", "Bearer " + token)
                .multiPart("image", new File("/Users/vicgan/Downloads/Docker and Kubernetes_ The Complete Guide _ Udemy_files/udemy-logo.png"))
                .filter(document("user-controller/upload-image", requestParts(
                        partWithName("image").description("The image to upload")), responseFields(fieldWithPath("message").description("The response message after a successful upload."))))
                .log().all()
                .when()
                .port(9000)
                .post("/api/v1/users/upload/image")
                .then()
                .log().all()
                .assertThat().statusCode(is(201));
    }

    public void signIn_andAuthorizeUser_andGetToken(){
        token = RestAssured.given()
                .contentType("application/json")
                .body(new SignInDto("gyimah@gmail.com", "1234"))
                .when()
                .post("http://localhost:9000/api/v1/auth/authenticate")
                .jsonPath()
                .get("token");
    }
}
