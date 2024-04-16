package com.ktdsuniversity.edu.pms.product.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ktdsuniversity.edu.pms.product.vo.ProductVO;

@Repository
public class ProductDaoImpl extends SqlSessionDaoSupport implements ProductDao{
	
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	@Override
	public List<ProductVO> getAllProduct(ProductVO productVO) {
		return getSqlSession().selectList(ProductDao.NAME_SPACE + ".getAllProduct", productVO);
	}

	@Override
	public int getProductAllCount(ProductVO productVO) {
		return getSqlSession().selectOne(ProductDao.NAME_SPACE + ".getProductAllCount", productVO);
	}

	@Override
	public int insertNewProduct(ProductVO productVO) {
		return getSqlSession().insert(ProductDao.NAME_SPACE+".insertNewProduct", productVO);
	}

	@Override
	public ProductVO selectOneProduct(String id) {
		return getSqlSession().selectOne(ProductDao.NAME_SPACE+".selectOneProduct", id);
	}
}
