<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Qna 리스트</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <script
      type="text/javascript"
      src="/js/qna/qnalist.js"
    ></script>
  </head>
  <body>
    <div class="grid">
      <div>총 ${qnaList.qnaCnt} 건의 게시글이 검색되었습니다.</div>
      <table class="table">
        <colgroup>
          <col width="40px" />
          <col width="100px" />
          <col width="180px" />
          <col width="100px" />
          <col width="*" />
          <col width="80px" />
          <col width="80px" />
          <col width="150px" />
        </colgroup>
        <thead>
          <tr>
            <th>
              <input type="checkbox" id="checked-all" data-target-class="target-qna-id">
              <label for="checked-all"></label>
            </th>
            <th>프로젝트</th>
            <th>요구사항명</th>
            <th>등록자</th>
            <th>제목</th>
            <th>조회수</th>
            <th>추천수</th>
            <th>작성일자</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${not empty qnaList.qnaList}">
              <c:forEach items="${qnaList.qnaList}" var="qna" varStatus="loop">
              <tr>
                <td>
                  <input type="checkbox" class="target-qna-id" id="target-qna-id-${loop.index}" value="${qna.qaId}">
                  <label for="target-qna-id-${loop.index}"></label>
                </td>
                  <td>${qna.projectVO.prjName}</td>
                  <td>${qna.requirementVO.rqmTtl}</td>
                  <td>${qna.crtrId}</td>
                  <td>
                    <a
                      class="ellipsis"
                      href="/qna/view?qaId=${qna.qaId}"
                    >
                      ${qna.qaTtl}</a
                    >
                  </td>
                  <td>${qna.qaCnt}</td>
                  <td>${qna.qaRecCnt}</td>
                  <td>${qna.crtDt}</td>
                </tr>
              </c:forEach>
            </c:when>
            <%-- qnaList 의 내용이 존재하지 않는다면 --%>
            <c:otherwise>
              <tr>
                <td colspan="6"></td>
                <a href="/qna/write">
                  등록된 게시글이 없습니다. 첫 번째 글의 주인공이 되어보세요!
                </a>
              </tr>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>

    <!-- 검색 -->
    <div>
      <form id="search-form">
        <input type="hidden" id="page-no" name="pageNo" value="0">
        <select name="listSize" id="list-size">
          <option value="10" ${searchQnaVO.listSize eq 10 ? 'selected' : ''}>10개</option>
          <option value="20" ${searchQnaVO.listSize eq 20 ? 'selected' : ''}>20개</option>
          <option value="30" ${searchQnaVO.listSize eq 30 ? 'selected' : ''}>30개</option>
          <option value="50" ${searchQnaVO.listSize eq 50 ? 'selected' : ''}>50개</option>
          <option value="100" ${searchQnaVO.listSize eq 100 ? 'selected' : ''}>100개</option>
        </select>

        <select name="searchType" id="search-type">
          <option value="prjName" ${searchQnaVO.searchType eq 'prjName' ? 'selected' : ''}>프로젝트</option>
          <option value="rqmTtl" ${searchQnaVO.searchType eq 'rqmTtl' ? 'selected' : ''}>요구사항명</option>
          <option value="qaTtl" ${searchQnaVO.searchType eq 'qaTtl' ? 'selected' : ''}>제목</option>
          <option value="qaCntnt" ${searchQnaVO.searchType eq 'qaCntnt' ? 'selected' : ''}>내용</option>
          <option value="qaTtl_qaCntnt" ${searchQnaVO.searchType eq 'qaTtl_qaCntnt' ? 'selected' : ''}>제목 + 내용</option>
          <option value="crtrId" ${searchQnaVO.searchType eq 'crtrId' ? 'selected' : ''}>등록자</option>
        </select>

        <input type="text" name="searchKeyword" value="${searchQnaVO.searchKeyword}">
        <button type="button" id="search-btn">검색</button>
        <button type="button" id="search-btn-cancel">초기화</button>

        <!-- pagination -->
        <ul>
          <c:if test="${searchQnaVO.hasPrevGroup}">
            <li>
              <a href="javascript:search(0);">처음</a>
            </li>
            <li>
              <a href="javascript:search(${searchQnaVO.prevGroupStartPageNo});">이전</a>
            </li>
          </c:if>
          <c:forEach begin = "${searchQnaVO.groupStartPageNo}"
                      end = "${searchQnaVO.groupEndPageNo}"  step="1" var="p">
            <li class="${searchQnaVO.pageNo eq p ? 'active' : ''}">
              <a href="javascript:search(${p});">${p+1}</a>
            </li>
          </c:forEach>

          <c:if test="${searchQnaVO.hasNextGroup}">
              <li>
                <a href="javascript:search(${searchQnaVO.nextGroupStartPageNo});">다음</a>
              </li>
              <li>
                <a href="javascript:search(${searchQnaVO.pageCount - 1});">마지막</a>
              </li>
            </c:if>
        </ul>
      </form>
    </div>
<!-- Paginator 끝 -->

    <div>
      <button class="btn-group">
      <a class="btn-group" href="/qna/write">새 글 등록</a>
    </button>
    <button>
      <a href="/qna/excel/download">엑셀 다운</a>
      </button>
      <!-- <button>
        <a id="uploadExcelfile" href="javaScript:void(0)">일괄 등록</a>
        <input type="file" id="excelfile" style="display: none;">
        </button> -->
        <button>
          <a id="deleteMassiveQna" href="javaScript:void(0)">일괄 삭제</a>
        </button>
    </div>

  </div>
  </div>
  </body>
</html>