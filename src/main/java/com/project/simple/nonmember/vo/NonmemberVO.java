package com.project.simple.nonmember.vo;

import org.springframework.stereotype.Component;

@Component("nonmemberVO")
public class NonmemberVO {
	
	private int nonMemOrderSeqNum;         //��ȸ�� �ֹ� ��ǰ �Ϸ� ��ȣ
	private String productNum;             //��ǰ ��ȣ
	private int nonMemOrderNum;            //��ȸ�� �ֹ� ��ȣ
	private String nonMemName;             //��ȸ�� �ֹ���
	private String nonMemPwd;             //��ȸ�� �ֹ� ��й�ȣ
	private String nonEmail;             //��ȸ�� �̸���
	private String nonMemPhoneNum;             //��ȸ�� ��ȭ��ȣ
	private String nonMemSpName;             //��ȸ�� �޴� ���
	private String nonMemSpPhoneNum1;             //��ȸ�� �޴� ��� ��ȭ��ȣ1
	private String nonMemSpPhoneNum2;             //��ȸ�� �޴� ��� ��ȭ��ȣ2
	private String nonMemSpAdr;             //��ȸ�� �޴� ��� �ּ�
	private String nonMemOrderMsg;             //�ֹ� �޽���
	private String nonMemDepositorName;             //
	private String nonMemPaymentMethod;             //���� ���
	private int totalPrice;                   //�� ����
	private String nonMemOrderDate;             //�ֹ� ��¥
	private int productCnt;
	private String productPrice;
	private String productImage;
	private String currentStatus;
	private String productName;
	private String option1value;
	private String option2value;
	private String option1name;
	private String option2name; 
	private String deliveryStatus; 
	
	public NonmemberVO() {

	}
	public int getnonMemOrderSeqNum() {
		return nonMemOrderSeqNum;
	}
	public void setnonMemOrderSeqNum(int nonMemOrderSeqNum) {
		this.nonMemOrderSeqNum = nonMemOrderSeqNum;
	}
	public String getproductNum() {
		return productNum;
	}
	public void setproductNum(String productNum) {
		this.productNum = productNum;
	}
	public int getnonMemOrderNum() {
		return nonMemOrderNum;
	}
	public void setnonMemOrderNum(int nonMemOrderNum) {
		this.nonMemOrderNum = nonMemOrderNum;
	}
	public String getnonMemName() {
		return nonMemName;
	}
	public void setnonMemName(String nonMemName) {
		this.nonMemName = nonMemName;
	}
	public String getnonMemPwd() {
		return nonMemPwd;
	}
	public void setnonMemPwd(String nonMemPwd) {
		this.nonMemPwd = nonMemPwd;
	}
	public String getnonEmail() {
		return nonEmail;
	}
	public void setnonEmail(String nonEmail) {
		this.nonEmail = nonEmail;
	}
	public String getnonMemPhoneNum() {
		return nonMemPhoneNum;
	}
	public void setnonMemPhoneNum(String nonMemPhoneNum) {
		this.nonMemPhoneNum = nonMemPhoneNum;
	}
	public String getnonMemSpName() {
		return nonMemSpName;
	}
	public void setnonMemSpName(String nonMemSpName) {
		this.nonMemSpName = nonMemSpName;
	}
	public String getnonMemSpPhoneNum1() {
		return nonMemSpPhoneNum1;
	}
	public void setnonMemSpPhoneNum1(String nonMemSpPhoneNum1) {
		this.nonMemSpPhoneNum1 = nonMemSpPhoneNum1;
	}
	public String getnonMemSpPhoneNum2() {
		return nonMemSpPhoneNum2;
	}
	public void setnonMemSpPhoneNum2(String nonMemSpPhoneNum2) {
		this.nonMemSpPhoneNum2 = nonMemSpPhoneNum2;
	}
	public String getnonMemSpAdr() {
		return nonMemSpAdr;
	}
	public void setnonMemSpAdr(String nonMemSpAdr) {
		this.nonMemSpAdr = nonMemSpAdr;
	}
	public String getnonMemOrderMsg() {
		return nonMemOrderMsg;
	}
	public void setnonMemOrderMsg(String nonMemOrderMsg) {
		this.nonMemOrderMsg = nonMemOrderMsg;
	}
	public String getnonMemDepositorName() {
		return nonMemDepositorName;
	}
	public void setnonMemDepositorName(String nonMemDepositorName) {
		this.nonMemDepositorName = nonMemDepositorName;
	}
	public String getnonMemPaymentMethod() {
		return nonMemPaymentMethod;
	}
	public void setnonMemPaymentMethod(String nonMemPaymentMethod) {
		this.nonMemPaymentMethod = nonMemPaymentMethod;
	}
	public int gettotalPrice() {
		return totalPrice;
	}
	public void settotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getnonMemOrderDate() {
		return nonMemOrderDate;
	}
	public void setnonMemOrderDate(String nonMemOrderDate) {
		this.nonMemOrderDate = nonMemOrderDate;
	}
	public int getproductCnt() {
		return productCnt;
	}
	public void setproductCnt(int productCnt) {
		this.productCnt = productCnt;
	}
	public String getproductPrice() {
		return productPrice;
	}
	public void setproductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getproductImage() {
		return productImage;
	}
	public void setproductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getcurrentStatus() {
		return currentStatus;
	}
	public void setcurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getOption1value() {
		return option1value;
	}
	public void setOption1value(String option1value) {
		this.option1value = option1value;
	}
	public String getOption2value() {
		return option2value;
	}
	public void setOption2value(String option2value) {
		this.option2value = option2value;
	}
	public String getOption1name() {
		return option1name;
	}
	public void setOption1name(String option1name) {
		this.option1name = option1name;
	}
	public String getOption2name() {
		return option2name;
	}
	public void setOption2name(String option2name) {
		this.option2name = option2name;
	}
	
	public String getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	

}
