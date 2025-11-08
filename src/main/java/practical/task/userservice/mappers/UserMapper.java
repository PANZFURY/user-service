package practical.task.userservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import practical.task.userservice.dtos.UserCreateDto;
import practical.task.userservice.dtos.UserResponse;
import practical.task.userservice.models.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", defaultValue = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User fromUserCreateDto(UserCreateDto userCreateDto);

    UserResponse toUserResponse(User user);
}
