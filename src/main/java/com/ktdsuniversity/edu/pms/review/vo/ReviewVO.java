package com.ktdsuniversity.edu.pms.review.vo;

import com.ktdsuniversity.edu.pms.project.vo.ProjectVO;

public class ReviewVO {

	private String rvId;
	private String rvCntnt;
	private String prjId;
	private String crtDt;
	private String crtrId;
	private String mdfDt;
	private String mdfrId;
	private String delYn;
		
	private ProjectVO projectVO;
	
	
	public ProjectVO getProjectVO() {
		return projectVO;
	}
	public void setProjectVO(ProjectVO projectVO) {
		this.projectVO = projectVO;
	}
	public String getRvId() {
		return rvId;
	}
	public void setRvId(String rvId) {
		this.rvId = rvId;
	}
	public String getRvCntnt() {
		return rvCntnt;
	}
	public void setRvCntnt(String rvCntnt) {
		this.rvCntnt = rvCntnt;
	}
	public String getPrjId() {
		return prjId;
	}
	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}
	public String getCrtDt() {
		return crtDt;
	}
	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}
	public String getCrtrId() {
		return crtrId;
	}
	public void setCrtrId(String crtrId) {
		this.crtrId = crtrId;
	}
	public String getMdfDt() {
		return mdfDt;
	}
	public void setMdfDt(String mdfDt) {
		this.mdfDt = mdfDt;
	}
	public String getMdfrId() {
		return mdfrId;
	}
	public void setMdfrId(String mdfrId) {
		this.mdfrId = mdfrId;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	
	
	
}
