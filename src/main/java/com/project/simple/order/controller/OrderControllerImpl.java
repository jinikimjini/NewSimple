package com.project.simple.order.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.simple.cart.service.CartService;
import com.project.simple.cart.vo.CartVO;
//import com.bookshop01.common.base.BaseController;
//import com.bookshop01.goods.vo.GoodsVO;
import com.project.simple.member.vo.MemberVO;
import com.project.simple.order.service.OrderService;
import com.project.simple.order.vo.OrderVO;
import com.project.simple.page.Criteria;
import com.project.simple.page.PageMaker;

@Controller("orderController")
public class OrderControllerImpl implements OrderController {
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderVO orderVO;
	@Autowired
	private MemberVO memberVO;

	// ��ٱ��Ͽ��� �ֹ������� �̵�(ȸ��/��ȸ��)
	@RequestMapping(value = "/order.do", method = RequestMethod.POST)
	private ModelAndView order(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();

		List<CartVO> cartlist = (ArrayList) session.getAttribute("cartlist");
		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");

		if (isLogOn == null) {

			session.removeAttribute("orderlist");
			List<CartVO> list = (ArrayList) session.getAttribute("orderlist");

			if (list == null) {
				list = new ArrayList<CartVO>();
				session.setAttribute("orderlist", list);
			}

			String totalPrice = request.getParameter("totalPrice");
			String[] ajaxMsg01 = request.getParameterValues("valueArr");
			int[] ajaxMsg = null;
			if (ajaxMsg01 != null) {
				ajaxMsg = new int[ajaxMsg01.length];
				for (int i = 0; i < ajaxMsg01.length; i++) {
					ajaxMsg[i] = Integer.parseInt(ajaxMsg01[i]);
				}
			}
			int size = ajaxMsg01.length;
			for (int i = 0; i < size; i++) {
				int no = ajaxMsg[i];
				CartVO vo = cartlist.get(no);
				list.add(vo);
			}
			session.setAttribute("totalPrice", totalPrice);
			session.setAttribute("orderNow", false);
			session.setAttribute("orderlist", list);
			mav.setViewName("nonorder_01");
		}

		else if (isLogOn == true) {
			List<OrderVO> orderlist = new ArrayList();
			String[] ajaxMsg = request.getParameterValues("valueArr");
			String totalPrice = request.getParameter("totalPrice");
			int size = ajaxMsg.length;

			String randomnumber = numberGen(9, 1);
			int memOrderNum = Integer.parseInt(randomnumber);

			for (int i = 0; i < size; i++) {
				orderlist.add(orderService.selectcartlist(ajaxMsg[i]));
			}
			
			
			
			session.setAttribute("memCartId", ajaxMsg);
			session.setAttribute("totalPrice", totalPrice);
			session.setAttribute("orderNow", false);
			session.setAttribute("orderlist", orderlist);
			mav.setViewName("order_01");
		}
		return mav;
	}
	
	// �ֹ������� �̵�(ȸ��)
		@RequestMapping(value = "/order_01.do", method = RequestMethod.GET)
		private ModelAndView order_01(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
				HttpServletResponse response) throws Exception {

			ModelAndView mav = new ModelAndView();
			
			String randomnumber = numberGen(9, 1);
			int orderNum = Integer.parseInt(randomnumber);
			mav.addObject("orderNum", orderNum);
			return mav;

		}

		// �ֹ������� �̵�(��ȸ��)
		@RequestMapping(value = "/nonorder_01.do", method = RequestMethod.GET)
		private ModelAndView nonorder_01(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
				HttpServletResponse response) throws Exception {

			ModelAndView mav = new ModelAndView();
			
			String randomnumber = numberGen(9, 1);
			int orderNum = Integer.parseInt(randomnumber);
			mav.addObject("orderNum",orderNum);
			return mav;

		}

