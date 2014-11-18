package pl.kedziora.emilek.roomies.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kedziora.emilek.roomies.database.objects.Payment;
import pl.kedziora.emilek.roomies.database.objects.PaymentGroup;
import pl.kedziora.emilek.roomies.database.objects.User;
import pl.kedziora.emilek.roomies.database.objects.UserBalance;
import pl.kedziora.emilek.roomies.repository.PaymentRepository;
import pl.kedziora.emilek.roomies.repository.UserBalanceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentGroupServiceImpl implements PaymentGroupService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void recalculateUserBalances(PaymentGroup paymentGroup) {
        List<User> members = paymentGroup.getGroup().getMembers();
        Map<User, BigDecimal> userSums = createUserSums(paymentGroup, members);

        BigDecimal average = calculateAverage(members, userSums);

        for(Map.Entry<User, BigDecimal> entry : userSums.entrySet()) {
            UserBalance userBalance = userBalanceRepository.findByUserIdAndPaymentGroup(entry.getKey().getId(), paymentGroup);
            userBalance.setBalance(entry.getValue().subtract(average));
            userBalanceRepository.save(userBalance);
        }
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
            createUserBalanceIfNotExists(user.getId(), paymentGroup);
            List<Payment> payments = paymentRepository.findByUserIdAndPaymentGroup(user.getId(), paymentGroup);

            BigDecimal sum = BigDecimal.ZERO;
            for(Payment payment : payments) {
                sum = sum.add(payment.getAmount());
            }
            userSums.put(user, sum);
        }

        return userSums;
    }

    private void createUserBalanceIfNotExists(Long userId, PaymentGroup paymentGroup) {
        UserBalance balance = userBalanceRepository.findByUserIdAndPaymentGroup(userId, paymentGroup);
        if(balance == null) {
            balance = new UserBalance();
            balance.setUserId(userId);
            balance.setPaymentGroup(paymentGroup);
            userBalanceRepository.save(balance);
        }
    }

}
