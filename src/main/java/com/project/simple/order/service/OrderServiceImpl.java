package com.project.simple.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


import com.project.simple.cart.vo.CartVO;
import com.project.simple.member.vo.MemberVO;
import com.project.simple.mypage.vo.MypageVO;
import com.project.simple.order.dao.OrderDAO;
import com.project.simple.order.vo.OrderVO;
import com.project.simple.page.Criteria;


@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDAO orderDAO;
	
	@Override
	public List<OrderVO> listOrders(Criteria cri) throws Exception{
		List<OrderVO> ordersList = orderDAO.selectAllOrderList(cri);
		return ordersList;
	}
	
	@Override
	public int orderCount() throws Exception {
		int orderCount = orderDAO.selectOrderCount();
		return orderCount;
	}
	
	@Override
	public Map<String, Object> orderSearch(Map<String, Object> orderSearchMap) throws Exception {
		List<OrderVO> orderSearchList=orderDAO.orderSearchList(orderSearchMap);

		orderSearchMap.put("orderSearchList", orderSearchList);
		

		return orderSearchMap;
	}
	
	@Override
	public int orderSearchCount(Map<String, Object> search) throws Exception {
		int orderSearchCount = orderDAO.orderSearchCount(search);
		return orderSearchCount;
	}
	
	public List<Integer> selectSeqNum(int orderNum) throws Exception{
		List<Integer> list = orderDAO.selectSeqNum(orderNum);
		return list;
	}
	
	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception{
		List<OrderVO> orderGoodsList;
		orderGoodsList=orderDAO.listMyOrderGoods(orderVO);
		return orderGoodsList;
	}
	
	public void addNewOrder(OrderVO orderVO) throws Exception{
		orderDAO.insertNewOrder(orderVO);	
	}	
	
	public OrderVO selectcartlist(String memCartId) throws Exception {
		return orderDAO.selectcartlist(memCartId);
	}
	
	public OrderVO findMyOrder(String order_id) throws Exception{
		return orderDAO.findMyOrder(order_id);
	}

	@Override
	public List<OrderVO> memOrderNumList(int memOrderNum) throws Exception {
		List<OrderVO> orderVO = orderDAO.selectmemOrderList(memOrderNum);
		return orderVO;
	}

	@Override
	public MemberVO memOrderId(int memOrderNum) throws Exception {
		MemberVO memberVO = orderDAO.selectmemberOrderId(memOrderNum);
		return memberVO;
	}

	@Override
	public OrderVO memOrderInfo(int memOrderNum) throws Exception {
		OrderVO orderVO = orderDAO.selectmemberOrderInfo(memOrderNum);
		return orderVO;
	}
	
	@Override
	public int adminModMemOrder(OrderVO orderVO) throws Exception {
		return orderDAO.updateAdminModMemOrder(orderVO);
		
	}
	
	//������ ��ȸ�� �ֹ� ����Ʈ
	@Override
	public List<OrderVO> admin_listNoOrder(Criteria cri) throws Exception {
		List<OrderVO> NoOrdersList = orderDAO.selectAllNoOrdersList(cri);
		return NoOrdersList;
	}

	@Override
	public int NoOrderCount() throws Exception {
		int NoOrderCount = orderDAO.selectNoOrderCount();
		return NoOrderCount;
	}

	@Override
	public Map<String, Object> NoOrderSearch(Map<String, Object> NoOrderSearchMap) throws Exception {
		List<OrderVO> NoOrderSearchList=orderDAO.NoOrderSearchList(NoOrderSearchMap);
		NoOrderSearchMap.put("NoOrderSearchList", NoOrderSearchList);

		return NoOrderSearchMap;
	}

	@Override
	public int NoOrderSearchCount(Map<String, Object> search) throws Exception {
		int NoOrderSearchCount = orderDAO.NoOrderSearchCount(search);
		return NoOrderSearchCount;
	}

	@Override
	public List<OrderVO> NonMemOrderNumList(int nonMemOrderNum) throws Exception {
		List<OrderVO> orderVO = orderDAO.selectNonMemOrderList(nonMemOrderNum);
		return orderVO;
	}

	@Override
	public OrderVO NonMemOrderInfo(int nonMemOrderNum) throws Exception {
		OrderVO orderVO = orderDAO.selectNonMemberOrderInfo(nonMemOrderNum);
		return orderVO;
	}

	@Override
	public int adminModNonMemOrder(OrderVO orderVO) throws Exception {
		return orderDAO.updateAdminModNonMemOrder(orderVO);
	}

	@Override
	public int deliveryModify(OrderVO orderVO) throws Exception {
		return orderDAO.updateAdmindeliveryModify(orderVO);
	}

	@Override
	public int deliveryModifyNon(OrderVO orderVO) throws Exception {
		return orderDAO.updateAdmindeliveryModifyNon(orderVO);
	}

}
	

