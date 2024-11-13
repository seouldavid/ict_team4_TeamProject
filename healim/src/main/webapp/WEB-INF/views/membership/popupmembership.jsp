<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<script>
    //��Ű����    
    function setCookie(name, value, expiredays) {
        var todayDate = new Date();
        todayDate.setDate(todayDate.getDate() + expiredays);
        document.cookie = name + '=' + escape(value) + '; path=/; expires=' + todayDate.toGMTString() + ';'
    }

    // ��Ű �ҷ�����
    function getCookie(name) {
        var obj = name + "=";
        var x = 0;
        while (x <= document.cookie.length) {
            var y = (x + obj.length);
            if (document.cookie.substring(x, y) == obj) {
                if ((endOfCookie = document.cookie.indexOf(";", y)) == -1)
                    endOfCookie = document.cookie.length;
                return unescape(document.cookie.substring(y, endOfCookie));
            }
            x = document.cookie.indexOf(" ", x) + 1;

            if (x == 0) break;
        }
        return "";
    }

    // �ݱ� ��ư Ŭ����
    function closeWin(key) {
        var todaycloseyn = document.getElementById("todaycloseyn");
        if (todaycloseyn.checked) {
            setCookie('healimpopup' + key, 'Y', 1);
            window.close();
        }
    }

</script>

<style type="text/css">
    img {
        width: 400px;
        height: auto;
        display: block;
        margin: 0 auto;
        border-radius: 10px;
    }
    
    body{
     background-color: #F5F5F5; 
    }

    .button_area {
        position: fixed;
        bottom: 20px; /* ȭ���� �ϴܿ��� 20px ���� ��ġ */
        right: 20px; /* ȭ���� �����ʿ��� 20px ���ʿ� ��ġ */
        background-color: white;
        padding: 10px;
        border-radius: 10px;
        
    }

    .topper {
        position: fixed;
        top: 0px;
        left: 50%; /* ��� ������ ���� 50% */
        transform: translateX(-50%); /* �������� ���� �̵��Ͽ� ���߾����� */
        text-align: center; /* ��� ���� */
    }

    .topper img {
        width: 300px;
        height: auto;
        display: inline-block;
        margin-top: 10px;
    }

    .content {
        text-align: center; /* ���� ��� ���� */
        margin-top: 100px; /* �ΰ� �Ʒ��� ���� */
    }

    .content p {
        font-size: 18px;
        margin-top: 10px;
    }

</style>
</head>
<body>

<div>
    <div class="topper">
        <h1><a href="/home"><img src="/resources/img/main.png" alt="Healim Logo"></a></h1>
    </div>

    <div class="content">
        <p>����� ������ �Ϸ縦 �Բ��մϴ�.</p>
        <a href="#" target="_parent" onclick="move()">
            <img src="resources/img/111.jpg" alt="">
        </a>
    </div>

    <div class="button_area">
        <input class="window" type="checkbox" id="todaycloseyn" value="Y"> ���� �Ϸ� �� â�� ���� �ʽ��ϴ�.
        <a href='#' onclick="closeWin(1);"><button>�ݱ�</button></a>
    </div>
</div>
    

    <script>
        function move() {
            opener.move();
            self.close();
        }
    </script>

</body>
</html>