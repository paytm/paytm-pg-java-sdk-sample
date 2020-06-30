
/*******************************************************************************
 * Copyright (C) 2019 Paytm
 ******************************************************************************/

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.apache.commons.lang3.StringUtils;

import com.paytm.merchant.models.PaymentDetail;
import com.paytm.merchant.models.PaymentStatusDetail;
import com.paytm.merchant.models.RefundDetail;
import com.paytm.merchant.models.RefundStatusDetail;
import com.paytm.merchant.models.SDKResponse;
import com.paytm.merchant.models.Time;
import com.paytm.pg.Payment;
import com.paytm.pg.Refund;
import com.paytm.pg.constants.LibraryConstants;
import com.paytm.pg.constants.MerchantProperties;
import com.paytm.pg.utils.CommonUtil;
import com.paytm.pgplus.enums.EChannelId;
import com.paytm.pgplus.enums.EnumCurrency;
import com.paytm.pgplus.models.ExtendInfo;
import com.paytm.pgplus.models.GoodsInfo;
import com.paytm.pgplus.models.Money;
import com.paytm.pgplus.models.PaymentMode;
import com.paytm.pgplus.models.ShippingInfo;
import com.paytm.pgplus.models.UserInfo;
import com.paytm.pgplus.response.AsyncRefundResponse;
import com.paytm.pgplus.response.InitiateTransactionResponse;
import com.paytm.pgplus.response.NativePaymentStatusResponse;
import com.paytm.pgplus.response.NativeRefundStatusResponse;




/**
 * This class shows of how to initialize and make API calls. Here Merchant will
 * change data as his requirement and make the API calls
 */
public class Demo {

	/**
	 * DemoApp main method.
	 * 
	 * @param args main method arguments
	 */
	public static void main(String[] args) {

		/** Setting all initial Parameters such as merchant key and id */
		setInitialParameters();

		/** Example using only mandatory fields */
		createTxnTokenwithRequiredParams();

		/** Example using mandatory and enabling and disabling payment modes fields */
		createTxnTokenwithPaytmSSotokenAndPaymentMode();

		/** Example using all fields */
		createTxnTokenwithAllParams();

		/** Example to get Payment status */
		getPaymentStatus();

		/** Example for calling Refund */
		initiateRefund();

		/** Example to get Refund status */
		getRefundStatus();
	}

	/**
	 * setInitialParameters is used to set the initial parameters required to call
	 * API.
	 * 
	 * Merchant can change these information according to his need. Logger,
	 * Connection timeout, information regarding merchant will be initialized.
	 */
	public static void setInitialParameters() {

		/** Set Logger Level for Paytm logs */
		LibraryConstants.LOGGER.setLevel(Level.ALL);
		/** setting log file path "/paytm/MyLogFile.log" */
		try {
			FileHandler fh = null;
			fh = new FileHandler("/paytm/MyLogFile.log");
			fh.setFormatter(new SimpleFormatter());
			LibraryConstants.LOGGER.addHandler(fh);

			/** Removing console handler from logger */
			LibraryConstants.LOGGER.setUseParentHandlers(false);
		} catch (IOException e) {
			LibraryConstants.LOGGER.log(Level.SEVERE, CommonUtil.getLogMessage(e.toString()), e);
		}

		/** Initialize mandatory Parameters */
		String env = LibraryConstants.STAGING_ENVIRONMENT;
		// Find your Merchant ID and Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
		String mid = "YOUR_MID_HERE";
		String key = "YOUR_KEY_HERE";
       /* Website: For Staging - WEBSTAGING, For Production - DEFAULT */
		String website = "YOUR_WEBSITE_NAME";
		/* Client Id e.g C11 */
		String clientid = "YOUR_CLIENT_ID_HERE";

        /** Setting Callback URL */
		String callbackUrl = "MERCHANT_CALLBACK_URL";
		MerchantProperties.setCallbackUrl(callbackUrl);

		/** Setting Initial Parameters */
		MerchantProperties.initialize(env, mid, key, clientid, website);

		/** Setting timeout for connection i.e. Connection Timeout */
		MerchantProperties.setConnectionTimeout(new Time(5, TimeUnit.MINUTES));
	}

