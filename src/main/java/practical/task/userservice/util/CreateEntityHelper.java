package practical.task.userservice.util;

public class CreateEntityHelper {
    public static <K> K resolveIfNotNull(K newValue, K oldValue) {
        return newValue != null ? newValue : oldValue;
    }
}
