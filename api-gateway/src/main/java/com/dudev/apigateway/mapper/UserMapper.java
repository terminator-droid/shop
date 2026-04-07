package com.dudev.apigateway.mapper;


import com.dudev.apigateway.api.dto.UserCreateRequest;
import com.dudev.apigateway.api.dto.UserResponse;
import com.dudev.apigateway.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toEntity(UserCreateRequest userCreateRequest);

    UserResponse toDto(User user);
}
