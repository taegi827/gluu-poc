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
	
	 var url = window.location.href;
	 localStorage.setItem('url',url);
	 var v_code = url.match('[?&]code=([^&]*)');
	 var c_code = v_code[1];
	  $.ajax({
		  type :'get'
		, url : '/oauth/token'
		, cache : false
		, data : {scope : 'openid profile',
			      code: c_code
			      }
	    , contentType : "application/x-www-form-urlencoded"
		, success:function(json) {
			//alert("access_token:"+json.token_reponse.access_token+", refresh_token:"+json.token_reponse.refresh_token
			//		+"expires_in: "+json.token_reponse.expires_in);
			alert(JSON.stringify(json));
			
			localStorage.setItem('access_token',json.token_reponse.access_token);
			localStorage.setItem('refresh_token',json.token_reponse.refresh_token);
			localStorage.setItem('userInfo',json.access_token_decoding.name);
			localStorage.setItem('expires_in',json.token_reponse.expires_in);
			
			location.href = '/index.php';
	    }
		,error:function(xhr,textStatus){
			alert("ERROR."+xhr);
		}
	}); 
	
})


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
            <button onclick="login()" id="loginId" class="btn btn-success">Sign in</button>
          </form>
        </div><!--/.navbar-collapse -->
      </div>
    </nav>

<!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container">
        <h1>Waiting...</h1>
      </div>
    </div>


</body>
</html>