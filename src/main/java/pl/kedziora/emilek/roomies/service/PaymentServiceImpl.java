package pl.kedziora.emilek.roomies.service;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.json.objects.data.BudgetData;
import pl.kedziora.emilek.json.objects.data.PaymentData;
import pl.kedziora.emilek.json.objects.params.AddPaymentParams;
import pl.kedziora.emilek.roomies.database.objects.*;
import pl.kedziora.emilek.roomies.repository.PaymentGroupRepository;
import pl.kedziora.emilek.roomies.repository.PaymentRepository;
import pl.kedziora.emilek.roomies.repository.UserBalanceRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.util.List;
import java.util.SortedSet;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentGroupRepository paymentGroupRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private PaymentGroupService paymentGroupService;

    @Override
    public BudgetData getBudgetData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Long userId = user.getId();
        Group group = user.getGroup();

        if(group == null) {
            return new BudgetData(null, null, null);
        }

        PaymentGroup firstPaymentGroup = createPaymentGroupIfNotExistsOrReturnExistingOne(group);

        List<Payment> payments = paymentRepository.findByPaymentGroup(firstPaymentGroup);
        UserBalance balance = createUserBalanceIfNotExistsOrReturnExistingOne(userId, firstPaymentGroup);

        List<PaymentData> paymentDatas = generatePaymentDatasFromPayments(payments, user.getName());

        return new BudgetData(userId, balance.getBalance(), paymentDatas);
    }

    private UserBalance createUserBalanceIfNotExistsOrReturnExistingOne(Long userId, PaymentGroup firstPaymentGroup) {
        UserBalance balance = userBalanceRepository.findByUserIdAndPaymentGroup(userId, firstPaymentGroup);
        if(balance == null) {
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setPaymentGroup(firstPaymentGroup);
            userBalanceRepository.save(balance);
        }
        return balance;
    }

    private PaymentGroup createPaymentGroupIfNotExistsOrReturnExistingOne(Group group) {
        SortedSet<PaymentGroup> paymentGroups = group.getPaymentGroups();
        if(paymentGroups.isEmpty()) {
            PaymentGroup paymentGroup = new PaymentGroup();
            paymentGroup.setGroup(group);
            paymentGroupRepository.save(paymentGroup);
            return paymentGroup;
        }
        else {
            return paymentGroups.first();
        }
    }

    private List<PaymentData> generatePaymentDatasFromPayments(List<Payment> payments, final String userName) {
        return Lists.newArrayList(
                Collections2.transform(payments, new Function<Payment, PaymentData>() {
                    @Override
                    public PaymentData apply(Payment payment) {
                        return new PaymentData(payment.getId(), payment.getDescription(), userName, payment.getUserId(),
                                payment.getAmount());
                    }
                })
        );
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findOne(paymentId);
        PaymentGroup paymentGroup = payment.getPaymentGroup();
        paymentRepository.delete(payment);

        paymentGroupService.recalculateUserBalances(paymentGroup);
    }

    @Override
    public void addPayment(AddPaymentParams params) {
        User user = userRepository.findUserByMail(params.getParams().getMail());
        Group group = user.getGroup();
        PaymentGroup paymentGroup = createPaymentGroupIfNotExistsOrReturnExistingOne(group);

        Payment payment = new Payment();
        payment.setPaymentGroup(paymentGroup);
        payment.setUserId(user.getId());
        payment.setAmount(params.getAmount());
        payment.setDescription(params.getDescription());
        paymentRepository.save(payment);

        paymentGroupService.recalculateUserBalances(paymentGroup);
    }

}
