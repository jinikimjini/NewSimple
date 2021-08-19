package com.project.simple.admin.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.project.simple.admin.vo.AdminVO;
import com.project.simple.board.vo.ArticleVO;
import com.project.simple.member.vo.MemberVO;
import com.project.simple.mypage.vo.MypageVO;
import com.project.simple.order.vo.OrderVO;
import com.project.simple.page.Criteria;
import com.project.simple.product.vo.ProductVO;

@Repository("adminDAO")
public class AdminDAOImpl implements AdminDAO {

	@Autowired
	private SqlSession sqlSession;

	// �α��� ��� ���� �߰�
	public AdminVO adminloginById(AdminVO adminVO) throws DataAccessException {
		AdminVO vo = sqlSession.selectOne("mapper.admin.adminloginById", adminVO);
		return vo;
	}

	// DB���� ��� ���̵� �ش��ϴ� ���� ��ȸ
	@Override
	public MemberVO selectMember(String memId) throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.selectMember", memId);
	}

	// Inquiry ��ü ��� ��ȸ
	@Override
	public List<ArticleVO> selectAllInquiryList(Criteria cri) throws DataAccessException {
		List<ArticleVO> inquiryList = sqlSession.selectList("mapper.admin.selectAllInquiryList", cri);
		return inquiryList;
	}

	@Override
	public int selectInquiryCount() throws DataAccessException {
		int inquiryCount = sqlSession.selectOne("mapper.admin.selectInquiryCount");

		return inquiryCount;
	}
	
	@Override
	public int insertNewNotice(Map noticeMap) throws DataAccessException {
		int noticeNum = selectNewNoticeNum();
		noticeMap.put("noticeNum", noticeNum);
		sqlSession.insert("mapper.admin.insertNewNotice", noticeMap);
		return noticeNum;
	}
	
	private int selectNewNoticeNum() throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.selectNewNoticeNum");
		
	}
	
	// notice �����ϱ���
	@Override
	public ArticleVO selectNotice(int noticeNum) throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.selectNotice", noticeNum);
	}
	
	//notice �����ϱ�
	@Override
	public void updateNotice(Map noticeMap) throws DataAccessException {
		sqlSession.update("mapper.admin.updateNotice", noticeMap);
	}
	
	//�������� �����ϱ�
	@Override
	public void deleteNotice(int noticeNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteNotice", noticeNum);
	}
	
	//���ֹ��� ���� �۾���
	@Override
	public void insertNewQuestion(ArticleVO question) throws DataAccessException {
		sqlSession.insert("mapper.admin.insertNewQuestion", question);

	}
	
	// ���ֹ��� ���� �����ϱ���
	@Override
	public ArticleVO selectQuestion(int questionNum) throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.selectQuestion", questionNum);
	}
	
	//���ֹ��� ���� �����ϱ�
	@Override
	public void updateQuestion(ArticleVO question) throws DataAccessException {
		sqlSession.update("mapper.admin.updateQuestion", question);
	}
	
	//���ֹ��� ���� �����ϱ�
	@Override
	public void deleteQuestion(int questionNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteQuestion",questionNum);
	}
	
	//1:1���� �亯 ��� ��
	@Override
	public ArticleVO selectInquiryAnswer(int inquiryNum) throws DataAccessException {
		return sqlSession.selectOne("mapper.admin.selectInquiryAnswer", inquiryNum);
	}
	
	//1:1���� �亯 ���
	@Override
	public void insertNewInquiryAnswer(ArticleVO inquiry) throws DataAccessException {
		sqlSession.update("mapper.admin.insertNewInquiryAnswer", inquiry);

	}
	
	//1:1���� �亯 �����ϱ�
	@Override
	public void deleteInquiryAnswer(int inquiryNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteInquiryAnswer", inquiryNum);
	}
	
	//asCenter �����Ϸ�
	@Override
	public void updateAsCenterConfirm(int asCenterNum) throws DataAccessException {
		sqlSession.update("mapper.admin.updateAsCenterConfirm", asCenterNum);
		System.out.println(asCenterNum);
	}


	@Override
	public int updateAdminMember(MemberVO modmember) throws DataAccessException {
		int result = sqlSession.update("mapper.admin.updateAdminMember", modmember);
		return result;
	}

	@Override
	public void deleteSelectRemoveMember(String memId) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteSelectRemoveMember",memId);
		
	}

	@Override
	public void deleteSelectRemoveMemOrder(String memOrderNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteSelectRemoveMemOrder",memOrderNum);
		
	}

	@Override
	public void deleteSelectRemoveNonMemOrder(String nonMemOrderNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteSelectRemoveNonMemOrder",nonMemOrderNum);
		
	}
	
	
	//��ǰ ���� ��ȸ
	@Override
	public List<ProductVO> listProductQuestion(Criteria cri) throws DataAccessException {
		List<ProductVO> listProductQuestion = sqlSession.selectList("mapper.admin.selectProductQuestionList", cri);
		return listProductQuestion;
	}
	
	@Override
	public int productQuestionCount() throws DataAccessException {
		int productQuestionCount = sqlSession.selectOne("mapper.admin.selectProductQuestionCount");

		return productQuestionCount;
	}
	
	//��ǰ���� �亯 ���
	@Override
	public void addProductQuestion(ProductVO productQuestion) throws DataAccessException {
		sqlSession.update("mapper.admin.insertNewProductQuestion", productQuestion);		

	}
	
	//��ǰ���� �亯 ����
	@Override
	public void modNewProductAnswer(ProductVO productQuestion) throws DataAccessException {
		sqlSession.update("mapper.admin.updateNewProductQuestion", productQuestion);		

	}
	
	//��ǰ���� �����ϱ�
	@Override
	public void deleteProductQuestion(int productQuestionNum) throws DataAccessException {
		sqlSession.delete("mapper.admin.deleteProductQuestion", productQuestionNum);
	}
	
	//��ǰ���� �����ϱ�
	@Override
	public void deleteProductAnswer(int productQuestionNum) throws DataAccessException {
		sqlSession.update("mapper.admin.deleteProductAnswer", productQuestionNum);
	}
	
	//��ǰ���� �˻�
	@Override
	public List<ProductVO> questionSearchList(Map<String, Object> questionSearchMap) throws DataAccessException {
		List<ProductVO> questionSearchList =sqlSession.selectList("mapper.admin.questionSearchList",questionSearchMap);		
		return questionSearchList;
	}
	
	@Override
	public int questionSearchCount(Map<String, Object> search) throws DataAccessException {
		int questionSearchCount = sqlSession.selectOne("mapper.admin.questionSearchCount",search);
		return questionSearchCount;
	}
	

}