	/**
	 * Merchant can use createTxnTokenwithRequiredParams method to get token with
	 * required parameters.
	 * 
	 * This method create a PaymentDetail object having all the required parameters
	 * (Merchant can change these values according to his requirements) and calls
	 * SDK's createTxnToken method to get the
	 * {@link SDKResponse}(InitiateTransactionResponse) object having token which
	 * will be used in future transactions such as getting payment options.
	 */
	public static void createTxnTokenwithRequiredParams() {

		/** ..... Merchants code here .... */
		/** 1. Merchants who only want to use PG for accepting payments */

		/** Channel through which call initiated [enum (APP, WEB, WAP, SYSTEM)] */
		EChannelId channelId = EChannelId.WEB;

		/** Unique order for each order request */
		String orderId = Sample.generateRandomString(6);

		/** Transaction amount and the currency value */
		Money txnAmount = new Money(EnumCurrency.INR, "1.00");

		/**
		 * User information contains user details cid : <Mandatory> user unique
		 * identification with respect to merchant
		 */
		UserInfo userInfo = new UserInfo();
		userInfo.setCustId("clientid");

		/**
		 * paymentDetails object will have all the information required to make
		 * createTxnToken call
		 */
		PaymentDetail paymentDetails = new PaymentDetail.PaymentDetailBuilder(channelId, orderId, txnAmount, userInfo)
				.build();
		/**
		 * Making call to SDK method which will return the
		 * SDKResponse(InitiateTransactionResponse) that holds the createTxnToken result
		 * parameter which will be used by merchant in future API calls.
		 */
		SDKResponse<InitiateTransactionResponse> response = Payment.createTxnToken(paymentDetails);
		System.out.println(response);
		/** ..... Merchants code here .... */

	}

	/** End of Function */

	/**
	 * Merchant can use createTxnTokenwithPaytmSSotokenAndPaymentMode method to get
	 * token with defined Payment mode parameters.
	 * 
	 * This method create a PaymentDetail object having all the required parameters
	 * and payment modes (Merchant can change these values according to his
	 * requirements) and calls SDK's createTxnToken method to get the
	 * {@link SDKResponse}(InitiateTransactionResponse) object having token which
	 * will be used in future transactions such as getting payment options.
	 * 
	 * Merchant can only use payment modes for this transaction which are applicable
	 * on the merchant.
	 */
	public static void createTxnTokenwithPaytmSSotokenAndPaymentMode() {

		/** ..... Merchants code here .... */
		/**
		 * 2. Merchants who want to use PG with Wallet and configure paymentmodes for
		 * accepting payments with paytmSSOTokenS
		 */

		/** Channel through which call initiated [enum (APP, WEB, WAP, SYSTEM)] */
		EChannelId channelId = EChannelId.WEB;
		/** Unique order for each order request */
		String orderId = Sample.generateRandomString(6);
		/** Transaction amount and the currency value */
		Money txnAmount = new Money(EnumCurrency.INR, "1.00");
		/**
		 * User information contains user details cid : <Mandatory> user unique
		 * identification with respect to merchant
		 */
		UserInfo userInfo = new UserInfo();
		userInfo.setCustId("clientid");

		/** Paytm Token for a user */
		String paytmSsoToken = StringUtils.EMPTY;
		/**
		 * list of the payment modes which needs to enable. If the value provided then
		 * only listed payment modes are available for transaction
		 */
		List<PaymentMode> enablePaymentMode = Sample.getEnablePaymentModes();
		/**
		 * list of the payment modes which need to disable. If the value provided then
		 * all the listed payment modes are unavailable for transaction
		 */
		List<PaymentMode> disablePaymentMode = Sample.getdisablePaymentModes();
		/**
		 * paymentDetails object will have all the information required to make
		 * createTxnToken call
		 */
		PaymentDetail paymentDetails = new PaymentDetail.PaymentDetailBuilder(channelId, orderId, txnAmount, userInfo)
				.setPaytmSsoToken(paytmSsoToken).setEnablePaymentMode(enablePaymentMode)
				.setDisablePaymentMode(disablePaymentMode).build();
		/**
		 * Making call to SDK method which will return the
		 * SDKResponse(InitiateTransactionResponse) that holds the createTxnToken result
		 * parameter which will be used by merchant in future API calls.
		 */
		SDKResponse<InitiateTransactionResponse> response = Payment.createTxnToken(paymentDetails);
		System.out.println(response);

		/** ..... Merchants code here .... */

	}

