package practical.task.userservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.dtos.requests.UserCreateDto;
import practical.task.userservice.dtos.UserResponse;
import practical.task.userservice.dtos.requests.UserUpdateDto;
import practical.task.userservice.models.User;

public interface UserService {

    Page<UserResponse> getAll(Specification<User> spec, Pageable pageable);
    UserResponse getOneById(Long id);
    UserResponse createUser(UserCreateDto userCreateDto);
    UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto);
    void deleteUserById(Long id);

}
