package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.BudgetData;
import pl.kedziora.emilek.json.objects.params.AddPaymentParams;

public interface PaymentService {
    BudgetData getBudgetData(String mail);

    void deletePayment(Long paymentId, String mail);

    void addPayment(AddPaymentParams params);
}