	/** End of Function */

	/**
	 * Merchant can use createTxnTokenwithAllParams method to get token with defined
	 * Payment mode parameters.
	 * 
	 * This method create a PaymentDetail object having all parameters (Merchant can
	 * change these values according to his requirements) and calls SDK's
	 * createTxnToken method to get the
	 * {@link SDKResponse}(InitiateTransactionResponse) object having token which
	 * will be used in future transactions such as getting payment options.
	 * 
	 * Merchant can only use payment modes for this transaction which are applicable
	 * on the merchant.
	 */
	public static void createTxnTokenwithAllParams() {

		/** ..... Merchants code here .... */
		/**
		 * 3. Merchants who want to use PG with Wallet, configure paymentmodes, send
		 * Order details Subscription Information and Extended Information for accepting
		 * payments
		 */

		/** Channel through which call initiated [enum (APP, WEB, WAP, SYSTEM)] */
		EChannelId channelId = EChannelId.WEB;
		/** Unique order for each order request */
		String orderId = Sample.generateRandomString(6);
		/** Transaction amount and the currency value */
		Money txnAmount = new Money(EnumCurrency.INR, "1.00");
		/**
		 * User information contains user details cid : <Mandatory> user unique
		 * identification with respect to merchant
		 */
		UserInfo userInfo = new UserInfo();
		userInfo.setCustId("clientid");

		/** Paytm Token for a user */
		String paytmSsoToken = StringUtils.EMPTY;
		/**
		 * list of the payment modes which needs to enable. If the value provided then
		 * only listed payment modes are available for transaction
		 */
		List<PaymentMode> enablePaymentMode = Sample.getEnablePaymentModes();
		/**
		 * list of the payment modes which need to disable. If the value provided then
		 * all the listed payment modes are unavailable for transaction
		 */
		List<PaymentMode> disablePaymentMode = Sample.getdisablePaymentModes();

		/** This contain the Goods info for an order. */
		List<GoodsInfo> goods = Sample.getGoodsInfo();
		/** This contain the shipping info for an order. */
		List<ShippingInfo> shippingInfo = Sample.getShippingInfo();

		/** Promocode that user is using for the payment */
		String promoCode = StringUtils.EMPTY;
		/** This contain the set of parameters for someadditional information */
		ExtendInfo extendInfo = Sample.getExtendInfo();
		String emiOption = StringUtils.EMPTY;
		String cardTokenRequired = StringUtils.EMPTY;
		Time readTimeout = new Time(5, TimeUnit.MINUTES);
		/**
		 * PaymentDetail object will have all the information required to make
		 * createTxnToken call
		 */
		PaymentDetail paymentDetails = new PaymentDetail.PaymentDetailBuilder(channelId, orderId, txnAmount, userInfo)
				.setPaytmSsoToken(paytmSsoToken).setEnablePaymentMode(enablePaymentMode)
				.setDisablePaymentMode(disablePaymentMode).setGoods(goods).setShippingInfo(shippingInfo)
				.setPromoCode(promoCode).setExtendInfo(extendInfo).setEmiOption(emiOption)
				.setCardTokenRequired(cardTokenRequired).setReadTimeout(readTimeout).build();
		/**
		 * Making call to SDK method which will return the
		 * SDKResponse(InitiateTransactionResponse) that holds the createTxnToken result
		 * parameter which will be used by merchant in future API calls.
		 */
		SDKResponse<InitiateTransactionResponse> response = Payment.createTxnToken(paymentDetails);
		System.out.println(response);

		/** ..... Merchants code here .... */

	}

	/** End of Function */

