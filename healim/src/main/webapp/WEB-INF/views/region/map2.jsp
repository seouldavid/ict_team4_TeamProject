<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<style>


body{
	width: auto%;
    align-items: center;
}


    .topper1 {
        width: 100px; /* 원하는 크기로 조정 */
        height: auto;
        display: flex;
        justify-content: center;
        align-items: center;
        margin: 0 auto;
        position: relative;
    }

    .topper1 img {
        width: 300px; /* 로고 이미지의 크기를 조정 */
        height: auto;
        text-align: center;
    }
    
     .header {
        text-align: center;
        margin-top: 10px;
    }

    .logo {
        font-size: 80px;
        color: #333;
        text-decoration: none;
        display: block;
        margin-bottom: 20px;
        text-align: center;
        text-decoration-style: solid;
    }
    body {
        font-family: Arial, sans-serif;
    }

    .map_wrap {
        position: relative;
        width: 100%;
        height: 600px; /* 지도의 높이를 600px로 키움 */
    }

    #menu_wrap {
        position: absolute;
        top: 0;
        left: 0;
        width: 300px;
        height: 100%; /* 높이를 지도의 100%로 설정 */
        padding: 10px;
        background-color: rgba(255, 255, 255, 0.8);
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
        z-index: 2;
        overflow-y: auto; /* 내용이 길어질 경우 스크롤바 추가 */
    }


    .option {
        margin-bottom: 10px;
    }

    .option input[type="text"] {
        width: 80%;
        padding: 5px;
        font-size: 14px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .option button {
        padding: 6px 10px;
        font-size: 14px;
        color: #fff;
        background-color: #5a67d8;
        border: none;
        border-radius: 4px;
        cursor: pointer;
    }

    .option button:hover {
        background-color: #4c51bf;
    }

    #placesList {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    #placesList li {
        padding: 10px;
        border-bottom: 1px solid #ddd;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    #placesList li h5 {
        font-size: 16px;
        margin: 0;
    }

    #placesList li .tel {
        font-size: 14px;
        color: #999;
    }

    #pagination {
        margin-top: 10px;
        text-align: center;
    }

    #pagination a {
        margin: 0 5px;
        padding: 5px 10px;
        background-color: #f1f1f1;
        border-radius: 5px;
        color: #333;
        text-decoration: none;
    }

    #pagination a.on {
        background-color: #5a67d8;
        color: white;
    }
      /* 네비게이션 스타일 */
        .footer-nav {
            width: 70%;
            padding: 15px;
            background-color: #ffffff;
            position: fixed;
            bottom: 0;
            display: flex;
            justify-content: space-around;
            box-shadow: 0px -4px 8px rgba(0, 0, 0, 0.1);
            left: 0;
            right: 0;
            margin: 0 auto;
        }

        .footer-nav div {
            text-align: center;
            cursor: pointer;
        }

        .footer-nav img {
            width: 30px;
            height: 30px;
        }

        .footer-nav span {
            display: block;
            margin-top: 5px;
            font-size: 12px;
            color: #555;
        }
        
         .footer-nav {
      width: 100%;
      padding: 15px;
      background-color: #ffffff;
      position: fixed;
      bottom: 0;
      display: flex;
      justify-content: space-around;
      box-shadow: 0px -4px 8px rgba(0, 0, 0, 0.1);
    }

    .footer-nav div {
      text-align: center;
      cursor: pointer;
    }

    .footer-nav img {
      width: 30px;
      height: 30px;
    }

    .footer-nav span {
      display: block;
      margin-top: 5px;
      font-size: 12px;
      color: #555;
    }
</style>
</head>
<body>

<div class="topper1">
<a href="/home" ><img src="resources/img/main.png"></a>

</div>
<hr>

<div class="map_wrap">
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div>

    <div id="menu_wrap">
        <div class="option">
            <form onsubmit="searchPlaces(); return false;">
                <label for="keyword">키워드:</label>
                <input type="text" value="경기도 요양기관" id="keyword" size="15">
                <button type="submit">검색하기</button>
            </form>
        </div>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>
</div>
<hr>

<div class="footer-nav">
  <div onclick="location.href='home'">
    <img src="/resources/img/hom4.jpg" alt="홈">
    <span>홈</span>
  </div>
  <div onclick="location.href='region_selected'">
    <img src="/resources/img/sisul.jpg" alt="시설찾기">
    <span>시설찾기</span>
  </div>
  <div onclick="location.href='boardListAll?bbs_id=CONS'">
    <img src="/resources/img/sangdam.jpg" alt="상담사례">
    <span>상담사례</span>
  </div>
  <div onclick="location.href='myPage'">
    <img src="/resources/img/my.jpg" alt="마이페이지">
    <span>마이페이지</span>
  </div>
