package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;

import java.math.BigDecimal;

public interface PaymentGroupService {
    void recalculateUserBalances(PaymentGroup paymentGroup);

    BigDecimal calculateUserBalance(PaymentGroup paymentGroup, User user);
}