	/**
	 * Merchant can use getPaymentStatus method to get the Payment Status of any
	 * previous transaction.
	 * 
	 * Merchant will use getPaymentStatus after Payment. This method (Mandatory
	 * Parameters)require OrderId ID. This will return the
	 * {@link SDKResponse}NativePaymentStatusResponse) object having status for the
	 * specific OrderId ID.
	 */
	public static void getPaymentStatus() {

		/** ..... Merchants code here .... */
		/** 4. Merchants who want to get TransactionStatus */

		/** Order id for which you need to know payment status */
		String orderId = "YOUR_ORDER_ID";

		Time readTimeout = new Time(5, TimeUnit.MINUTES);

		/**
		 * PaymentStatusDetail object will have all the information required to make
		 * getPaymentStatus call
		 */
		PaymentStatusDetail paymentStatusDetail = new PaymentStatusDetail.PaymentStatusDetailBuilder(orderId)
				.setReadTimeout(readTimeout).build();

		/**
		 * Making call to SDK method which will return the
		 * SDKResponse(NativePaymentStatusResponse) that holds Payment Status of any
		 * previous transaction.
		 */
		SDKResponse<NativePaymentStatusResponse> response = Payment.getPaymentStatus(paymentStatusDetail);
		System.out.println(response);

		/** ..... Merchants code here .... */

	}

	/**
	 * Merchant can use initiateRefund method to initiate the refund of any
	 * successful previous transaction.
	 * 
	 * Merchant will use doRefund after Payment. This method (Mandatory
	 * Parameters)require Transaction ID, Transaction Type and Refund Amount. This
	 * will initiate the refund for the specific Transaction ID and will return the
	 * {@link SDKResponse}(AsyncRefundResponse) object having refund details for the
	 * specific refund.
	 */
	public static void initiateRefund() {

		/** ..... Merchants code here .... */
		/** 5. Merchants who want to do refund */

		/** Order id for which refund request needs to be raised */
		String orderId = "YOUR_ORDER_ID";
		/** Unique refund id */
		String refId = "UNIQUE_REFUND_ID";
		/** Transaction ID returned in Paytm\pg\process\PaymentStatus Api */
		String txnId = "PAYTM_TRANSACTION_ID";
		/** Transaction Type for refund */
		String txnType = "REFUND";

		/**
		 * Refund Amount to be refunded (should not be greater than the Amount paid in
		 * the Transaction)
		 */
		String refundAmount = "1";
		Time readTimeout = new Time(5, TimeUnit.MINUTES);

		/**
		 * refund object will have all the information required to make refund call
		 */
		RefundDetail refundDetail = new RefundDetail.RefundDetailBuilder(orderId, refId, txnId, txnType, refundAmount)
				.setReadTimeout(readTimeout).build();

		/**
		 * Making call to SDK method which will return a
		 * SDKResponse(AsyncRefundResponse) object that will contain the Refund Response
		 * regarding the Transaction Id
		 */
		SDKResponse<AsyncRefundResponse> response = Refund.initiateRefund(refundDetail);
		System.out.println(response);

		/** ..... Merchants code here .... */

	}

	/** End of Function */

	/**
	 * Merchant can use getRefundStatus method to get the Status of any previous
	 * Refund transaction.
	 * 
	 * Merchant will use getRefundStatus after Payment. This method (Mandatory
	 * Parameters)require OrderId ID and refId. This will return the
	 * {@link SDKResponse}NativeRefundStatusResponse) object having status for the
	 * specific Refund.
	 */
	public static void getRefundStatus() {

		/** ..... Merchants code here .... */
		/** 6. Merchants who want to get Refund Status */

		/** Order id for which refund status needs to be checked */
		String orderId = "YOUR_ORDER_ID";
		 /** Refund id of the refund request for which refund status needs to be checked */
		String refId = "YOUR_REFUND_ID";

		Time readTimeout = new Time(5, TimeUnit.MINUTES);

		/**
		 * RefundStatusDetail object will have all the information required to make
		 * getRefundStatus call
		 */
		RefundStatusDetail refundStatusDetail = new RefundStatusDetail.RefundStatusDetailBuilder(orderId, refId)
				.setReadTimeout(readTimeout).build();

		/**
		 * Making call to SDK method which will return the
		 * SDKResponse(NativeRefundStatusRequest) that holds Refund Status of any
		 * previous Refund.
		 */
		SDKResponse<NativeRefundStatusResponse> response = Refund.getRefundStatus(refundStatusDetail);
		System.out.println(response);

		/** ..... Merchants code here .... */

	}

}
