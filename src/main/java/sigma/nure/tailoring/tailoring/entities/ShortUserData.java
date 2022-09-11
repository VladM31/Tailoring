package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUserData {
    private Long id;
    private String phoneNumber;
    private String city;
    private String country;
    private String firstname;
    private boolean isMale;

}
