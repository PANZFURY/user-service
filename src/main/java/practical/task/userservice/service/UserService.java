package practical.task.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.dto.response.UserResponse;
import practical.task.userservice.dto.request.userDto.UserCreateDto;
import practical.task.userservice.dto.request.userDto.UserUpdateDto;
import practical.task.userservice.model.User;

public interface UserService {

    Page<UserResponse> getAll(Specification<User> spec, Pageable pageable);
    UserResponse getOneById(Long id);
    UserResponse createUser(UserCreateDto userCreateDto);
    UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto);
    void deleteUserById(Long id);

}
