package practical.task.userservice.specification;

import org.springframework.data.jpa.domain.Specification;
import practical.task.userservice.model.User;

public class UserSpecification {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> hasSurname(String surname) {
        return (root, query, cb) ->
                surname == null ? null : cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }
}
