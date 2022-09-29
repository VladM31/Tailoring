package sigma.tailoring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.tailoring.entities.Role;
import sigma.tailoring.entities.User;
import sigma.tailoring.entities.UserState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserList {
    private List<UserDto> userDtos;

    public UserList(List<User> users) {
        this.userDtos = toUserDtoList(users);
    }

    private List<UserDto> toUserDtoList(List<User> users) {
        return users.stream()
                .collect(Collectors.mapping(
                        UserList::toUserDto,
                        Collectors.toList()));
    }

    private static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.phoneNumber = user.getPhoneNumber();
        userDto.email = user.getEmail();
        userDto.city = user.getCity();
        userDto.country = user.getCountry();
        userDto.firstname = user.getFirstname();
        userDto.lastname = user.getLastname();
        userDto.dateRegistration = user.getDateRegistration();
        userDto.active = user.isActive();
        userDto.male = user.isMale();
        userDto.userState = user.getUserState();
        userDto.role = user.getRole();

        return userDto;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserDto {
        private Long id;
        private String phoneNumber;
        private String email;
        private String city;
        private String country;
        private String firstname;
        private String lastname;
        private LocalDateTime dateRegistration;
        private boolean active;
        private boolean male;
        private UserState userState;
        private Role role;
    }
}
