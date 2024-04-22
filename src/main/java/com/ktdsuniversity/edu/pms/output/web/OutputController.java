package com.ktdsuniversity.edu.pms.output.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.ktdsuniversity.edu.pms.commoncode.service.CommonCodeService;
import com.ktdsuniversity.edu.pms.commoncode.vo.CommonCodeVO;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeVO;
import com.ktdsuniversity.edu.pms.exceptions.PageNotFoundException;
import com.ktdsuniversity.edu.pms.output.service.OutputService;
import com.ktdsuniversity.edu.pms.output.vo.OutputListVO;
import com.ktdsuniversity.edu.pms.output.vo.OutputSearchVO;
import com.ktdsuniversity.edu.pms.output.vo.OutputVO;
import com.ktdsuniversity.edu.pms.project.dao.ProjectDao;
import com.ktdsuniversity.edu.pms.project.service.ProjectService;
import com.ktdsuniversity.edu.pms.project.vo.ProjectListVO;
import com.ktdsuniversity.edu.pms.project.vo.ProjectTeammateVO;
import com.ktdsuniversity.edu.pms.utils.AjaxResponse;
import com.ktdsuniversity.edu.pms.utils.Validator;
import com.ktdsuniversity.edu.pms.utils.Validator.Type;


@Controller
public class OutputController {
	@Autowired
	private OutputService outputService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private CommonCodeService commonCodeService;
	@Autowired
	private ProjectDao projectDao;

	@GetMapping("/output")
	public String viewOutputList() {
		return "redirect:output/search?prjId=";
	}

	@GetMapping("/output/search")
	public String viewOutputSearhList(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,
			@RequestParam String prjId, Model model, OutputSearchVO outputSearchVO) {
		
		this.checkAccess(employeeVO, prjId);
		
		ProjectListVO projectList = this.projectService.getAllProject();
		projectList.setProjectList(
				projectList.getProjectList().stream().filter(project -> project.getOutYn().equals("Y")).toList());
		List<CommonCodeVO> commonCodeList = this.commonCodeService.getAllCommonCodeListByPId("1000");
		List<CommonCodeVO> verStsList = this.commonCodeService.getAllCommonCodeListByPId("400");
		OutputListVO outputList = this.outputService.serarchAllOutputList(outputSearchVO);

		model.addAttribute("outputList", outputList).addAttribute("prjId", prjId)
				.addAttribute("projectList", projectList).addAttribute("commonCodeList", commonCodeList)
				.addAttribute("outputSearchVO", outputSearchVO)
				.addAttribute("verStsList", verStsList);

		return "output/outputlist";
	}

	@GetMapping("/output/write")
	public String viewCreateOutput(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,Model model) {
		this.checkAccess(employeeVO);
		
		ProjectListVO projectList = this.projectService.getAllProject();
		projectList.setProjectList(
				projectList.getProjectList().stream().filter((project) -> project.getOutYn().equals("Y")).toList());
		List<CommonCodeVO> outputType = this.commonCodeService.getAllCommonCodeListByPId("1000");
		List<CommonCodeVO> prjSts =this.commonCodeService.getAllCommonCodeListByPId("400");
		model.addAttribute("projectList", projectList).addAttribute("outputType", outputType)
		.addAttribute("prjSts", prjSts);
		return "output/outputwrite";
	}

	@PostMapping("/output/write")
	public String createOutput(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,@RequestParam MultipartFile file, OutputVO outputVO, Model model) {
		this.checkAccess(employeeVO, outputVO.getPrjId());
		
		Validator<OutputVO> validator = new Validator<>(outputVO);
		validator.add("outTtl", Type.NOT_EMPTY, "제목은 필수 입력값입니다").add("outType", Type.NOT_EMPTY, "산출물 타입은 필수 입력값입니다")
				.add("prjId", Type.NOT_EMPTY, "올바르지 않은 프로젝트에서 생성했습니다.").start();

		boolean isSuccess = this.outputService.insertOneOutput(outputVO, file);

		return "redirect:/output";

	}

