package practical.task.userservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import practical.task.userservice.dtos.UserResponse;
import practical.task.userservice.dtos.requests.UserCreateDto;
import practical.task.userservice.dtos.requests.UserUpdateDto;
import practical.task.userservice.models.User;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Page<UserResponse> getAll(Specification<User> spec, Pageable pageable) {
        return null;
    }

    @Override
    public UserResponse getOneById(Long id) {
        return null;
    }

    @Override
    public UserResponse createUser(UserCreateDto userCreateDto) {
        return null;
    }

    @Override
    public UserResponse updateUserById(Long id, UserUpdateDto userUpdateDto) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
