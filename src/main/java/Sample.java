/*******************************************************************************
 * Copyright (C) 2019 Paytm
 ******************************************************************************/


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.paytm.pg.constants.LibraryConstants;
import com.paytm.pgplus.enums.EChannelId;
import com.paytm.pgplus.enums.UserSubWalletType;
import com.paytm.pgplus.models.ExtendInfo;
import com.paytm.pgplus.models.GoodsInfo;
import com.paytm.pgplus.models.Money;
import com.paytm.pgplus.models.PaymentMode;
import com.paytm.pgplus.models.ShippingInfo;

public class Sample {

	/**
	 * getEnablePaymentModes returns a list of Payment modes which merchant wants to
	 * enable. Currently this returns null.
	 * 
	 * @return List of PaymentMode
	 */
	public static List<PaymentMode> getEnablePaymentModes() {
		List<PaymentMode> list = new ArrayList<PaymentMode>();
		PaymentMode paymentMode1 = new PaymentMode("CC");
		paymentMode1.setChannels(new ArrayList<String>());
		paymentMode1.getChannels().add(EChannelId.WEB.getValue());
		paymentMode1.getChannels().add(EChannelId.APP.getValue());

		PaymentMode paymentMode2 = new PaymentMode("DC");
		paymentMode2.setChannels(new ArrayList<String>());
		paymentMode2.getChannels().add(EChannelId.WEB.getValue());
		paymentMode2.getChannels().add(EChannelId.APP.getValue());

		list.add(paymentMode1);
		list.add(paymentMode2);

		return list;
	}

	/**
	 * getdisablePaymentModes returns a list of Payment modes which merchant wants
	 * to disable. Currently this returns null.
	 * 
	 * @return List of PaymentMode
	 */
	public static List<PaymentMode> getdisablePaymentModes() {
		List<PaymentMode> list = new ArrayList<PaymentMode>();
		PaymentMode paymentMode1 = new PaymentMode("CC");
		paymentMode1.setChannels(new ArrayList<String>());
		paymentMode1.getChannels().add(EChannelId.WEB.getValue());
		paymentMode1.getChannels().add(EChannelId.APP.getValue());

		PaymentMode paymentMode2 = new PaymentMode("DC");
		paymentMode2.setChannels(new ArrayList<String>());
		paymentMode2.getChannels().add(EChannelId.WEB.getValue());
		paymentMode2.getChannels().add(EChannelId.APP.getValue());

		list.add(paymentMode1);
		list.add(paymentMode2);

		return list;
	}

	/**
	 * getGoodsInfo returns a list of GoodsInfo Object for the required payment.
	 * Currently this returns null.
	 * 
	 * @return List of GoodsInfo
	 */
	public static List<GoodsInfo> getGoodsInfo() {
		List<GoodsInfo> list = new ArrayList<GoodsInfo>();
		GoodsInfo goodsInfo1 = new GoodsInfo();
		goodsInfo1.setMerchantShippingId("merchantShippingId");
		goodsInfo1.setMerchantGoodsId("merchantGoodsId");
		goodsInfo1.setSnapshotUrl("snapshotUrl");
		goodsInfo1.setDescription("description");
		goodsInfo1.setCategory("category");
		goodsInfo1.setQuantity("quantity");
		goodsInfo1.setUnit("unit");
		goodsInfo1.setPrice(new Money("1.00"));
		goodsInfo1.setExtendInfo(getExtendInfo());

		GoodsInfo goodsInfo2 = new GoodsInfo();
		goodsInfo2.setMerchantShippingId("merchantShippingId");
		goodsInfo2.setMerchantGoodsId("merchantGoodsId");
		goodsInfo2.setSnapshotUrl("snapshotUrl");
		goodsInfo2.setDescription("description");
		goodsInfo2.setCategory("category");
		goodsInfo2.setQuantity("quantity");
		goodsInfo2.setUnit("unit");
		goodsInfo2.setPrice(new Money("1.00"));
		goodsInfo2.setExtendInfo(getExtendInfo());

		list.add(goodsInfo1);
		list.add(goodsInfo2);

		return list;
	}

