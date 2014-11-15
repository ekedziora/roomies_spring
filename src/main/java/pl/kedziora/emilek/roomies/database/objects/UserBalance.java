package pl.kedziora.emilek.roomies.database.objects;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_balances")
public class UserBalance extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 9043166369728230359L;

    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "payment_group_id", nullable = false)
    private PaymentGroup paymentGroup;

    public UserBalance() {
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