	// ȸ���ֹ����� DB ����(�ֹ��Ϸ�)
	@RequestMapping(value = "/memaddorderlist.do", method = RequestMethod.POST)
	private ModelAndView memaddorderlist(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.removeAttribute("totalPrice");
		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");
		MemberVO membervo = (MemberVO) session.getAttribute("member");
		String memId = membervo.getmemId();
		Boolean orderNow = (Boolean) session.getAttribute("orderNow");
		List list = new ArrayList<Integer>(); 



		if (isLogOn == true) {
			
			if ( orderNow ==  false) {
				ArrayList<OrderVO> orderlist = (ArrayList) session.getAttribute("orderlist");
				int size = orderlist.size();
				for (int i = 0; i < size; i++) {
					OrderVO vo = orderlist.get(i);
					String productNum = vo.getProductNum();
					String productName = vo.getProductName();
					String option1name = vo.getOption1name();
					String option1value = vo.getOption1value();
					String option2name = vo.getOption2name();
					String option2value = vo.getOption2value();
					String deliverycharge = vo.getDeliverycharge();
					int productCnt = vo.getProductCnt();
					String productPrice = vo.getProductPrice();
					String totalPrice = orderVO.getTotalPrice();
					String productImage = vo.getProductImage();	
					orderVO.setProductNum(productNum);
					orderVO.setMemId(memId);
					orderVO.setProductName(productName);
					orderVO.setOption1name(option1name);
					orderVO.setOption1value(option1value);
					orderVO.setOption2name(option2name);
					orderVO.setOption2value(option2value);
					orderVO.setDeliverycharge(deliverycharge);
					orderVO.setProductCnt(productCnt);
					orderVO.setProductPrice(productPrice);
					orderVO.setTotalPrice(totalPrice);
					orderVO.setProductImage(productImage);
					orderService.addNewOrder(orderVO); // ���̹�Ƽ������ �б�
				}

				String[] memCartId = (String[]) session.getAttribute("memCartId");
				for (int i = 0; i < size; i++) {
					cartService.removeCompleteCartlist(memCartId[i]);
				}
				
				int orderNum = orderVO.getMemOrderNum();
				list = orderService.selectSeqNum(orderNum);
				
				session.removeAttribute("memCartId");
				
				mav.setViewName("order_03");
			} else if( orderNow == true) {

				OrderVO order = (OrderVO) session.getAttribute("memOrder");
				String productNum = order.getProductNum();
				String productName = order.getProductName();
				String option1name = order.getOption1name();
				String option1value = order.getOption1value();
				String option2name = order.getOption2name();
				String option2value = order.getOption2value();
				String deliverycharge = order.getDeliverycharge();
				int productCnt = order.getProductCnt();
				String productPrice = order.getProductPrice();
				String totalPrice = orderVO.getTotalPrice();
				String productImage = order.getProductImage();
				orderVO.setMemId(memId);
				orderVO.setProductNum(productNum);
				orderVO.setProductName(productName);
				orderVO.setOption1name(option1name);
				orderVO.setOption1value(option1value);
				orderVO.setOption2name(option2name);
				orderVO.setOption2value(option2value);
				orderVO.setDeliverycharge(deliverycharge);		
				orderVO.setProductCnt(productCnt);
				orderVO.setProductPrice(productPrice);
				orderVO.setTotalPrice(totalPrice);
				orderVO.setProductImage(productImage);			
				orderService.addNewOrder(orderVO);
			
				mav.setViewName("order_03");
			}

		}
		return mav;
	}

