package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.UserBalance;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    UserBalance findByUserIdAndPaymentGroup(Long userId, PaymentGroup paymentGroup);

}
