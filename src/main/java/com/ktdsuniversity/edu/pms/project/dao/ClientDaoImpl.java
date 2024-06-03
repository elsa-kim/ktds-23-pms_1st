package com.ktdsuniversity.edu.pms.project.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDaoImpl extends SqlSessionDaoSupport  implements ClientDao {
	
	 	@Autowired
	    @Override
	    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
	        super.setSqlSessionTemplate(sqlSessionTemplate);
	    }

}
