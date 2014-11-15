package pl.kedziora.emilek.roomies.service;

import pl.kedziora.emilek.json.objects.data.BudgetData;

public interface PaymentService {
    BudgetData getBudgetData(String mail);

    void deletePayment(Long paymentId);
}
