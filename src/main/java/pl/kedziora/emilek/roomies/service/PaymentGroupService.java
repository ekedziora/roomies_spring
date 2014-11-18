package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;

public interface PaymentGroupService {
    void recalculateUserBalances(PaymentGroup paymentGroup);
}
