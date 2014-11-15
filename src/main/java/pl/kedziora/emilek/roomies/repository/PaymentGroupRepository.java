package pl.kedziora.emilek.roomies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;

public interface PaymentGroupRepository extends JpaRepository<PaymentGroup, Long> {
}
