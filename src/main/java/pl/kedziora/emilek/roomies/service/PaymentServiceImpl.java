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
import pl.kedziora.emilek.roomies.annotation.Secured;
import pl.kedziora.emilek.roomies.database.objects.Group;
import pl.kedziora.emilek.roomies.database.objects.Payment;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.exception.BadRequestException;
import pl.kedziora.emilek.roomies.repository.PaymentGroupRepository;
import pl.kedziora.emilek.roomies.repository.PaymentRepository;
import pl.kedziora.emilek.roomies.repository.UserRepository;

import java.math.BigDecimal;
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
    private PaymentGroupService paymentGroupService;

    @Override
    public BudgetData getBudgetData(String mail) {
        User user = userRepository.findUserByMail(mail);
        Group group = user.getGroup();

        if(group == null) {
            return new BudgetData(null, null, null);
        }

        PaymentGroup firstPaymentGroup = createPaymentGroupIfNotExistsOrReturnExistingOne(group);

        List<Payment> payments = paymentRepository.findByPaymentGroup(firstPaymentGroup);
        BigDecimal balance = paymentGroupService.calculateUserBalance(firstPaymentGroup, user);

        List<PaymentData> paymentDatas = generatePaymentDatasFromPayments(payments);

        return new BudgetData(user.getId(), balance, paymentDatas);
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

    private List<PaymentData> generatePaymentDatasFromPayments(List<Payment> payments) {
        return Lists.newArrayList(
                Collections2.transform(payments, new Function<Payment, PaymentData>() {
                    @Override
                    public PaymentData apply(Payment payment) {
                        User user = payment.getUser();
                        return new PaymentData(payment.getId(), payment.getDescription(), user.getName(), user.getId(),
                                payment.getAmount());
                    }
                })
        );
    }

    @Override
    @Secured("User deleting any payment in database")
    public void deletePayment(Long paymentId, String mail) {
        Payment payment = paymentRepository.findOne(paymentId);
        User user = userRepository.findUserByMail(mail);
        if(!payment.getUser().equals(user)) {
            throw new BadRequestException();
        }

        paymentRepository.delete(payment);
    }

    @Override
    public void addPayment(AddPaymentParams params) {
        User user = userRepository.findUserByMail(params.getParams().getMail());
        Group group = user.getGroup();
        PaymentGroup paymentGroup = createPaymentGroupIfNotExistsOrReturnExistingOne(group);

        Payment payment = new Payment();
        payment.setPaymentGroup(paymentGroup);
        payment.setUser(user);
        payment.setAmount(params.getAmount());
        payment.setDescription(params.getDescription());
        paymentRepository.save(payment);
    }

}