	// ��ȸ���ֹ����� DB ����(�ֹ��Ϸ�)
	@RequestMapping(value = "/nonmemaddorderlist.do", method = RequestMethod.POST)
	private ModelAndView nonmemaddorderlist(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.removeAttribute("totalPrice");
		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");
		Boolean orderNow = (Boolean) session.getAttribute("orderNow");

		if (isLogOn == null) {
			
			if ( orderNow ==  false ) {
				
				ArrayList<CartVO> orderlist = (ArrayList) session.getAttribute("orderlist");
				int size = orderlist.size();
				for (int i = 0; i < size; i++) {
					CartVO vo = orderlist.get(i);
					String productNum = vo.getProductNum();
					String productName = vo.getProductName();
					String option1name = vo.getOption1name();
					String option1value = vo.getOption1value();
					String option2name = vo.getOption2name();
					String option2value = vo.getOption2value();
					String deliverycharge = vo.getDeliverycharge();
					int productCnt = vo.getProductCnt();
					String productPrice = vo.getProductPrice();
					String totalPrice = orderVO.getTotalPrice();
					String productImage = vo.getProductImage();
					orderVO.setProductNum(productNum);
					orderVO.setProductName(productName);
					orderVO.setOption1name(option1name);
					orderVO.setOption1value(option1value);
					orderVO.setOption2name(option2name);
					orderVO.setOption2value(option2value);
					orderVO.setDeliverycharge(deliverycharge);
					orderVO.setProductCnt(productCnt);
					orderVO.setProductPrice(productPrice);
					orderVO.setTotalPrice(totalPrice);
					orderVO.setProductImage(productImage);

					orderService.addNewOrder(orderVO);
				}

				session.removeAttribute("cartlist");

				mav.setViewName("order_03");
			}

			else if( orderNow ==  true) {
				OrderVO order = (OrderVO) session.getAttribute("nonMemOrder");

				String productNum = order.getProductNum();
				String productName = order.getProductName();
				String option1name = order.getOption1name();
				String option1value = order.getOption1value();
				String option2name = order.getOption2name();
				String option2value = order.getOption2value();
				String deliverycharge = order.getDeliverycharge();
				int productCnt = order.getProductCnt();
				String productPrice = order.getProductPrice();
				String totalPrice = orderVO.getTotalPrice();
				String productImage = order.getProductImage();
				orderVO.setProductNum(productNum);
				orderVO.setProductName(productName);
				orderVO.setOption1name(option1name);
				orderVO.setOption1value(option1value);
				orderVO.setOption2name(option2name);
				orderVO.setOption2value(option2value);
				orderVO.setDeliverycharge(deliverycharge);
				orderVO.setProductCnt(productCnt);
				orderVO.setProductPrice(productPrice);
				orderVO.setTotalPrice(totalPrice);
				orderVO.setProductImage(productImage);


				orderService.addNewOrder(orderVO);
	
				mav.setViewName("order_03");
			}
		}
		return mav;
	}

	// 10�ڸ� �ֹ���ȣ ���� ����
	public static String numberGen(int len, int dupCd) {

		Random rand = new Random();
		String numStr = "";

		for (int i = 0; i < len; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			if (dupCd == 1) {
				numStr += ran;
			} else if (dupCd == 2) {
				if (!numStr.contains(ran)) {
					numStr += ran;
				} else {
					i -= 1;
				}
			}
		}
		return numStr;
	}

	// �ֹ�����������̵�(ȸ��)
	@RequestMapping(value = "/memberOrderResult.do", method = RequestMethod.GET)
	private ModelAndView memberOrderResult(@RequestParam("Price") int price,
			String memPaymentMethod, @RequestParam("memOrderNum") String memOrderNum, @RequestParam("paymentMethod") String paymentMethod, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		
		int point = price/10; 
		ModelAndView mav = new ModelAndView();
		mav.addObject("Price", price);
		mav.addObject("Point", point);
		mav.addObject("memOrderNum", memOrderNum);
		mav.addObject("memPaymentMethod", paymentMethod);
		mav.setViewName("order_03");
		return mav;

	}
	
