<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Document</title>

<style>
/* ��ü �׸��� ���̾ƿ� ���� */
html, body {
	height: 100%;
	margin: 0;
}

div {
	/* border: 1px solid black; */
	
}

.container {
	height: 100vh;
	display: grid;
	grid-template-rows: 90% 10%;
}
/* ���� ������ */
.bottom {
	display: flex;
	place-content: end;
}

label {
	place-content: center;
}
</style>

</head>
<body>
	<div class="container">
		<div class="top">
			<img alt="" src="resources/img/${f_name}"
				style="width: 100%; height: 100%;">
		</div>
		<div class="bottom">
			<label for="hideoneday" onclick="hide(${pu_id})">�Ϸ絿�� ���� �ʱ� X</label>
		</div>
	</div>
	<script type="text/javascript">
	
    function setCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = "expires=" + date.toUTCString();
        document.cookie = name + "=" + value + ";" + expires + ";path=/";
    }
    
    function hide(pu_id){
    	setCookie(pu_id,"pu_id",1);
    	window.close();
    }
    
    </script>
</body>
</html>