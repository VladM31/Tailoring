package sigma.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {
    private Long id;
    private String phoneNumber;
    private String city;
    private String country;
    private String firstname;
    private boolean isMale;

    public CustomerOrder(Long id) {
        this.id = id;
    }
}
