package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.FinancialPunishment;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.util.List;

public interface FinancialPunishmentRepository extends JpaRepository<FinancialPunishment, Long> {

    List<FinancialPunishment> findByUserAndPaymentGroup(User user, PaymentGroup paymentGroup);

    List<FinancialPunishment> findByPaymentGroup(PaymentGroup paymentGroup);
}
