package practical.task.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import practical.task.userservice.dto.response.UserResponse;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.request.userDto.UserUpdateDto;

public interface UserService {

    Page<UserResponse> getAll(String name, String surname, Pageable pageable);
    UserResponse getOneById(Long id);
    UserResponse createUser(UserCreateDto userCreateDto);
    UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto);
    UserResponse deleteUserById(Long id);

}
