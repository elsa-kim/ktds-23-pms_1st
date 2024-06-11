package com.ktdsuniversity.edu.pms.employee.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.ktdsuniversity.edu.pms.beans.FileHandler;
import com.ktdsuniversity.edu.pms.changehistory.service.ChangeHistoryService;
import com.ktdsuniversity.edu.pms.changehistory.vo.DepartmentHistoryVO;
import com.ktdsuniversity.edu.pms.changehistory.vo.JobHistoryVO;
import com.ktdsuniversity.edu.pms.changehistory.vo.PositionHistoryVO;
import com.ktdsuniversity.edu.pms.commoncode.service.CommonCodeService;
import com.ktdsuniversity.edu.pms.commoncode.vo.CommonCodeVO;
import com.ktdsuniversity.edu.pms.department.service.DepartmentService;
import com.ktdsuniversity.edu.pms.department.vo.DepartmentListVO;
import com.ktdsuniversity.edu.pms.employee.service.EmployeeService;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeDataVO;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeListVO;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeVO;
import com.ktdsuniversity.edu.pms.employee.vo.SearchEmployeeVO;
import com.ktdsuniversity.edu.pms.employee.web.EmployeeController;
import com.ktdsuniversity.edu.pms.job.service.JobService;
import com.ktdsuniversity.edu.pms.job.vo.JobVO;
import com.ktdsuniversity.edu.pms.requirement.vo.RequirementVO;
import com.ktdsuniversity.edu.pms.team.service.TeamService;
import com.ktdsuniversity.edu.pms.team.vo.TeamListVO;
import com.ktdsuniversity.edu.pms.utils.ApiResponse;
import com.ktdsuniversity.edu.pms.utils.ValidationUtils;
import com.ktdsuniversity.edu.pms.utils.Validator;
import com.ktdsuniversity.edu.pms.utils.Validator.Type;

@RestController
@RequestMapping("/api/v1")
public class ApiEmployeeController {

	private Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private ChangeHistoryService changeHistoryService;	

	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileHandler fileHandler;
	
	private CommonCodeService commonCodeService;
	
	
	// 사원 리스트
	@GetMapping("/employee")
	public ApiResponse getEmployeeList(SearchEmployeeVO searchEmployeeVO) {
		
		EmployeeListVO employeeListVO = this.employeeService.searchAllEmployee(searchEmployeeVO);
		
		return ApiResponse.Ok(employeeListVO.getEmployeeList(), employeeListVO.getEmployeeCnt(),
								searchEmployeeVO.getPageCount(), searchEmployeeVO.getPageNo() < searchEmployeeVO.getPageCount() -1);
	}
	
	// 사원 상세 조회
	@GetMapping("/employee/view/{empId}")
	public ApiResponse getOneEmployee(@PathVariable String empId, Authentication authentication) {
		
		EmployeeVO employeeVO = this.employeeService.getOneEmployee(empId);
		
		// 변경이력 TODO@@!!
//		PaHistoryVO paHistoryVO = this.
//		List<JobVO> jobList = this.changeHistoryService.getAllJob();
//		List<CommonCodeVO> positionList = this.changeHistoryService.getAllPosition();
		
		return ApiResponse.Ok(employeeVO);
	}
	
