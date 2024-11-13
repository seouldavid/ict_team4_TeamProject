<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="/resources/css/admin.css" rel="stylesheet" />
<title>Document</title>
<style type="text/css">
table {
	text-align: center;
	margin-bottom: 10px;
}
</style>
</head>



<body>
	<!--탑1 --------------------------------------------------------------------------- -->
	<div class="page">
		<div class="top1">
			<div>
				<a href="/home"><img class="bt_header" href="#"
					src="/resources/img/admin_img/logo.png" alt="header" /></a>
			</div>
			<div class="administrator">ADMINISTRATOR</div>
			<div class="gohome">
				<div></div>
				<div class="house">
					<a href="/home"> <img class="houseimg"
						src="/resources/img/admin_img/check_img.svg">
						<button class="homepagebutton" style="cursor: pointer;">홈페이지</button>
					</a>
				</div>
				<div class="logoutbox">
					<a href="/login"> <img class="houseimg"
						src="/resources/img/admin_img/right-from-bracket-solid.svg">
						<button class="logoutbutton" style="cursor: pointer;">로그아웃</button>
					</a>
				</div>
			</div>
		</div>

		<!--탑2 --------------------------------------------------------------------------- -->



		<div class="main">

			<!-- 네비게이션--------------------------------------------------------------------------- -->
			<div class="nav">
				<ul class="nav_button">
					<li><a href="/index"><img title="메인페이지" class="nav_img"
							src="/resources/img/admin_img/button-0.svg" alt="메인페이지 " /></a></li>
					<li><a href="/member"><img title="회원관리" class="nav_img"
							src="/resources/img/admin_img/button-1.svg" alt="회원관리" /></a></li>
					<li><a href="/admin_board"><img title="게시판 관리"
							class="nav_img" src="/resources/img/admin_img/button-2.svg"
							alt="게시판 관리" /></a></li>
					<li><a href="/hospital_info"><img title="병원정보관리"
							class="nav_img" src="/resources/img/admin_img/button-3.svg"
							alt="병원정보관리" /></a></li>
					<li><a href="/admin"><img title="관리자관리" class="nav_img"
							src="/resources/img/admin_img/button-4.svg" alt="관리자관리" /></a></li>
					<li><a href="/config"><img title="환경설정" class="nav_img"
							src="/resources/img/admin_img/img_setting.png" alt="환경설정" style="height: 16px; width: 16px;" /></a></li>
				</ul>

			</div>


			<!-- 메인 테이블--------------------------------------------------------------------------- -->

			<div class="content">
				<div class="top2">
					<div class="top2text">
						<h2>관리자 메인</h2>
					</div>


				</div>
				<div class="searchsection">
					<!-- 검색창  /  빈 div 건드리지 마시오 -->
					<div></div>
					<div class="searchbox">
						<!--  <input type="search">
                    <input type="submit" value="검색"> -->
					</div>
				</div>
				<!-- 테이블 박스 -->
				<div class="tablebox">
					<section>
						<h2>회원 관리</h2>
						<table>
							<thead>
								<tr>
									<th>회원아이디</th>
									<th>닉네임</th>
									<th style="width: 10%;">권한</th>
									<th style="width: 12%;">메일 인증</th>
									<th style="width: 12%;">광고 차단</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="k" items="${list5}" varStatus="c">
								<tr>
									<td>${k.mber_id}</td>
									<td>${k.mber_nm}</td>
									<td>
										<c:choose>
										<c:when test="${k.mber_auth == '2'}">읽기 및 쓰기</c:when>
										<c:when test="${k.mber_auth == '1'}">읽기</c:when>
										</c:choose>
									</td>	
									<td>${k.email_status}</td>
									<td>${k.adv_status}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<div class="button-group">
							<button type="button" onclick="location.href='/member'">회원
								관리 페이지</button>
						</div>
					</section>

					<section>
						<h2>게시물 관리</h2>
						<table>
							<thead>
								<tr>
									<th>그룹</th>
									<th>작성자</th>
									<th style="width: 40%;">제목</th>
									<th style="width: 20%;">최종 수정일</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="k" items="${list4}" varStatus="c">
								<tr>
							        <td>
							            <c:choose>
							                <c:when test="${k.bbs_id == 'NOTIS'}">공지사항</c:when>
							                <c:when test="${k.bbs_id == 'INQU'}">문의사항</c:when>
							                <c:when test="${k.bbs_id == 'CONS'}">상담내역</c:when>
							                <c:when test="${k.bbs_id == 'FREE'}">자유게시판</c:when>
							            </c:choose>
							        </td>
							        <td>${k.mber_nm}</td>
							        <td>${k.wr_subject}</td>
							        <td>${k.upd_dt}</td>
							    </tr>
							</c:forEach>
							</tbody>
						</table>
						<div class="button-group">
							<button type="button" onclick="location.href='/admin_board'">게시물
								관리 페이지</button>
						</div>
					</section>

					<section>
						<h2>병원 정보 관리</h2>
						<table>
							<thead>
								<tr>
									<th>그룹</th>
									<th>병원명</th>
									<th>지역</th>
									<th>등급</th>
									<th>작성자</th>
									<th style="width: 20%;">최종 수정일</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="k" items="${list3}" varStatus="c">
								<tr>
									<td>
										<c:choose>
										<c:when test="${k.h_type == 'N'}">요양원</c:when>
										<c:when test="${k.h_type == 'H'}">요양병원</c:when>
										</c:choose>
									</td>
									<td>${k.h_name}</td>
									<td>${k.city_name}</td>
									<td>${k.grade}</td>
									<td>${k.crt_by}</td>
									<td>${k.upd_dt}</td>
								</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="button-group">
							<button type="button" onclick="location.href='/hospital_info'">병원
								정보 관리 페이지</button>
						</div>
					</section>
					<section>
						<h2>관리자 관리</h2>
						<table>
							<thead>
								<tr>
									<th>관리자 아이디</th>
									<th>비밀번호</th>
									<th>채팅관리</th>
									<th>팝업관리</th>
									<th>게시판관리</th>
									<th>관리자 권한레벨</th>
									<th>상태</th>
									<th>최근접속</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="k" items="${list2}" varStatus="c">
									<tr>
										<td>${k.admin_id}</td>
										<td>${k.admin_pw}</td>
										<!-- 권한 체크박스 추가 -->
										<td><input type="checkbox" name="chat_manage" value="o"
											<c:if test="${k.chat_auth == 'o'}">checked</c:if> disabled></td>
										<td><input type="checkbox" name="popup_manage" value="o"
											<c:if test="${k.popup_auth == 'o'}">checked</c:if> disabled></td>
										<td><input type="checkbox" name="board_manage" value="o"
											<c:if test="${k.board_auth == 'o'}">checked</c:if> disabled></td>
										<td>${k.authority_level}</td>
										<td>${k.status}</td>
										<td>${k.last_visit}</td>
										<!-- 관리 버튼 -->
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="button-group">
							<button type="button" onclick="location.href='/admin'">관리자
								관리 페이지</button>
						</div>
					</section>

					<section>
						<h2>팝업창 관리</h2>
						<table>
							<thead>
								<tr>
									<th>번호</th>
									<!-- <th>상태</th> -->
									<th>제목</th>
									<th>작성자</th>
									<th>시작일</th>
									<th>종료일</th>
									<th>수정</th>
									<th>삭제</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="k" items="${list}" varStatus="c">
									<tr>
										<td>${c.count}</td>
										<td>${k.pu_title}</td>
										<td>admin</td>
										<td>${k.pu_start_time}</td>
										<td>${k.pu_end_time}</td>

										<td><button type="button"
												onclick="location.href='/popup_edit?pu_id=${k.pu_id}'">수정</button></td>
										<td><button type="button"
												onclick="location.href='/popup_delete?pu_id=${k.pu_id}'">삭제</button></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="button-group">
							<button type="button" onclick="location.href='/popup'">등록</button>
						</div>
					</section>
				</div>
			</div>
		</div>
		<!--푸터 --------------------------------------------------------------------------- -->
		<div class="footer"></div>
	</div>
	<script type="text/javascript">
		
	</script>
</body>
</html>