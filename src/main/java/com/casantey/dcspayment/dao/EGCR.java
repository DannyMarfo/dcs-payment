package com.casantey.dcspayment.dao;

import com.casantey.dcspayment.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class EGCR {
	   
	   private String agencyUniqueReference;
	   private String receiptDescription;
	   private String recipientName;
	   private String recipientEmail;
	   private String recipientPhone;
	   private String amount;
	   private final String receiptType;
	   private final String receivedBy;

	   public EGCR() {
	      this.receiptType = Constants.EGCR_RECEIPT_TYPE;
	      this.receivedBy = Constants.EGCR_RECEIVED_BY;
	   }



	}
