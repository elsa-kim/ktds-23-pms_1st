package com.ktdsuniversity.edu.pms.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktdsuniversity.edu.pms.login.dao.CommuteDao;
import com.ktdsuniversity.edu.pms.login.vo.CommuteListVO;

@Service
public class CommuteServiceImpl implements CommuteService {

	@Autowired
	private CommuteDao commuteDao;

	@Override
	public CommuteListVO getAllCommuteData() {

		CommuteListVO commuteListVO = new CommuteListVO();
		commuteListVO.setCommuteList(commuteDao.getAllCommuteData());
		return commuteListVO;
	}

	@Override
	public CommuteListVO getAllCommuteDataByEmpId(String empId) {
		CommuteListVO commuteListVO = new CommuteListVO();
		commuteListVO.setCommuteList(commuteDao.getAllCommuteDataByEmpId(empId));
		return commuteListVO;
	}

}
