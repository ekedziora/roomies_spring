package pl.kedziora.emilek.roomies.database.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4735689388398627280L;

    private BigDecimal amount;

    private String description;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "payment_group_id", nullable = false)
    private PaymentGroup paymentGroup;

    public Payment() {
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PaymentGroup getPaymentGroup() {
        return paymentGroup;
    }

    public void setPaymentGroup(PaymentGroup paymentGroup) {
        this.paymentGroup = paymentGroup;
    }
}
