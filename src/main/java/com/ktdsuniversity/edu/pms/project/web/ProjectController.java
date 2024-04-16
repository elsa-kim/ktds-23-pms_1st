package com.ktdsuniversity.edu.pms.project.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.ktdsuniversity.edu.pms.employee.service.EmployeeService;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeListVO;
import com.ktdsuniversity.edu.pms.employee.vo.EmployeeVO;
import com.ktdsuniversity.edu.pms.exceptions.PageNotFoundException;
import com.ktdsuniversity.edu.pms.project.vo.ProjectTeammateVO;
import com.ktdsuniversity.edu.pms.utils.Validator;
import com.ktdsuniversity.edu.pms.utils.Validator.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ktdsuniversity.edu.pms.commoncode.service.CommonCodeService;
import com.ktdsuniversity.edu.pms.commoncode.vo.CommonCodeVO;
import com.ktdsuniversity.edu.pms.exceptions.CreationException;
import com.ktdsuniversity.edu.pms.project.service.ProjectService;
import com.ktdsuniversity.edu.pms.project.vo.CreateProjectVO;
import com.ktdsuniversity.edu.pms.project.vo.ProjectListVO;
import com.ktdsuniversity.edu.pms.project.vo.ProjectVO;
import com.ktdsuniversity.edu.pms.project.vo.SearchProjectVO;
import com.ktdsuniversity.edu.pms.utils.AjaxResponse;

@Controller
public class ProjectController {

    Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CommonCodeService commonCodeService;

//    @Autowired
//    private EmployeeService employeeService;

    @GetMapping("/project")
    public String redirectToProjectSearchPage() {
//        ProjectListVO projectListVO = projectService.getAllProject();
//
//        model.addAttribute("projectList", projectListVO);

        return "redirect:/project/search";
    }

    @GetMapping("/project/search")
    public String viewSearchProjectListPage(Model model,
                                            SearchProjectVO searchProjectVO) {
//        ProjectListVO projectListVO = projectService.getAllProject();

        ProjectListVO projectListVO = projectService
                .searchProject(searchProjectVO);
        List<CommonCodeVO> projectCommonCodeList = commonCodeService
                .getAllCommonCodeListByPId("400");

        model.addAttribute("commonCodeList", projectCommonCodeList);
        model.addAttribute("projectList", projectListVO);
        model.addAttribute("searchProjectVO", searchProjectVO);

        return "project/projectlist";
    }

    /**
     * /project/view?projectId=PRJ_240409_000012
     *
     * @param prjId
     * @return
     */
    @GetMapping("/project/view")
    public String viewProjectDetailPage(@RequestParam String prjId, Model model) {
        ProjectVO projectVO = projectService.getOneProject(prjId);
        int projectTeammateCount = projectService.getProjectTeammateCount(prjId);

        // 사원 검증 로직, 관리자인지, 프로젝트의 팀에 해당되는 사람인지 확인해야한다. 권한 없으므로 예외
        // boolean isTeammate = projectVO.getProjectTeammateList().stream()
        // .anyMatch(teammate -> teammate.getTmId().equals(세션에 있는 사원 아이디));

        // PM 뽑기
        Optional<ProjectTeammateVO> pmOptional = projectVO.getProjectTeammateList().stream()
                .filter(projectTeammateVO -> "PM".equals(projectTeammateVO.getRole()))
                .findFirst();

        if (pmOptional.isPresent()) {
            ProjectTeammateVO pm = pmOptional.get();
            model.addAttribute("project", projectVO);
            model.addAttribute("teammateCount", projectTeammateCount);
            model.addAttribute("pm", pm);
        } else {
            throw new PageNotFoundException();
        }

        return "project/projectview";
    }

    // chart.js api data
    @ResponseBody
    @GetMapping("/ajax/project/status/{projectId}")
    public AjaxResponse responseProjectStatus(@PathVariable String projectId) {
        return new AjaxResponse().append("chartData", projectService.getProjectStatus(projectId));
    }

    @GetMapping("/project/team")
    public String viewProjectTeamPage(@RequestParam String prjId, Model model) {
        int teammateCount = projectService.getProjectTeammateCount(prjId);
        List<ProjectTeammateVO> teammate = projectService.getAllProjectTeammateByProjectId(prjId);

        model.addAttribute("teammateCount", teammateCount);
        model.addAttribute("teammate", teammate);

        return null;
    }