</div>

<!-- 수정된 자바스크립트 파일 로딩 -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=6f548e1d389e3b8ae5804cac96623033&libraries=services"></script>

<script>
    // 마커를 담을 배열입니다
    var markers = [];

    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
        mapOption = {
            center: new kakao.maps.LatLng(37.27538, 127.05488), // 지도의 중심좌표
            level: 50 // 지도의 확대 레벨
        };

    // 지도를 생성합니다
    var map = new kakao.maps.Map(mapContainer, mapOption);

    // 장소 검색 객체를 생성합니다
    var ps = new kakao.maps.services.Places();

    // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({zIndex:1});

    // 키워드로 장소를 검색합니다
    searchPlaces();

    // 키워드 검색을 요청하는 함수입니다
    function searchPlaces() {
        var keyword = document.getElementById('keyword').value;

        if (!keyword.replace(/^\s+|\s+$/g, '')) {
            alert('키워드를 입력해주세요!');
            return false;
        }

        // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
        ps.keywordSearch( keyword, placesSearchCB);
    }

    // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
    function placesSearchCB(data, status, pagination) {
        if (status === kakao.maps.services.Status.OK) {
            displayPlaces(data);
            displayPagination(pagination);
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
            return;
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
            return;
        }
    }

    // 검색 결과 목록과 마커를 표출하는 함수입니다
    function displayPlaces(places) {
        var listEl = document.getElementById('placesList'),
            menuEl = document.getElementById('menu_wrap'),
            fragment = document.createDocumentFragment(),
            bounds = new kakao.maps.LatLngBounds();

        // 검색 결과 목록에 추가된 항목들을 제거합니다
        removeAllChildNods(listEl);

        // 지도에 표시되고 있는 마커를 제거합니다
        removeMarker();

        for ( var i=0; i<places.length; i++ ) {
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, places[i]);

            bounds.extend(placePosition);

            (function(marker, title) {
                kakao.maps.event.addListener(marker, 'mouseover', function() {
                    displayInfowindow(marker, title);
                });
                kakao.maps.event.addListener(marker, 'mouseout', function() {
                    infowindow.close();
                });
                itemEl.onmouseover =  function () {
                    displayInfowindow(marker, title);
                };
                itemEl.onmouseout =  function () {
                    infowindow.close();
                };
            })(marker, places[i].place_name);

            fragment.appendChild(itemEl);
        }

        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;

        map.setBounds(bounds);
    }

    function getListItem(index, places) {
        var el = document.createElement('li'),
            itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                '<div class="info">' +
                '   <h5>' + places.place_name + '</h5>';

        if (places.road_address_name) {
            itemStr += '    <span>' + places.road_address_name + '</span>' +
                '   <span class="jibun gray">' +  places.address_name  + '</span>';
        } else {
            itemStr += '    <span>' +  places.address_name  + '</span>';
        }

        itemStr += '  <span class="tel">' + places.phone  + '</span>' +
            '</div>';

        el.innerHTML = itemStr;
        el.className = 'item';

        return el;
    }

    function addMarker(position, idx, title) {
        var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png',
            imageSize = new kakao.maps.Size(36, 37),
            imgOptions =  {
                spriteSize : new kakao.maps.Size(36, 691),
                spriteOrigin : new kakao.maps.Point(0, (idx*46)+10),
                offset: new kakao.maps.Point(13, 37)
            },
            markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
            marker = new kakao.maps.Marker({
                position: position,
                image: markerImage
            });

        marker.setMap(map);
        markers.push(marker);

        return marker;
    }

    function removeMarker() {
        for ( var i = 0; i < markers.length; i++ ) {
            markers[i].setMap(null);
        }
        markers = [];
    }

    function displayPagination(pagination) {
        var paginationEl = document.getElementById('pagination'),
            fragment = document.createDocumentFragment(),
            i;

        while (paginationEl.hasChildNodes()) {
            paginationEl.removeChild(paginationEl.lastChild);
        }

        for (i=1; i<=pagination.last; i++) {
            var el = document.createElement('a');
            el.href = "#";
            el.innerHTML = i;

            if (i===pagination.current) {
                el.className = 'on';
            } else {
                el.onclick = (function(i) {
                    return function() {
                        pagination.gotoPage(i);
                    }
                })(i);
            }

            fragment.appendChild(el);
        }
        paginationEl.appendChild(fragment);
    }

    function displayInfowindow(marker, title) {
        var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

        infowindow.setContent(content);
        infowindow.open(map, marker);
    }

    function removeAllChildNods(el) {
        while (el.hasChildNodes()) {
            el.removeChild(el.lastChild);
        }
    }
</script>


</body>
</html>