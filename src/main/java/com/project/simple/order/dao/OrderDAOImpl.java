package com.project.simple.order.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.project.simple.member.vo.MemberVO;
import com.project.simple.order.vo.OrderVO;
import com.project.simple.page.Criteria;

@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<OrderVO> selectAllOrderList(Criteria cri) throws DataAccessException {
		List<OrderVO> ordersList = sqlSession.selectList("mapper.order.selectAllOrderList", cri);
		return ordersList;
	}

	@Override
	public int orderSearchCount(Map<String, Object> search) throws DataAccessException {
		int orderSearchCount = sqlSession.selectOne("mapper.order.orderSearchCount", search);

		return orderSearchCount;
	}

	@Override
	public List<OrderVO> orderSearchList(Map<String, Object> orderSearchMap) throws DataAccessException {
		List<OrderVO> orderSearchList = sqlSession.selectList("mapper.order.orderSearchList", orderSearchMap);
		return orderSearchList;
	}

	@Override
	public int selectOrderCount() throws DataAccessException {
		int orderCount = sqlSession.selectOne("mapper.order.selectOrderCount");

		return orderCount;
	}

	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws DataAccessException {
		List<OrderVO> orderGoodsList = new ArrayList<OrderVO>();
		orderGoodsList = (ArrayList) sqlSession.selectList("mapper.order.selectMyOrderList", orderVO);
		return orderGoodsList;
	}

	public void insertNewOrder(OrderVO ordervo) throws DataAccessException {
		sqlSession.insert("mapper.order.insertNewOrder", ordervo);
		sqlSession.insert("mapper.order.insertNewOrderdelivery", ordervo);
	}

	public List<Integer> selectSeqNum(int orderNum) throws DataAccessException {
		List<Integer> list = sqlSession.selectList("mapper.order.selectSeqNum", orderNum);
		return list;
	}

	public OrderVO selectcartlist(String memCartId) throws DataAccessException {
		OrderVO vo = sqlSession.selectOne("mapper.order.selectcartlist", memCartId);
		return vo;
	}

	public OrderVO findMyOrder(String order_id) throws DataAccessException {
		OrderVO orderVO = (OrderVO) sqlSession.selectOne("mapper.order.selectMyOrder", order_id);
		return orderVO;
	}

	public void removeGoodsFromCart(OrderVO orderVO) throws DataAccessException {
		sqlSession.delete("mapper.order.deleteGoodsFromCart", orderVO);
	}

	public void removeGoodsFromCart(List<OrderVO> myOrderList) throws DataAccessException {
		for (int i = 0; i < myOrderList.size(); i++) {
			OrderVO orderVO = (OrderVO) myOrderList.get(i);
			sqlSession.delete("mapper.order.deleteGoodsFromCart", orderVO);
		}
	}

	private int selectOrderID() throws DataAccessException {
		return sqlSession.selectOne("mapper.order.selectOrderID");

	}

	@Override
	public List<OrderVO> selectmemOrderList(int memOrderNum) throws DataAccessException {
		return sqlSession.selectList("mapper.order.selectmemOrderList", memOrderNum);
	}

	@Override
	public MemberVO selectmemberOrderId(int memOrderNum) throws DataAccessException {

		return sqlSession.selectOne("mapper.order.selectmemberOrderId", memOrderNum);

	}

	@Override
	public OrderVO selectmemberOrderInfo(int memOrderNum) throws DataAccessException {
		return sqlSession.selectOne("mapper.order.selectmemberOrderInfo", memOrderNum);
	}

	@Override
	public int updateAdminModMemOrder(OrderVO orderVO) throws DataAccessException {
		int result = sqlSession.update("mapper.order.updateAdminModMemOrder", orderVO);
		return result;
	}

	// ������ ��ȸ�� �ֹ� ����Ʈ
	@Override
	public List<OrderVO> selectAllNoOrdersList(Criteria cri) throws DataAccessException {
		List<OrderVO> NoOrdersList = sqlSession.selectList("mapper.order.selectAllNoOrdersList", cri);
		return NoOrdersList;
	}

	@Override
	public int selectNoOrderCount() throws DataAccessException {
		int NoOrderCount = sqlSession.selectOne("mapper.order.selectNoOrderCount");
		return NoOrderCount;
	}

	@Override
	public List<OrderVO> NoOrderSearchList(Map<String, Object> noOrderSearchMap) throws DataAccessException {
		List<OrderVO> NoOrderSearchList = sqlSession.selectList("mapper.order.NoOrderSearchList", noOrderSearchMap);
		return NoOrderSearchList;
	}

	@Override
	public int NoOrderSearchCount(Map<String, Object> search) throws DataAccessException {
		int NoOrderSearchCount = sqlSession.selectOne("mapper.order.NoOrderSearchCount", search);

		return NoOrderSearchCount;
	}

	@Override
	public List<OrderVO> selectNonMemOrderList(int nonMemOrderNum) throws DataAccessException {
		return sqlSession.selectList("mapper.order.selectNonMemOrderList", nonMemOrderNum);
	}

	@Override
	public OrderVO selectNonMemberOrderInfo(int nonMemOrderNum) throws DataAccessException {
		return sqlSession.selectOne("mapper.order.selectNonMemberOrderInfo", nonMemOrderNum);
	}

	@Override
	public int updateAdminModNonMemOrder(OrderVO orderVO) throws DataAccessException {
		int result = sqlSession.update("mapper.order.updateAdminModNonMemOrder", orderVO);
		return result;
	}

	@Override
	public int updateAdmindeliveryModify(OrderVO orderVO) throws DataAccessException {
		int result = sqlSession.update("mapper.order.updateAdmindeliveryModify", orderVO);
		return result;
	}

	@Override
	public int updateAdmindeliveryModifyNon(OrderVO orderVO) throws DataAccessException {
		int result = sqlSession.update("mapper.order.updateAdmindeliveryModifyNon", orderVO);
		return result;
	}
}