	// �ֹ�����������̵�(��ȸ��)
		@RequestMapping(value = "/nonMemberOrderResult.do", method = RequestMethod.GET)
		private ModelAndView nonMemberOrderResult(@RequestParam("Price") int price,
				String memPaymentMethod, @RequestParam("nonMemOrderNum") String nonMemOrderNum, @RequestParam("paymentMethod") String paymentMethod, HttpServletRequest request,
				HttpServletResponse response) throws Exception {
			
			
			ModelAndView mav = new ModelAndView();
			mav.addObject("Price", price);
			mav.addObject("nonMemOrderNum", nonMemOrderNum);
			mav.addObject("nonMemPaymentMethod", paymentMethod);
			mav.setViewName("order_03");
			return mav;

		}

	// ������ �ֹ���ȸ
	@Override
	@RequestMapping(value = "/admin_listorder.do", method = { RequestMethod.GET, RequestMethod.POST })

	public ModelAndView listorder(Criteria cri, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		List<OrderVO> ordersList = orderService.listOrders(cri);
		// System.out.println(ordersList);
		int orderCount = orderService.orderCount();
		ModelAndView mav = new ModelAndView(viewName);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(orderCount);
		int pageNum = pageMaker.getCri().getPage();

		mav.addObject("pageNum", pageNum);
		mav.addObject("ordersList", ordersList);
		mav.addObject("pageMaker", pageMaker);

		return mav;
	}

	// ������ �ֹ����� �󼼺���
	@RequestMapping(value = "/admin_listorder/detailorder.do", method = RequestMethod.GET)
	public ModelAndView viewMyOrderInfo(@RequestParam("memOrderNum") int memOrderNum, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();

		List<OrderVO> OrderList = orderService.memOrderNumList(memOrderNum);
		OrderVO orderVO = orderService.memOrderInfo(memOrderNum);
		MemberVO memberVO = orderService.memOrderId(memOrderNum);

		session.setAttribute("member", memberVO);
		session.setAttribute("order", orderVO);
		session.setAttribute("OrderList", OrderList);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("OrderList", OrderList);
		mav.addObject("member", memberVO);
		mav.addObject("order", orderVO);
		return mav;
	}

	// ������ �ֹ����� ����ȭ��
	@RequestMapping(value = "/admin_listorder/admin_ModVeiwMemorder.do", method = RequestMethod.GET)
	public ModelAndView admin_ModVeiwMemorder(@RequestParam("memOrderNum") int memOrderNum, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);

