package com.ktdsuniversity.edu.pms.product.service;

import java.util.List;

import com.ktdsuniversity.edu.pms.product.vo.ProductListVO;
import com.ktdsuniversity.edu.pms.product.vo.ProductManagementVO;
import com.ktdsuniversity.edu.pms.product.vo.ProductVO;

public interface ProductService {

	/**
	 * 비픔 목록과 비품 수를 모두 조회
	 * @return
	 */
	public ProductListVO getAllProduct(ProductVO productVO);

	public boolean createNewProduct(ProductVO productVO);

	public ProductVO getOneProduct(String id);

	public boolean updateOneProduct(String prdtId);

	public boolean addProductCount(ProductManagementVO productManagementVO);

	public boolean deleteOneProduct(String prdtId);

	public boolean modifyProduct(ProductVO productVO);
	
	/**
	 * 모든 비품 목록을 중복없이 조회
	 */
	public ProductListVO getAllProductCategory();

	
	/**
	 * 선택된 비품명으로 해당 비품의 재고수를 얻기 위함
	 * @param prdtName 선택된 비품명
	 * @return 선택된 비품의 정보들
	 */
	public ProductVO getOneSelectedProduct(String prdtName);

	/**
	 * 여러 개의 비품 일괄등록 
	 * @param addItems
	 * @return
	 */
	public boolean createManyProduct(List<Integer> addItems);
	

}