    @GetMapping("/project/write")
    public String viewProjectWritePage(Model model) {

//        List<EmployeeVO> employeeList = employeeService.getAllEmployee().getEmployeeList();
//
//        model.addAttribute("employee", employeeList);

        return "project/projectwrite";
    }

    // 작성자 추가를 위해 SessionAttribute 추가 필요, @SessionAttribute("_LOGIN_USER_")
    // MemberVO
    // memberVO
    // form action 추가 필요

    @ResponseBody
    @PostMapping("/ajax/project/write")
    public AjaxResponse writeProject(CreateProjectVO createProjectVO) {
        // 0. session memberVO가 admin 이 아닌 경우, return list page or return 400
        // page(잘못된
        // 접근)

        Validator<CreateProjectVO> validator = new Validator<>(createProjectVO);

        validator.add("prjName", Type.NOT_EMPTY, "프로젝트명을 입력해주세요.")
                .add("clntInfo", Type.NOT_EMPTY, "고객사를 입력해주세요.")
                .add("deptId", Type.NOT_EMPTY, "부서를 선택해주세요.")
                .add("strtDt", Type.NOT_EMPTY, "시작일을 입력해주세요.")
                .add("endDt", Type.NOT_EMPTY, "종료일을 입력해주세요.")
                .add("strtDt", Type.DATE, createProjectVO.getEndDt(), "종료일은 시작일보다 이후여야 합니다. 날짜를 다시 설정해주세요")
                .start();

        if (validator.hasErrors()) {
            Map<String, List<String>> errors = validator.getErrors();
            return new AjaxResponse().append("errors", errors);
        }

        if (createProjectVO.getReqYn() == null) {
            if (createProjectVO.getIsYn() != null
                    || createProjectVO.getKnlYn() != null
                    || createProjectVO.getQaYn() != null) {
                throw new CreationException();
            }
        }

        // 2. 검증 로직에 잘 맞춰서 작성한 경우, 데이터 저장
        // 2-1. 세션에서 작성자 id 추출, projectVO.setCrtrId();
        // 현재는 정적 데이터로 해결함.
        createProjectVO.setCrtrId("system01");

        boolean isCreateSuccess = projectService
                .createNewProject(createProjectVO);

        if (!isCreateSuccess) {
            throw new CreationException();
        }

        String prjId = createProjectVO.getPrjId();

        return new AjaxResponse().append("next", "/project/view?projectId=" + prjId);
    }

    /**
     * TODO
     */
    @GetMapping("/project/modify/{projectId}")
    public String viewProjectModifyPage(@PathVariable String projectId,
                                        Model model) {

        ProjectVO projectVO = projectService.getOneProject(projectId);

        // 작성자 또는 PM인지를 검증하는 로직 작성 필요

        model.addAttribute("projectVO", projectVO);

        return "project/projectmodify";
    }

    // 수정자 추가를 위해 SessionAttribute 추가 필요
    @PostMapping("/project/modify/{projectId}")
    public String modifyProject(@PathVariable String projectId) {
        // 1. 프로젝트를 가져와서 있는지 확인
        ProjectVO originalProjectVO = projectService.getOneProject(projectId);

        // 2. 세션으로 관리자 판별 (originalProjectVO와 유저를 판별 및 유저 권한으로 판별), 실패 시 throw
        // new
        // RuntimeException

        // 3. 데이터 수정 여부 확인

        return "redirect:/project/view?projectId=" + projectId;
    }

    // 수정자 추가를 위해 SessionAttribute 추가 필요,
    // 수정자 추가 시, Mapper 에도 컬럼 추가 필요 Parameter 도 Id에서 VO로 변경 필요
    @GetMapping("/project/delete/{projectId}")
    public String deleteProject(@PathVariable String projectId) {
        // 1. 프로젝트를 가져와서 있는지 확인
        ProjectVO originalProjectVO = projectService.getOneProject(projectId);

        // 2. 검증 로직 (originalProjectVO와 유저를 판별 및 유저 권한으로 판별), 실패 시 throw new
        // 작성자 또는 관리자
        // RuntimeException

        // 3. 데이터 삭제 여부 확인
        boolean isDeleteSuccess = projectService.deleteOneProject(projectId);

//        if (isDeleteSuccess) {
//            성공로그
//        } else {
//            실패로그
//        }

        return "redirect:/project";
    }
}
