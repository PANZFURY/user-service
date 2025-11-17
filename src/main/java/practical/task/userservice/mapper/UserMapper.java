package practical.task.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import practical.task.userservice.dto.response.UserResponse;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", expression = "java(true)")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User fromUserCreateDto(UserCreateDto userCreateDto);

    UserResponse toUserResponse(User user);
}
