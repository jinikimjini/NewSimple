package com.project.simple.order.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.project.simple.member.vo.MemberVO;
import com.project.simple.order.vo.OrderVO;
import com.project.simple.page.Criteria;

public interface OrderDAO {
	public List<OrderVO> selectAllOrderList(Criteria cri) throws DataAccessException;
	public List<OrderVO> orderSearchList(Map<String, Object> orderSearchMap) throws DataAccessException;
	public int orderSearchCount(Map<String, Object> search) throws DataAccessException;
	public int selectOrderCount() throws DataAccessException;
	public List<OrderVO> listMyOrderGoods(OrderVO orderBean) throws DataAccessException;
	public void insertNewOrder(OrderVO ordervo) throws DataAccessException;
	public OrderVO findMyOrder(String order_id) throws DataAccessException;
	public void removeGoodsFromCart(List<OrderVO> myOrderList)throws DataAccessException;
	public OrderVO selectcartlist(String memCartId) throws DataAccessException;
	public List<OrderVO> selectmemOrderList(int memOrderNum) throws DataAccessException;
	public MemberVO selectmemberOrderId(int memOrderNum) throws DataAccessException;
	public OrderVO selectmemberOrderInfo(int memOrderNum) throws DataAccessException;
	public int updateAdminModMemOrder(OrderVO orderVO)throws DataAccessException;
	
	public List<OrderVO> selectAllNoOrdersList(Criteria cri) throws DataAccessException;
	public int selectNoOrderCount() throws DataAccessException;
	public List<OrderVO> NoOrderSearchList(Map<String, Object> noOrderSearchMap) throws DataAccessException;
	public int NoOrderSearchCount(Map<String, Object> search) throws DataAccessException;
	
	public List<OrderVO> selectNonMemOrderList(int nonMemOrderNum) throws DataAccessException;
	public OrderVO selectNonMemberOrderInfo(int nonMemOrderNum) throws DataAccessException;
	public int updateAdminModNonMemOrder(OrderVO orderVO)throws DataAccessException;
	
	public int updateAdmindeliveryModify(OrderVO orderVO) throws DataAccessException;
	public int updateAdmindeliveryModifyNon(OrderVO orderVO) throws DataAccessException;
	
	public List<Integer> selectSeqNum(int orderNum) throws DataAccessException;
}
