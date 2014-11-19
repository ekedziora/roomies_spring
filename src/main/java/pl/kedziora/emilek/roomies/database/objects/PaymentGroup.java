package pl.kedziora.emilek.roomies.database.objects;

import com.google.common.collect.Lists;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "payment_groups")
public class PaymentGroup extends BaseEntity implements Comparable<PaymentGroup>, Serializable {

    private static final long serialVersionUID = 3179299787402612632L;

    @Type(type = "date")
    private Date fromDate = new Date();

    @Type(type = "date")
    private Date toDate;

    @Type(type = "true_false")
    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paymentGroup")
    private List<Payment> payments = Lists.newArrayList();

    public PaymentGroup() {
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date from) {
        this.fromDate = from;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date to) {
        this.toDate = to;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    @Override
    public int compareTo(PaymentGroup o) {
        if(o == null)
            return 1;

        return fromDate.compareTo(o.getFromDate());
    }
}