	@GetMapping("output/downloadFile/{outId}")
	public ResponseEntity<Resource> fileDownload(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,@PathVariable String outId) {
		this.checkAccess(employeeVO);
		
		OutputVO Output = this.outputService.getOneOutput(outId);

		return this.outputService.getDownloadFile(Output);

	}

	@GetMapping("/output/modify/{outId}")
	public String viewModifyOutputPage(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,@PathVariable String outId, Model model) {
		this.checkAccess(employeeVO);
		
		ProjectListVO projectList = this.projectService.getAllProject();
		List<CommonCodeVO> outputType = this.commonCodeService.getAllCommonCodeListByPId("1000");
		OutputVO output = this.outputService.getOneOutput(outId);

		model.addAttribute("projectList", projectList).addAttribute("outputType", outputType).addAttribute("output",
				output);

		return "/output/outputmodify";

	}

	@PostMapping("/output/modify/{outId}")
	public String ModifyOutputPage(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,@PathVariable String outId, @RequestParam MultipartFile file, OutputVO outputVO) {
		this.checkAccess(employeeVO, outputVO.getPrjId());
		
		Validator<OutputVO> validator = new Validator<>(outputVO);
		validator.add("outTtl", Type.NOT_EMPTY, "제목은 필수 입력값입니다").add("outType", Type.NOT_EMPTY, "산출물 타입은 필수 입력값입니다")
				.add("prjId", Type.NOT_EMPTY, "올바르지 않은 프로젝트에서 생성했습니다.").start();

		boolean isSuccess = this.outputService.updateOneOutput(outputVO, file);
		return "redirect:/output";
	}

	@GetMapping("/output/delete/{outId}")
	public String deleteOutputment(@SessionAttribute("_LOGIN_USER_") EmployeeVO employeeVO,@PathVariable String outId, @RequestParam String prjId) {
		this.checkAccess(employeeVO, prjId);

		boolean isSuccess = this.outputService.deleteOneOutput(outId);

		return "redirect:/output/search?prjId=" + prjId;

	}
	
	private void checkAccess(EmployeeVO employeeVO, String prjId) {
		ProjectTeammateVO pmVO =this.projectDao.findPmByProjectId(prjId);
		if(! employeeVO.getAdmnCode().equals("301") )  {//관리자가 아닌경우
			if(pmVO != null) {//프로젝트 아이디가 주어진경우
				if(pmVO.getTmId().equals(employeeVO.getEmpId())) {//pm인경우
				}else {//pm이 아닌경우
					throw new PageNotFoundException();
				}
			}else{//프로젝트 아이디가 안주어진 경우
				List<ProjectTeammateVO>  tmList = this.projectService.getAllProjectTeammate()
				.stream()
				.filter(tm -> tm.getTmId().equals(employeeVO.getEmpId()))
				.filter(tm -> tm.getRole().equals("PM")).toList();
				
				if(tmList ==null || tmList.isEmpty()) {//PM을 맏은 포지션이 없다면
					throw new PageNotFoundException();
				}else {}//pm을 맞은 포지션이 있다면
			}
		}
	}
		
		private void checkAccess(EmployeeVO employeeVO) {
			if(! employeeVO.getAdmnCode().equals("301") )  {//관리자가 아닌경우
					//프로젝트 아이디가 안주어진 경우
				List<ProjectTeammateVO>  tmList = this.projectService.getAllProjectTeammate()
				.stream()
				.filter(tm -> tm.getTmId().equals(employeeVO.getEmpId()))
				.filter(tm -> tm.getRole().equals("PM")).toList();
	
				if(tmList ==null || tmList.isEmpty()) {//PM을 맏은 포지션이 없다면
					throw new PageNotFoundException();
				}else {}//pm을 맞은 포지션이 있다면
		}
	}
}