	// 부서, 팀, 직무, 직급 리스트 조회
	@GetMapping("/employee/data")
	public ApiResponse getDataList() {
		var result = new HashMap<String, List<EmployeeDataVO>>();
		List<EmployeeDataVO> deptList = this.employeeService.getDepartList();
		List<EmployeeDataVO> teamList = this.employeeService.getTeamList();
		List<EmployeeDataVO> jobList = this.employeeService.getJobList();
		List<EmployeeDataVO> gradeList = this.employeeService.getEmployeeGradeList();
		List<EmployeeDataVO> workStsList = this.employeeService.getEmployeeWorkStsList();
		
		result.put("depart", deptList);
		result.put("team", teamList);
		result.put("job", jobList);
		result.put("grade", gradeList.stream().filter(grade -> {
            return Integer.parseInt(grade.getDataId()) <= 110;
    }).toList());
		result.put("workSts", workStsList.stream().filter(workSts -> {
			return Integer.parseInt(workSts.getDataId()) <= 204;
		}).toList());
		
		return ApiResponse.Ok(result);
	}
	
	
	// 수정
	@PutMapping("/employee/modify/{empId}")
	public ApiResponse domodifyEmployee(@RequestBody EmployeeVO employeeVO,
										@PathVariable("empId") String empId,
										MultipartFile file,
										Authentication authentication) {
		
		Validator<EmployeeVO> validator = new Validator<EmployeeVO>(employeeVO);

		validator.add("empName", Type.NOT_EMPTY, "이름을 입력해주세요.")
				.add("empId", Type.NOT_EMPTY, "사원번호를 입력해주세요.")
				.add("jobId", Type.NOT_EMPTY, "직무를 입력해주세요.")
				.add("deptId", Type.NOT_EMPTY, "부서를 입력해주세요.")
				.add("pstnId", Type.NOT_EMPTY, "직급을 입력해주세요.")
				.add("workSts", Type.NOT_EMPTY, "재직상태를 입력해주세요.")
				.add("salYear", Type.NOT_EMPTY, "호봉을 입력해주세요.")
				.add("hireDt", Type.NOT_EMPTY, "입사일을 설정해주세요.")
				.add("hireYear", Type.NOT_EMPTY, "연차를 설정해주세요.")
				.add("addr", Type.NOT_EMPTY, "주소를 입력해주세요.")
				.add("brth", Type.NOT_EMPTY, "생일을 입력해주세요.")
				.add("email", Type.NOT_EMPTY, "이메일을 입력해주세요.")
				.add("email", Type.EMAIL, "이메일 형식으로 입력해주세요.")
				.add("pwd", Type.NOT_EMPTY, "비밀번호를 입력해주세요.")
				.add("confirmPwd", Type.NOT_EMPTY, "비밀번호 확인을 입력해주세요.")
				.add("mngrYn", Type.NOT_EMPTY, "임원여부를 설정해주세요.")
				.start();
		
		if(validator.hasErrors()) {
			return ApiResponse.BAD_REQUEST(validator.getErrors());
		}
		
		boolean isSuccess = this.employeeService.modifyOneEmployee(employeeVO);
		
		return ApiResponse.Ok(isSuccess);
	}
	
	
	// 회원 등록
	@PutMapping("/employee/regist")
	public ApiResponse registOneEmployee(EmployeeVO employeeVO, @RequestParam(required = false) MultipartFile file,
											Authentication authentication) {
		
		Validator<EmployeeVO> validator = new Validator<>(employeeVO);

		validator.add("empName", Type.NOT_EMPTY, "사원이름을 입력해 주세요.")
				.add("pwd", Type.NOT_EMPTY, "비밀번호를 입력해 주세요.")
				.add("pwd", Type.PASSWORD, "비밀번호 형식으로 입력해 주세요.")
				.add("confirmPwd", Type.NOT_EMPTY, "비밀번호 확인을 입력해 주세요.")
				.add("confirmPwd", Type.EQUALS, employeeVO.getPwd(), "동일한 비밀번호를 입력해 주세요.")
				.add("hireDt", Type.NOT_EMPTY, "입사일을 지정해 주세요.")
				.add("hireDt", Type.NOW_DATE, "입사일은 현재 날짜보다 이전이어야 합니다.")
				.add("addr", Type.NOT_EMPTY, "주소를 입력해 주세요.")
				.add("brth", Type.NOT_EMPTY, "생일을 지정해 주세요.")
				.add("brth", Type.NOW_DATE, "생일은 현재 날짜보다 이전이어야 합니다.");
		if (file != null) {
			employeeVO.setFileName(file.getContentType());
			validator.add("fileName", Type.IMAGE_FILE, "이미지 형식의 파일을 업로드 해주세요.");
		}
		validator.start();
		
		
		boolean isCreateSuccess = this.employeeService.createEmployee(employeeVO, file);
		
		return ApiResponse.Ok(isCreateSuccess);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
