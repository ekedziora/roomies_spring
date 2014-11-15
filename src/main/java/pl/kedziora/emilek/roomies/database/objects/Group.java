package pl.kedziora.emilek.roomies.database.objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

@Entity
@Table(name = "groups")
public class Group extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -688823842543038525L;

    @Column(nullable = false)
    private String name;

    private String address;

    @OneToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<User> members = Lists.newArrayList();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("fromDate desc")
    @SortNatural
    private SortedSet<PaymentGroup> paymentGroups = Sets.newTreeSet();

    public Group() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public SortedSet<PaymentGroup> getPaymentGroups() {
        return paymentGroups;
    }

    public void setPaymentGroups(SortedSet<PaymentGroup> paymentGroups) {
        this.paymentGroups = paymentGroups;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("address", address)
                .append("admin", admin)
                .append("members", members)
                .toString();
    }

}
