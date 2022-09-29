package sigma.tailoring.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.entities.User;
import sigma.tailoring.exceptions.UserNotFound;
import sigma.tailoring.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActiveForm {
    @NotNull(message = "You didn't choose active", groups = OnUpdate.class)
    public Boolean active;

    public User toUser(UserService userService, Long userId) {
        User user = userService.findBy(UserSearchCriteria
                                .builder()
                                .ids(List.of(userId))
                                .build(),
                        new Page())
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFound("User with id %s not found".formatted(userId)));

        user.setActive(active);
        return user;
    }
}
