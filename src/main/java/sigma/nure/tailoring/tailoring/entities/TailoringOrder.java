package sigma.nure.tailoring.tailoring.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tailoring_order")
@NoArgsConstructor
@AllArgsConstructor
public class TailoringOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "address_for_send",nullable = false)
    private String addressForSend;
    @Column(name = "order_description",nullable = false)
    private String orderDescription;
    @Column(name ="order_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @Column(name ="order_payment_status",nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderPaymentStatus paymentStatus;
    @Column(name ="date_of_creation",nullable = false)
    private LocalDateTime dateOfCreation;
    @Column(name ="is_from_template",nullable = false)
    private boolean fromTemplate;
    @Column(name ="end_date",nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private int cost;
    @Column(name = "count_of_order",nullable = false)
    private int countOfOrder;
    @OneToOne
    @JoinColumn(name = "material_id",nullable = false)
    private Material material;
    @OneToOne
    @JoinColumn(name = "color_id",nullable = false)
    private Color color;
    @Column(name = "user_id",nullable = false)
    private Long userId;
    @OneToMany
    @JoinColumn(name = "user_order_id")
    private List<Image> images;
    @OneToMany
    @JoinColumn(name = "user_order_id")
    private List<PartSizeOrder> partSizes;
}
