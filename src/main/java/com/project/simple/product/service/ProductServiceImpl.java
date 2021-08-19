package com.project.simple.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.project.simple.product.vo.OptionVO;
import com.project.simple.product.vo.ProductVO;
import com.project.simple.favorite.vo.FavoriteVO;
import com.project.simple.product.dao.ProductDAO;
import com.project.simple.product.page.Criteria1;

@Service("productService")
@Transactional(propagation = Propagation.REQUIRED)
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductDAO productDAO;

	@Override
	public List<ProductVO> listProduct(Map<String, Object> ProductMap) throws Exception {
		List<ProductVO> productList = productDAO.selectAllProductList(ProductMap);
		return productList;
	}

	@Override
	public List<ProductVO> admin_listProduct(Criteria1 cri) throws DataAccessException {
		List<ProductVO> productList = productDAO.admin_selectAllProductList(cri);
		return productList;
	}

	@Override
	public int addProduct(Map productMap) throws DataAccessException {
		return productDAO.insertProduct(productMap);
	}

	@Override
	public void removeProduct(String productNum) throws DataAccessException {
		productDAO.deleteProduct(productNum);
	}

	@Override
	public ProductVO viewProduct(String productNum) throws Exception {
		ProductVO productVO = productDAO.selectProduct(productNum);
		return productVO;
	}

	@Override
	public List<String> keywordSearch(String keyword) throws Exception {
		List<String> list = productDAO.selectKeywordSearch(keyword);
		return list;
	}

	@Override
	public List<ProductVO> searchProduct(String searchWord) throws Exception {
		List productList = productDAO.selectProductBySearchWord(searchWord);
		return productList;
	}

	@Override
	public ProductVO admin_detailproduct(String productNum) throws Exception {
		ProductVO productVO = productDAO.selectProduct(productNum);
		return productVO;
	}

	@Override
	public void modProduct(Map productMap) throws Exception {

		productDAO.updateProduct(productMap);

	}

	@Override
	public ProductVO productForm(String productNum) throws Exception {
		ProductVO productVO = productDAO.selectProduct(productNum);
		return productVO;

	}

	public List<ProductVO> listProductReview(Map<String, Object> productMap) throws Exception {
		List<ProductVO> productReviewList = productDAO.selectAllProductReviewList(productMap);

		return productReviewList;
	}

	public int productReviewCount(String productNum) throws Exception {
		int productReviewCount = productDAO.selectProductReviewCount(productNum);
		return productReviewCount;
	}

	public List<ProductVO> listProductQuestion(Map<String, Object> productMap) throws Exception {
		List<ProductVO> productQuestionList = productDAO.selectAllProductQuestionList(productMap);

		return productQuestionList;
	}

	public int productQuestionCount(String productNum) throws Exception {
		int productQuestionCount = productDAO.selectProductQuestionCount(productNum);
		return productQuestionCount;
	}

	@Override
	public int productCount() throws Exception {
		int productCount = productDAO.selectProductCount();
		return productCount;
	}

	@Override
	public Map<String, Object> productSearch(Map<String, Object> productSearchMap) throws Exception {
		List<ProductVO> productSearchList = productDAO.productSearchList(productSearchMap);

		productSearchMap.put("productSearchList", productSearchList);

		return productSearchMap;
	}

	@Override
	public int productSearchCount(Map<String, Object> search) throws Exception {
		int productSearchCount = productDAO.productSearchCount(search);
		return productSearchCount;
	}

	@Override
	public Map<String, Object> viewOptionvalue(String productNum) throws Exception {
		Map<String, Object> option = productDAO.selectOptionvalue(productNum);

		return option;
	}

	@Override
	public Map<String, List> BestProductList() throws Exception {
		Map<String, List> BestProductMap = new HashMap<String, List>();
		List<ProductVO> BestProductList = productDAO.selectBestList();
		List<ProductVO> myProductList = productDAO.selectProductList(BestProductList);
		BestProductMap.put("BestProductList", BestProductList);
		BestProductMap.put("myProductList", myProductList);
		return BestProductMap;
	}

	// ��ǰ�������� ��ǰ���� �۾���
	@Override
	public void addNewQuestion(ProductVO question) throws Exception {
		productDAO.insertNewQuestion(question);
	}

	@Override
	public void removeQuestion(int productQuestionNum) throws Exception {
		productDAO.deleteQuestion(productQuestionNum);
	}

	@Override
	public void modQuestion(ProductVO question) throws Exception {
		productDAO.updateQuestion(question);
	}

	// ��ǰ��� �ɼ��̸� ����
	@Override
	public List<String> selectOptionName() throws Exception {
		List<String> optionName = productDAO.selectOptionName();

		return optionName;
	}

	// ��ǰ��� �ɼ�1�� ����
	@Override
	public Map<String,Object> selectOptionValue(Map<String,Object> optionValue) throws Exception {
		List<String> option = productDAO.selectOptionValue(optionValue);

		if(option.size()>1) {
		for(int i=0; i<option.size();i++) {
			optionValue.put(option.get(i), option.get(i));
		}
		}else {
			optionValue.put(option.get(0),option.get(0));
		}
		return optionValue;
	}
	
	// ��ǰ��� �ɼǵ��
	@Override
	public List<OptionVO> addNewOption(OptionVO option) throws Exception {
		List<OptionVO> optionList = productDAO.addNewOption(option);

		return optionList;
	}
	
	// ��ǰ��� �ɼǼ��û���
	@Override
	public List<OptionVO> removeSelectOption(OptionVO option) throws Exception {
		List<OptionVO> optionList = productDAO.removeSelectOption(option);

		return optionList;
	}

	@Override
	public String addProductNum() throws Exception {
		String productNum = productDAO.addProductNum();
		return productNum;
	}
	
	@Override
	public List<OptionVO> selectOptionList(String productNum) throws Exception {
		List<OptionVO> selectOptionList = productDAO.selectOptionList(productNum);
		return selectOptionList;
	}

}