	/**
	 * getShippingInfo returns a list of GoodsInfo Object for the required payment.
	 * Currently this returns null.
	 * 
	 * @return List of ShippingInfo
	 */
	public static List<ShippingInfo> getShippingInfo() {
		List<ShippingInfo> list = new ArrayList<ShippingInfo>();
		ShippingInfo shippingInfo1 = new ShippingInfo();
		shippingInfo1.setFirstName("first name");
		shippingInfo1.setLastName("last name");
		shippingInfo1.setEmail("XXX@gmail.com");
		shippingInfo1.setMerchantShippingId("sid");
		shippingInfo1.setAddress1("address1");
		shippingInfo1.setAddress2("address2");
		shippingInfo1.setCarrier("carrier");
		shippingInfo1.setMobileNo("9999999999");
		shippingInfo1.setChargeAmount(new Money("1.00"));
		shippingInfo1.setCityName("cityName");
		shippingInfo1.setStateName("stateName");
		shippingInfo1.setTrackingNo("xxxxxxxxx");

		ShippingInfo shippingInfo2 = new ShippingInfo();
		shippingInfo2.setFirstName("first name");
		shippingInfo2.setLastName("last name");
		shippingInfo2.setEmail("XXX@gmail.com");
		shippingInfo2.setMerchantShippingId("sid");
		shippingInfo2.setAddress1("address1");
		shippingInfo2.setAddress2("address2");
		shippingInfo2.setCarrier("carrier");
		shippingInfo2.setMobileNo("9999999999");
		shippingInfo2.setChargeAmount(new Money("1.00"));
		shippingInfo2.setCityName("cityName");
		shippingInfo2.setStateName("stateName");
		shippingInfo2.setTrackingNo("xxxxxxxxx");

		list.add(shippingInfo1);
		list.add(shippingInfo2);

		return list;
	}

	/**
	 * getExtendInfo returns a ExtendInfo Object for the required payment.
	 * 
	 * @return ExtendInfo custom object
	 */
	public static ExtendInfo getExtendInfo() {
		ExtendInfo extendInfo = new ExtendInfo();
		extendInfo.setUdf1("udf1");
		extendInfo.setUdf2("udf2");
		extendInfo.setUdf3("udf3");
		extendInfo.setMercUnqRef("mercUnqRef");
		extendInfo.setComments("comment");
		extendInfo.setAmountToBeRefunded("1");
		Map<String, String> subwalletAmount = new HashMap<String, String>();
		subwalletAmount.put(LibraryConstants.Request.FOOD_WALLET, "2");
		subwalletAmount.put(LibraryConstants.Request.GIFT_WALLET, "2.5");
		extendInfo.setSubwalletAmount(subwalletAmount);
		return extendInfo;
	}

	/**
	 * getSubwalletAmount returns a Map(UserSubWalletType, BigDecimal) Object for
	 * the required Refund.
	 * 
	 * @return Map of (UserSubWalletType, BigDecimal) custom object
	 */
	public static Map<UserSubWalletType, BigDecimal> getSubwalletAmount() {
		Map<UserSubWalletType, BigDecimal> subwalletAmount = new TreeMap<UserSubWalletType, BigDecimal>();
		subwalletAmount.put(UserSubWalletType.FOOD, new BigDecimal("1.00"));
		subwalletAmount.put(UserSubWalletType.GIFT, new BigDecimal("10.00"));
		return subwalletAmount;
	}

	/**
	 * getExtraParamsMap returns a Map(String, Object) Object for the required
	 * Refund.
	 * 
	 * @return Map of (String, Object) custom object
	 */
	public static Map<String, Object> getExtraParamsMap() {
		Map<String, Object> extraParamsMap = new TreeMap<String, Object>();
		extraParamsMap.put("purpose", "merchant purpose");
		extraParamsMap.put("data", "merchant data");
		return extraParamsMap;
	}

	/**
	 * generateRandomString returns a random String for orderID.
	 * 
	 * @param count length of random string to be generated
	 * @return String random
	 */
	public static String generateRandomString(int count) {

		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = new Random().nextInt(9);
			builder.append(character);
		}
		return builder.toString();
	}

}
