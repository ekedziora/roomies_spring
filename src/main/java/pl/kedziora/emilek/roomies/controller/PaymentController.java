package pl.kedziora.emilek.roomies.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.kedziora.emilek.json.objects.data.BudgetData;
import pl.kedziora.emilek.json.objects.params.AddPaymentParams;
import pl.kedziora.emilek.json.objects.params.DeletePaymentParams;
import pl.kedziora.emilek.json.objects.params.RequestParams;
import pl.kedziora.emilek.roomies.service.PaymentService;

@RestController
@RequestMapping("payments")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "getData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public BudgetData getPaymentData(@RequestBody RequestParams params) {
        preHandle(params);

        return paymentService.getBudgetData(params.getMail());
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deletePayment(@RequestBody DeletePaymentParams params) {
        preHandle(params.getRequestParams());

        paymentService.deletePayment(params.getPaymentId());
    }

    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addPayment(@RequestBody AddPaymentParams params) {
        preHandle(params.getParams());

        paymentService.addPayment(params);
    }

}
