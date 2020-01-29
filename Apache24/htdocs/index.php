<!DOCTYPE html>
<html lang="kr">
<head>
<meta charset="UTF-8">

<script	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script> -->

<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">

<!-- 부가적인 테마 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">

<!-- 합쳐지고 최소화된 최신 자바스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<title>Insert title here</title>
</head>
<script>

$(document).ready(function(){
	
	var access_toeken = localStorage.getItem('access_token');
	var username = localStorage.getItem('userInfo');
	var expires_in = localStorage.getItem('expires_in');
	
	if(access_toeken) {
		document.getElementById('loginId').style.visibility ="hidden";
		
		$("#main").append('<h1> Hello. '+username+' 님 로그인 하였습니다.</h1>');
		
	}
	else {
		document.getElementById('logoutId').style.visibility ="hidden";
		
		$("#main").append('<h1> Hello, 비 로그인 상태 입니다. </h1>');
	}
	
	
	
	if(expires_in) {
		document.getElementById('tt').value = expires_in;
	}
	
});

function login() {
	
	var url = 'https://gluu.dsmcorps.com/oxauth/restv1/authorize?&scope=openid profile&response_type=code&client_id=023c3a02-6a97-4318-bec8-68444753aa61&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fauthorize%2Fcallback';
	
	location.href = url;
	
	alert('로그인 처리 중입니다.');
	
	};
	
function logout() {
	
	localStorage.removeItem('access_token');
	localStorage.removeItem('refresh_token');
	localStorage.removeItem('userInfo');
	localStorage.removeItem('expires_in');
	
	
	var logout_redirect_uri = 'http://localhost:8080/logout.php';
	var endSessionUrl = 'https://gluu.dsmcorps.com/oxauth/restv1/end_session?&post_logout_redirect_uri=' + logout_redirect_uri;
	location.href = endSessionUrl;
	
	alert('로그아웃 되었습니다.');
	};
	

function userInfo() {
		
	var access_token = localStorage.getItem('access_token');
	
	if(access_token) {
	
		  $.ajax({
			  type :'get'
			, url : '/oauth/userInfo'
			, cache : false
			, data : {accessToken : access_token
				      }
		    , contentType : "application/x-www-form-urlencoded"
			, success:function(json) {
				alert(JSON.stringify(json));
		    }
			,error:function(xhr,textStatus){
				alert("ERROR."+xhr);
			}
		}); 
	}
	else {
		alert('유효한 token이 없습니다.');
	}
		
};

function refresh() {
	
	var refresh_token = localStorage.getItem('refresh_token');
	
	if(refresh_token) {
	
		  $.ajax({
			  type :'get'
			, url : '/oauth/refreshAccessToken'
			, cache : false
			, data : {refreshToken : refresh_token
				      }
		    , contentType : "application/x-www-form-urlencoded"
			, success:function(json) {
				alert('토큰이 갱신되었습니다.<br>'+'origin = '+localStorage.getItem('access_token')+','+JSON.stringify(json));
				
				localStorage.setItem('access_token',json.response.access_token);
				localStorage.setItem('refresh_token',json.response.refresh_token);
				
		    }
			,error:function(xhr,textStatus){
				alert("ERROR."+xhr);
			}
		}); 
	}
	else {
		alert('유효한 token이 없습니다.');
	}
		
};

function resourceOwnerGrant() {

	  var username = document.querySelector('#username').value;
	  var password = document.querySelector('#password').value;

	  if (!username) {
	    alert('아이디를 입력하세요.');
	  }

	  if (!password) {
	    alert('패스워드를 입력하세요.');
	  }

	  
	  $.ajax({
		  type :'get'
		, url : '/oauth/resource'
		, cache : false
		, data : {username : username,
			      password : password
			      }
	    , contentType : "application/x-www-form-urlencoded"
		, success:function(json) {
			alert(JSON.stringify(json));
			
			localStorage.setItem('access_token',json.token_reponse.access_token);
			localStorage.setItem('refresh_token',json.token_reponse.refresh_token);
			localStorage.setItem('expires_in',json.token_reponse.expires_in);
			
	    }
		,error:function(xhr,textStatus){
			alert("ERROR."+xhr);
		}
	}); 
	  
};
</script>
<body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Gluu</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <form class="navbar-form navbar-right">
            <div class="form-group">
              <input type="text" id="tt" placeholder="time" class="form-control">
            </div>
            <input type="text" id="username" placeholder="user_id">
    		<input type="password" id="password" placeholder="password">
            <button onclick="javascript:login()" id="loginId" class="btn btn-success">Sign in</button>
            <button onclick="javascript:logout()" id="logoutId" class="btn btn-success">Sign out</button>
            <button onclick="javascript:resourceOwnerGrant()" id="passwdCD" class="btn btn-success">Password Credentials</button> 
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>

    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container" id="main">
      </div>
    </div>

    <div class="container">
      <!-- Example row of columns -->
      <div class="row">
        <div class="col-md-4">
          <h2>API Call</h2>
          <p>사용자 정보 조회는 여기를 선택하여 주세요. </p>
          <p><button class="btn btn-default" id="call_user" onclick="userInfo()" >View details &raquo;</button></p>
        </div>
        <div class="col-md-4">
          <h2>Refresh Call</h2>
          <p>새로운 Token을 발급 받으려면 여기를 선택하여 주세요. </p>
          <p><button class="btn btn-default" id="call_refresh" onclick="refresh()" >View details &raquo;</button></p>
       </div>
        <div class="col-md-4">
          <h2>Heading</h2>
          <p>Donec sed odio dui. Cras justo odio, dapibus ac facilisis in, egestas eget quam. Vestibulum id ligula porta felis euismod semper. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>
          <p><a class="btn btn-default" href="#" role="button">View details &raquo;</a></p>
        </div>
      </div>

      <hr>

      <footer>
        <p>&copy; New Company 2014</p>
      </footer>
    </div> <!-- /container -->
  </body>
</html>