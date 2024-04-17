package com.ktdsuniversity.edu.pms.approval.dao;

import java.util.List;

import com.ktdsuniversity.edu.pms.approval.vo.ApprovalDetailVO;

public interface ApprovalDetailDao {
	
	public String NAME_SPACE = "com.ktdsuniversity.edu.pms.approval.dao.ApprovalDetailDao";
	
	/**
     * DB에 저장된 모든 게시글의 목록을 조회
     * @return
     */
    public List<ApprovalDetailVO> getAllApprovalDetail();
    
    /**
     * 비품 번경시 새로운 물품 추가 및 삭제를 진행한다
     */
    public int insertApproval(ApprovalDetailVO approvaldetailVo);
    
    /**
     * 신청 비품 정보를 가지고 온다
     */
    public List<ApprovalDetailVO> getpersonApproval(String id);
    
    
    
    
}
