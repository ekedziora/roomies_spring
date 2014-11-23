package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.Payment;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserAndPaymentGroup(User user, PaymentGroup paymentGroup);

    List<Payment> findByPaymentGroup(PaymentGroup paymentGroup);

    List<Payment> findByUser(User user);
}
