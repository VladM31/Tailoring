package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
@Table(name="role")
@AllArgsConstructor
public class Role {

    @Transient public static final Role CUSTOMER = new Role(1,"CUSTOMER");
    @Transient public static final Role ADMINISTRATION = new Role(10,"ADMINISTRATION");
    @Id
    private Integer id;
    @Column(unique = true)
    private String name;

    public Role(){
        this(CUSTOMER.id,CUSTOMER.name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!Objects.equals(id, role.id)) return false;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name);
    }
}
