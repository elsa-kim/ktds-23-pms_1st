package com.ktdsuniversity.edu.pms.review.dao;

import java.util.List;

import com.ktdsuniversity.edu.pms.project.vo.ProjectVO;
import com.ktdsuniversity.edu.pms.review.vo.ReviewVO;

public interface ReviewDao {
	
	public String NAME_SPACE = "com.ktdsuniversity.edu.pms.review.dao.ReviewDao";

	public List<ReviewVO> selectAllReview();
		
	public List<ReviewVO> viewReviewCntnt();

	/**
	 * 후기 작성 요청 전송(PM)
	 * @param reviewVO
	 * @return 전송된 후기 작성 요청의 건수
	 */
	public int insertNewReviewQuestion(ReviewVO reviewVO);
	
	public ReviewVO getOneReview(String rvId);
	
	/**
	 * PM, 팀원 후기 작성
	 * @param reviewVO
	 * @return 작성된 후기의 개수
	 */
	public int insertNewReview(ReviewVO reviewVO);
	
	/**
	 * PM, 팀원 후기 수정
	 * @param reviewVO
	 * @return 수정한 개수
	 */
	public int updateOneReview(ReviewVO reviewVO);
	
	/**
	 * PM이상이 후기를 삭제
	 * @param reviewVO
	 * @return 삭제한 개수
	 */
//	public int deleteOneReview(String rvId);

	/**
	 * 후기 삭제
	 * @param id 후기id
	 * @return 삭제 count
	 */
	public int deleteReviewViewResult(String id);
	
}
