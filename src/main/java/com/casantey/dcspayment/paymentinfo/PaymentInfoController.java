package com.casantey.dcspayment.paymentinfo;

import com.casantey.dcspayment.dao.EGCR;
import com.casantey.dcspayment.dao.InvoiceModel;
import com.casantey.dcspayment.dao.InvoiceResponse;
import com.casantey.dcspayment.email.EmailMessaging;
import com.casantey.dcspayment.email.EmailService;
import com.casantey.dcspayment.util.Constants;
import com.casantey.dcspayment.util.HTTPHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController @RequiredArgsConstructor @Slf4j
@RequestMapping(path = "/api")
public class PaymentInfoController {

    private final PaymentInfoService service;
    private final EmailService emailService;


    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        return new ResponseEntity<>("Welcome to DCS Payment Endpoint",HttpStatus.OK);
    }

    @GetMapping("/billNo/{billNo}")
    public ResponseEntity<PaymentInfo> getByBillNo(@PathVariable String billNo){
        return new ResponseEntity<>(service.findByBillNo(billNo),HttpStatus.OK);
    }

    @GetMapping("/egcr/{egcr}")
    public ResponseEntity<PaymentInfo> getByEgcr(@PathVariable String egcr){
        return new ResponseEntity<>(service.findByEgcrNo(egcr),HttpStatus.OK);
    }

    @GetMapping("/clientReference/{clientReference}")
    public ResponseEntity<PaymentInfo> getByClientReference(@PathVariable String clientReference){
        return new ResponseEntity<>(service.findByClientReference(clientReference),HttpStatus.OK);
    }

    @GetMapping("/checkoutId/{checkoutId}")
    public ResponseEntity<PaymentInfo> getByCheckoutId(@PathVariable String checkoutId){
        return new ResponseEntity<>(service.findByCheckoutId(checkoutId),HttpStatus.OK);
    }


    @PostMapping("/createInvoice")
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody PaymentInfo info){

        String finalCallback = info.getCallbackUrl();
        String defaultCallback = Constants.INIT_CALLBACK;

        InvoiceModel model = new InvoiceModel();
        model.setCallbackUrl(defaultCallback);
        model.setCancellationUrl(info.getCancellationUrl());
        model.setReturnUrl(info.getReturnUrl());
        model.setDescription(info.getDescription());
        model.setClientReference(info.getClientReference());
        model.setTotalAmount(info.getTotalAmount());
        model.setMerchantAccountNumber(Constants.MERCHANT_ID);

        log.info("InvoiceReq: {}",model);

        try {
            String postInvoice = HTTPHandler.postObject(Constants.HUBTEL_WEB_CHECK_OUT_URL,
                    new Gson().toJson(model), Constants.HUBTEL_CHECK_OUT_AUTHORIZATION);
            log.info("Post Invoice: {}",postInvoice);
            if (postInvoice != null) {

                ObjectMapper mapper = new ObjectMapper();
                InvoiceResponse response = mapper.readValue(postInvoice, InvoiceResponse.class);

                // If invoice is created successfully
                if (response.getResponseCode().equals("0000")) {
                    info.setCheckoutId(response.getData().getCheckoutId());
                    info.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    info.setCallbackUrl(finalCallback);
                    double total = info.getBillAmount() + info.getServiceCharge();
                    info.setTotalAmount(total);

                    // Save invoice info into db table
                    PaymentInfo p = service.savePaymentInfo(info);
                    if (p != null) {
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
                }

            }

        } catch (Exception e) {
            log.error("Create Invoice error: {}",e.getLocalizedMessage());
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/updatePaymentRecord")
    public void updatePaymentRecord(@RequestBody String json){
        log.info("Payment Response from Hubtel: {}",json);
        try {
            if (json != null) {
                JSONObject obj = new JSONObject(json);
                String status = obj.getString("Status");
                JSONObject data = obj.getJSONObject("Data");
                String clientRef = data.getString("ClientReference");
                double amount = data.getDouble("Amount");
//		    String phone = data.getString("CustomerPhoneNumber");
//		    String desc = data.getString("Description");
//			String checkoutId = data.getString("CheckoutId");
//		    String salesInvoiceId = data.getString("SalesInvoiceId");
                JSONObject det = data.getJSONObject("PaymentDetails");
                String momo = det.getString("MobileMoneyNumber");
                String type = det.getString("PaymentType");
//				obj.getJSONObject("Data").getJSONObject("PaymentDetails").setString("PaymentType")
//			String channel = "MoMo";
//			if(type.contains("card")) {
//				channel = "Card";
//			}
                if (status.equals("Success")) {
                    status = "Paid";
                }
                PaymentInfo p = service.findByClientReference(clientRef);
                 if (p != null && p.getClientReference().equals(clientRef)) {
                            p.setStatus(status);
                            p.setPaymentMode(type);
                            p.setPhone(momo);
                            p.setPaymentAmount(amount);
                            p.setPaymentDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                 }
                 PaymentInfo updated = service.savePaymentInfo(p);
                 log.info("Callback payment update: {}",updated);
                 if (updated != null) {
                     processReceipt(updated);
                     //Add egcrlink to json before posting to LC
                     //Do same for bank payments
                     HTTPHandler.postObject(updated.getCallbackUrl(), json, "");
                 }
            }
        } catch (Exception e) {
            System.out.println("Callback Ini Error: " + e.getLocalizedMessage());
        }
    }

    private void processReceipt(PaymentInfo updated) {
        try {
            if (updated.getStatus().equals("Paid")) {
                EGCR e = new EGCR();
                e.setAgencyUniqueReference(updated.getBillNo());
                e.setAmount(String.valueOf(updated.getTotalAmount()));
                e.setReceiptDescription(updated.getDescription());
                e.setRecipientEmail(updated.getEmail());
                e.setRecipientName(updated.getName());
                e.setRecipientPhone(updated.getPhone());
                log.info("EGCR Request Model: {}",e);

                String gcr = fetchEGCR(e);
                log.info("EGCR Response Model: {}"+gcr);
                if (gcr != null) {

                    System.out.println("Generated EGCR: "+gcr);
                    JSONObject jsonObj = new JSONObject(gcr);
                    String receiptNumber = jsonObj.getString("receiptNumber");

                    updated.setEgcrDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    updated.setEgcrNo(receiptNumber);
                    updated.setEgcrLink(Constants.GET_EGCR_URL + updated.getEgcrNo() + "/receiptfile");
                    String egcrRef = UUID.randomUUID().toString().replace("-", "");
                    updated.setEgcrRef(egcrRef+new SimpleDateFormat("YYMMddHHmmss").format(new Date()));
                    service.savePaymentInfo(updated);

                    if (receiptNumber != null) {
                        if(updated.getEgcrLink() != null) {
                            EmailMessaging msg = new EmailMessaging();
                            String message = "" +
                                    "<head><link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@100&display=swap\" rel=\"stylesheet\"></head><body style=\"font-family: 'Montserrat', sans-serif;\"><section style=\"margin-left:10%; margin-right: 10%; \"><header style=\"background-color: #27C5DA; padding-top:20px; padding-bottom:20px; padding-left:10px; font-size:20px; color: white;\">Official Receipt</header><h3>Dear "
                                        + updated.getName()
                                        + ",</h3><hr style=\"color:#CFEFF3\"/><br/><p>Please find the link to your electronic receipt for your service from Property.Gov below.</p><br/><h6>Please download receipt <a href=\"https://payment.casantey.com/dcs_egcr/Receipt?number="
                                        + updated.getEgcrRef()
                                        + "\">here</a>.</h6><br/><p>Best Regards.</p></section></body>";

                            msg.setEmail(updated.getEmail());
                            msg.setRecipientName(updated.getName());
                            msg.setSubject("Official Receipt - Property.Gov");
                            msg.setMessage(message);

                            emailService.sendHTMLEmail(
                                    msg.getEmail(),
                                    msg.getRecipientName(),
                                    msg.getSubject(),
                                    msg.getMessage()
                            );
                        }
                    }
                }
            }
//		}
        }catch(Exception e) {
            log.error("EGCR Email Error: {}",e.getMessage());
        }
    }

    private String fetchEGCR(EGCR egcr) {
        return HTTPHandler.fetchEGCR(Constants.EGCR_URL, new Gson().toJson(egcr), Constants.EGCR_KEY);
    }

}