		return mav;
	}

	// ������ �ֹ����� ����
	@RequestMapping(value = "/admin_listorder/admin_ModMemorder.do", method = RequestMethod.POST)
	private void admin_ModMemorder(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		int result = 0;
		result = orderService.adminModMemOrder(orderVO);
		System.out.println(result);
		if (result != 0) {
			session.removeAttribute("member");
			session.removeAttribute("order");
			session.removeAttribute("OrderList");
			out.println("<script>");
			out.println("alert('ȸ�� �ֹ� ������ �����Ͽ����ϴ�.');");
			out.println("location.href = '/simple/admin_listorder.do';");
			out.println("</script>");
			out.close();
		}

	}

	@Override
	@RequestMapping(value = "/admin_listorder/orderSearch.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView orderSearch(@RequestParam("search") String search,
			@RequestParam("searchType") String searchType, Criteria cri, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);

		Map<String, Object> orderSearchMap = new HashMap<String, Object>();
		int pageStart = cri.getPageStart();
		int perPageNum = cri.getPerPageNum();
		orderSearchMap.put("pageStart", pageStart);
		orderSearchMap.put("perPageNum", perPageNum);
		orderSearchMap.put("search", search);
		orderSearchMap.put("searchType", searchType);
		System.out.println(searchType);
		orderSearchMap = orderService.orderSearch(orderSearchMap);

		int orderSearchCount = orderService.orderSearchCount(orderSearchMap);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		int pageNum = pageMaker.getCri().getPage();
		orderSearchMap.put("pageNum", pageNum);
		pageMaker.setTotalCount(orderSearchCount);
		mav.addObject("orderSearchMap", orderSearchMap);
		mav.addObject("pageMaker", pageMaker);
		mav.addObject("pageNum", pageNum);

		return mav;

	}

	@RequestMapping(value = "/orderEachGoods.do", method = RequestMethod.POST)
	public ModelAndView orderEachGoods(@ModelAttribute("orderVO") OrderVO _orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();

		Boolean isLogOn = (Boolean) session.getAttribute("isLogOn");
		String action = (String) session.getAttribute("action");
		// �α��� ���� üũ
		// ������ �α��� ������ ���� �ֹ����� ����
		// �α׾ƿ� ������ ��� �α��� ȭ������ �̵�
		if (isLogOn == null || isLogOn == false) {
			session.setAttribute("orderInfo", _orderVO);
			session.setAttribute("action", "/order/orderEachGoods.do");
			return new ModelAndView("redirect:/member/loginForm.do");
		} else {
			if (action != null && action.equals("/order/orderEachGoods.do")) {
				orderVO = (OrderVO) session.getAttribute("orderInfo");
				session.removeAttribute("action");
			} else {
				orderVO = _orderVO;
			}
		}

		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);

		List myOrderList = new ArrayList<OrderVO>();
		myOrderList.add(orderVO);

		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");

		session.setAttribute("myOrderList", myOrderList);
		session.setAttribute("orderer", memberInfo);
		return mav;
	}

	/*
	 * @RequestMapping(value="/orderAllCartGoods.do" ,method = RequestMethod.POST)
	 * public ModelAndView orderAllCartGoods( @RequestParam("cart_goods_qty")
	 * String[] cart_goods_qty, HttpServletRequest request, HttpServletResponse
	 * response) throws Exception{ String
	 * viewName=(String)request.getAttribute("viewName"); ModelAndView mav = new
	 * ModelAndView(viewName); HttpSession session=request.getSession(); Map
	 * cartMap=(Map)session.getAttribute("cartMap"); List myOrderList=new
	 * ArrayList<OrderVO>();
	 * 
	 * List<GoodsVO> myGoodsList=(List<GoodsVO>)cartMap.get("myGoodsList"); MemberVO
	 * memberVO=(MemberVO)session.getAttribute("memberInfo");
	 * 
	 * for(int i=0; i<cart_goods_qty.length;i++){ String[]
	 * cart_goods=cart_goods_qty[i].split(":"); for(int j = 0; j<
	 * myGoodsList.size();j++) { GoodsVO goodsVO = myGoodsList.get(j); int goods_id
	 * = goodsVO.getGoods_id(); if(goods_id==Integer.parseInt(cart_goods[0])) {
	 * OrderVO _orderVO=new OrderVO(); String goods_title=goodsVO.getGoods_title();
	 * int goods_sales_price=goodsVO.getGoods_sales_price(); String
	 * goods_fileName=goodsVO.getGoods_fileName(); _orderVO.setGoods_id(goods_id);
	 * _orderVO.setGoods_title(goods_title);
	 * _orderVO.setGoods_sales_price(goods_sales_price);
	 * _orderVO.setGoods_fileName(goods_fileName);
	 * _orderVO.setOrder_goods_qty(Integer.parseInt(cart_goods[1]));
	 * myOrderList.add(_orderVO); break; } } } session.setAttribute("myOrderList",
	 * myOrderList); session.setAttribute("orderer", memberVO); return mav; }
	 * 
	 * @RequestMapping(value="/payToOrderGoods.do" ,method = RequestMethod.POST)
	 * public ModelAndView payToOrderGoods(@RequestParam Map<String, String>
	 * receiverMap, HttpServletRequest request, HttpServletResponse response) throws
	 * Exception{ String viewName=(String)request.getAttribute("viewName");
	 * ModelAndView mav = new ModelAndView(viewName);
	 * 
	 * HttpSession session=request.getSession(); MemberVO
	 * memberVO=(MemberVO)session.getAttribute("orderer"); String
	 * member_id=memberVO.getMember_id(); String
	 * orderer_name=memberVO.getMember_name(); String orderer_hp =
	 * memberVO.getHp1()+"-"+memberVO.getHp2()+"-"+memberVO.getHp3(); List<OrderVO>
	 * myOrderList=(List<OrderVO>)session.getAttribute("myOrderList");
	 * 
	 * for(int i=0; i<myOrderList.size();i++){ OrderVO
	 * orderVO=(OrderVO)myOrderList.get(i); orderVO.setMember_id(member_id);
	 * orderVO.setOrderer_name(orderer_name);
	 * orderVO.setReceiver_name(receiverMap.get("receiver_name"));
	 * 
	 * orderVO.setReceiver_hp1(receiverMap.get("receiver_hp1"));
	 * orderVO.setReceiver_hp2(receiverMap.get("receiver_hp2"));
	 * orderVO.setReceiver_hp3(receiverMap.get("receiver_hp3"));
	 * orderVO.setReceiver_tel1(receiverMap.get("receiver_tel1"));
	 * orderVO.setReceiver_tel2(receiverMap.get("receiver_tel2"));
	 * orderVO.setReceiver_tel3(receiverMap.get("receiver_tel3"));
	 * 
	 * orderVO.setDelivery_address(receiverMap.get("delivery_address"));
	 * orderVO.setDelivery_message(receiverMap.get("delivery_message"));
	 * orderVO.setDelivery_method(receiverMap.get("delivery_method"));
	 * orderVO.setGift_wrapping(receiverMap.get("gift_wrapping"));
	 * orderVO.setPay_method(receiverMap.get("pay_method"));
	 * orderVO.setCard_com_name(receiverMap.get("card_com_name"));
	 * orderVO.setCard_pay_month(receiverMap.get("card_pay_month"));
	 * orderVO.setPay_orderer_hp_num(receiverMap.get("pay_orderer_hp_num"));
	 * orderVO.setOrderer_hp(orderer_hp); myOrderList.set(i, orderVO); //�� orderVO��
	 * �ֹ��� ������ ������ �� �ٽ� myOrderList�� �����Ѵ�. }//end for
	 * 
	 * orderService.addNewOrder(myOrderList);
	 * mav.addObject("myOrderInfo",receiverMap);//OrderVO�� �ֹ���� �������� �ֹ��� ������ ǥ���Ѵ�.
	 * mav.addObject("myOrderList", myOrderList); return mav; }
	 */

	@RequestMapping(value = "/orderNow.do", method = RequestMethod.POST)
	public ModelAndView orderNow(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		if (session.getAttribute("member") == null) {

			String randomnumber = numberGen(9, 1);
			int orderNum = Integer.parseInt(randomnumber);

			mav.addObject("orderNum", orderNum);
			session.setAttribute("orderNow", true);
			session.setAttribute("nonMemOrder", orderVO);
			session.setAttribute("totalPrice",orderVO.getTotalPrice());
			mav.setViewName("nonorder_01");
		}

		else if (session.getAttribute("member") != null) {

			String randomnumber = numberGen(9, 1);
			int orderNum = Integer.parseInt(randomnumber);

			mav.addObject("orderNum", orderNum);
			session.setAttribute("orderNow", true);
			session.setAttribute("memOrder", orderVO);
			session.setAttribute("totalPrice", orderVO.getTotalPrice());
			mav.setViewName("order_01");
		}
		return mav;
	}

	// ������ ��ȸ�� �ֹ���ȸ
	@Override
	@RequestMapping(value = "/admin_listNoOrder.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView admin_listNoOrder(Criteria cri, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		List<OrderVO> NoOrdersList = orderService.admin_listNoOrder(cri);
		// System.out.println(ordersList);
		int NoOrderCount = orderService.NoOrderCount();
		ModelAndView mav = new ModelAndView(viewName);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(NoOrderCount);
		int pageNum = pageMaker.getCri().getPage();

		mav.addObject("pageNum", pageNum);
		mav.addObject("NoOrdersList", NoOrdersList);
		mav.addObject("pageMaker", pageMaker);

		return mav;
	}

	@Override
	@RequestMapping(value = "/admin_listNoOrder/NoOrderSearch.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView NoOrderSearch(@RequestParam("search") String search,
			@RequestParam("searchType") String searchType, Criteria cri, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView(viewName);

		Map<String, Object> NoOrderSearchMap = new HashMap<String, Object>();
		int pageStart = cri.getPageStart();
		int perPageNum = cri.getPerPageNum();
		NoOrderSearchMap.put("pageStart", pageStart);
		NoOrderSearchMap.put("perPageNum", perPageNum);
		NoOrderSearchMap.put("search", search);
		NoOrderSearchMap.put("searchType", searchType);
		System.out.println(searchType);
		NoOrderSearchMap = orderService.NoOrderSearch(NoOrderSearchMap);

		int NoOrderSearchCount = orderService.NoOrderSearchCount(NoOrderSearchMap);
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		int pageNum = pageMaker.getCri().getPage();
		NoOrderSearchMap.put("pageNum", pageNum);
		pageMaker.setTotalCount(NoOrderSearchCount);
		mav.addObject("NoOrderSearchMap", NoOrderSearchMap);
		mav.addObject("pageMaker", pageMaker);
		mav.addObject("pageNum", pageNum);

		return mav;

	}

	// ������ �ֹ����� �󼼺���
	@RequestMapping(value = "/admin_listNoOrder/detailNonOrder.do", method = RequestMethod.GET)
	public ModelAndView viewNonOrderInfo(@RequestParam("nonMemOrderNum") int nonMemOrderNum, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();

		List<OrderVO> NonOrderList = orderService.NonMemOrderNumList(nonMemOrderNum);
		OrderVO orderVO = orderService.NonMemOrderInfo(nonMemOrderNum);

		session.setAttribute("NonOrder", orderVO);
		session.setAttribute("NonOrderList", NonOrderList);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("NonOrderList", NonOrderList);

		mav.addObject("order", orderVO);
		return mav;
	}

	// ������ �ֹ����� ����ȭ��
	@RequestMapping(value = "/admin_listNoOrder/admin_ModVeiwNonMemorder.do", method = RequestMethod.GET)
	public ModelAndView admin_ModVeiwNonMemorder(@RequestParam("nonMemOrderNum") int nonMemOrderNum,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();

		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);

		return mav;
	}

	// ������ �ֹ����� ����
	@RequestMapping(value = "/admin_listNoOrder/admin_ModNonMemorder.do", method = RequestMethod.POST)
	private void admin_ModNonMemorder(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		int result = 0;
		result = orderService.adminModNonMemOrder(orderVO);
		System.out.println(result);
		if (result != 0) {
			session.removeAttribute("NonOrder");
			session.removeAttribute("NonOrderList");
			out.println("<script>");
			out.println("alert('��ȸ�� �ֹ� ������ �����Ͽ����ϴ�.');");
			out.println("location.href = '/simple/admin_listNoOrder.do';");
			out.println("</script>");
			out.close();
		}

	}
	
	@RequestMapping(value = "/admin_listorder/deliveryModify.do", method = RequestMethod.POST)
	public @ResponseBody void deliveryModify(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		int result = 0;
		result = orderService.deliveryModify(orderVO);
		if(result != 0) {
			out.print("ȸ�� �ֹ� ��ۻ��¸� �����Ͽ����ϴ�.");
			out.close();
		}

		
	}
	@RequestMapping(value = "/admin_listNoOrder/deliveryModifyNon.do", method = RequestMethod.POST)
	public @ResponseBody void deliveryModifyNon(@ModelAttribute("orderVO") OrderVO orderVO, HttpServletRequest request, HttpServletResponse response ) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		int result = 0;
		result = orderService.deliveryModifyNon(orderVO);
		if(result != 0) {
			out.print("��ȸ�� �ֹ� ��ۻ��¸� �����Ͽ����ϴ�.");
			out.close();
		}

		
	}
}
