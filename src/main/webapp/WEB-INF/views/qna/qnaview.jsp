<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@ taglib
prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Qna 상세 페이지</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <jsp:include page="../commonmodal.jsp" />
    <script type="text/javascript" src="/js/qna/qnaview.js"></script>
  </head>
  <body>
    <h1>Qna 게시글 조회</h1>

    <div class="grid" data-id="${qnaVO.qaId}">
      <span style="display: none" hidden id="login-email">${sessionScope._LOGIN_USER_.empId}</span>
      <label for="qaTtl">제목</label>
      <div>${qnaVO.qaTtl}</div>

      <label for="name">작성자 ID</label>
      <div>${qnaVO.crtrId}</div>

      <label for="name">수정자 ID</label>
      <div>${qnaVO.mdfrId}</div>

      <label for="originFileName">첨부파일</label>
      <div>
        <a href="/qna/file/download/${qnaVO.qaId}"> ${qnaVO.originFileName} </a>
      </div>

      <label for="crtDt">등록일</label>
      <div>${qnaVO.crtDt}</div>

      <label for="mdfDt">수정일</label>
      <div>${qnaVO.mdfDt}</div>

      <label for="knlCntnt">내용</label>
      <div>${qnaVO.qaCntnt}</div>

      <label for="knlCnt">조회수</label>
      <div>${qnaVO.qaCnt}</div>

      <label for="qaRecCnt">추천수</label>
      <div id="qaRecCnt">${qnaVO.qaRecCnt}</div>

      <div class="replies">
        <div class="reply-items"></div>
        <div class="write-reply">
          <textarea id="txt-reply" name="rplCntnt" data-issue-id="${qnaVO.qaId}"></textarea>
          <button id="btn-save-reply" data-mode="">등록</button>
          <button id="btn-cancel-reply">취소</button>
        </div>
      </div>

      <div class="btn-group">
        <button type="button" class="recommend-qna" value="${qnaVO.qaId}">추천하기</button>

        <button>
          <a href="/qna/modify/${qnaVO.qaId}">수정</a>
        </button>
        <button>
          <a class="delete-qna" href="javascript:void(0);">삭제</a>
        </button>
      </div>

      <script>
        $().ready(function () {
          qaId = $(".recommend-qna").val();

          $(".recommend-qna").on("click", function () {
            $.post("/qna/recommend/" + qaId, function (response) {
              var alertModal = $(".modal-confirm-window");
              var modalButton = $(".confirm-confirm-button");
              var modalButton1 = $(".cancel-confirm-button");
              var modalText = $(".modal-confirm-text");
              modalText.text(response.data.result);
              modalButton.text("확인");
              modalButton1.css("display", "none");
              alertModal[0].showModal();
              $("#qaRecCnt").html(response.data.count);
            });
          });
          $(".confirm-confirm-button").on("click", function () {
            location.reload();
          });
        });
      </script>
    </div>
  </body>
</html>
