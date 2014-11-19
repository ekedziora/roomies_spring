package pl.kedziora.emilek.roomies.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.Payment;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentGroupServiceImpl implements PaymentGroupService {

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * Na przyszłość do przeliczania salda wszystkich userów w grupie
     *
     * @param paymentGroup
     */
    @Override
    public void recalculateUserBalances(PaymentGroup paymentGroup) {
        List<User> members = paymentGroup.getGroup().getMembers();
        Map<User, BigDecimal> userSums = createUserSums(paymentGroup, members);

        BigDecimal average = calculateAverage(members, userSums);
    }

    private BigDecimal calculateAverage(List<User> members, Map<User, BigDecimal> userSums) {
        BigDecimal allSums = BigDecimal.ZERO;
        for(BigDecimal sum : userSums.values()) {
            allSums = allSums.add(sum);
        }

        BigDecimal divider = new BigDecimal(String.valueOf(members.size()));
        return allSums.divide(divider, 2, BigDecimal.ROUND_HALF_UP);
    }

    private Map<User, BigDecimal> createUserSums(PaymentGroup paymentGroup, List<User> members) {
        Map<User, BigDecimal> userSums = Maps.newHashMapWithExpectedSize(members.size());

        for(User user : members) {
            List<Payment> payments = paymentRepository.findByUserAndPaymentGroup(user, paymentGroup);

            BigDecimal sum = BigDecimal.ZERO;
            for(Payment payment : payments) {
                sum = sum.add(payment.getAmount());
            }
            userSums.put(user, sum);
        }

        return userSums;
    }

    @Override
    public BigDecimal calculateUserBalance(PaymentGroup paymentGroup, User user) {
        BigDecimal average = calculateAverage(paymentGroup);

        List<Payment> userPayments = paymentRepository.findByUserAndPaymentGroup(user, paymentGroup);
        BigDecimal userSum = sumPayments(userPayments);

        return userSum.subtract(average);
    }

    private BigDecimal calculateAverage(PaymentGroup paymentGroup) {
        List<Payment> groupPayments = paymentRepository.findByPaymentGroup(paymentGroup);
        BigDecimal groupSum = sumPayments(groupPayments);

        int membersCount = paymentGroup.getGroup().getMembers().size();
        BigDecimal divider = new BigDecimal(String.valueOf(membersCount));

        return groupSum.divide(divider, 2, BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal sumPayments(List<Payment> payments) {
        BigDecimal sum = BigDecimal.ZERO;
        for(Payment payment : payments) {
            sum = sum.add(payment.getAmount());
        }
        return sum;
    }

}
