package com.vicgan.todoapi.configs;

import com.vicgan.todoapi.dtos.*;
import com.vicgan.todoapi.entities.Image;
import com.vicgan.todoapi.entities.Task;
import com.vicgan.todoapi.entities.User;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Configuration
public class DtoToEntityMapper {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();

        PropertyMap<User, UserDto> userUserDtoPropertyMap = new PropertyMap<User, UserDto>() {
            @Override
            protected void configure() {
                map().setFullName(source.getFullName());
            }
        };

        PropertyMap<User, SignInDto> userSignInDtoPropertyMap = new PropertyMap<User, SignInDto>() {

            @Override
            protected void configure() {
                map().setEmail(source.getEmail());
                map().setPassword(source.getPassword());
            }
        };

        PropertyMap<Image, ImageDto> imageImageDtoPropertyMap = new PropertyMap<Image, ImageDto>() {
            @Override
            protected void configure() {
                map().setId(null);
            }
        };

        PropertyMap<Image, DeleteImageDto> imageDeleteImageDtoPropertyMap = new PropertyMap<Image, DeleteImageDto>(){

            @Override
            protected void configure() {
                map().setName(source.getName());
            }
        };

        PropertyMap<Task, TaskDto> taskDtoPropertyMap = new PropertyMap<Task, TaskDto>() {
            @Override
            protected void configure() {
            }
        };

        Converter<Timestamp, String> timestampStringConverter = new AbstractConverter<Timestamp, String>() {
            @Override
            protected String convert(Timestamp source) {
                Timestamp timestamp = Timestamp.valueOf(source.toString());
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                return dateTimeFormatter.format(timestamp.toLocalDateTime());
            }
        };

        Converter<String, Timestamp> stringTimestampConverter = new AbstractConverter<String, Timestamp>() {
            @Override
            protected Timestamp convert(String source) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                try {
                    Date date = simpleDateFormat.parse(source);
                    return Timestamp.valueOf(String.valueOf(date.getTime()));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        mapper.addMappings(userUserDtoPropertyMap);
        mapper.addMappings(userSignInDtoPropertyMap);
        mapper.addMappings(imageImageDtoPropertyMap);
        mapper.addMappings(imageDeleteImageDtoPropertyMap);
        mapper.addMappings(taskDtoPropertyMap);
        mapper.addConverter(stringTimestampConverter);
        mapper.addConverter(timestampStringConverter);

        return mapper;
    }
}
