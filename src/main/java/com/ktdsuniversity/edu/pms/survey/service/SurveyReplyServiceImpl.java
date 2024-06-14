package com.ktdsuniversity.edu.pms.survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktdsuniversity.edu.pms.project.dao.ProjectDao;
import com.ktdsuniversity.edu.pms.survey.dao.SurveyQuestionDao;
import com.ktdsuniversity.edu.pms.survey.dao.SurveyQuestionPickDao;
import com.ktdsuniversity.edu.pms.survey.dao.SurveyReplyDao;
import com.ktdsuniversity.edu.pms.survey.vo.SurveyListVO;
import com.ktdsuniversity.edu.pms.survey.vo.SurveyReplyVO;

@Service
public class SurveyReplyServiceImpl implements SurveyReplyService {

	@Autowired
	private SurveyReplyDao surveyReplyDao;
	@Autowired
	private SurveyQuestionPickDao surveyQuestionPickDao;
	@Autowired
	private ProjectDao projectDao;

	@Transactional
	@Override
	public boolean responseSurvey(SurveyReplyVO surveyReplyVO) {
		int insertedCount = this.surveyReplyDao.insertSurveyAnswer(surveyReplyVO);
		System.out.println("작성자" + surveyReplyVO.getCrtrId());
		insertedCount = this.projectDao.updateOneTeammateSurveySts(surveyReplyVO);
		// insertedCount = this.surveyQuestionPickDao.updateAllSqpCountPlusOneByReply(surveyReplyVO);		
		// surveyReplyVO.getCrtrId(), surveyReplyVO.getSurveyQuestionVO().getPrjId()
		return insertedCount > 0;
	}

	@Override
	public SurveyListVO getAllReplies(SurveyReplyVO surveyReplyVO) {
		List<SurveyReplyVO> replyList = this.surveyReplyDao.getAllReplies(surveyReplyVO);

		SurveyListVO surveyListVO = new SurveyListVO();
		surveyListVO.setReplyList(replyList);

		return surveyListVO;
	}

	@Override
	public List<String> getDoneEmpIdList(String prjId) {
		return this.surveyReplyDao.getDoneEmpIdList(prjId);
	}

}
