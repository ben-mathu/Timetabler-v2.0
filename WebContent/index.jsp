<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="styling/style.css">
	
	<title>Insert title here</title>
</head>
<body class="body-container">
	<form class="form-signin" method="post">
		<div class="form-container">
			<div class="title text-center mb-4">
				<img class="mb-4" src="images/download.png" alt="" width="72" height="72">
				<h1 class="h3 mb-3 font-weight-normal">Floating labels</h1>
				<p>Build form controls with floating labels via the <code>:placeholder-shown</code> pseudo-element. <a href="https://caniuse.com/#feat=css-placeholder-shown">Works in latest Chrome, Safari, and Firefox.</a></p>
			</div>
			
			<div class="actual-form">
				<div class="form-label-group">
					<label for="inputEmail">Username</label>
				    <input type="email" id="inputUsername" name="username" class="form-control" placeholder="Email address" required="required">
				</div>
				
				<div class="form-label-group">
					<label for="inputPassword">Password</label>
					<input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required="required">
				</div>
				
				<div class="checkbox mb-3">
					<label>
					  <input type="checkbox" name="remember-me" value="remember-me"> Remember me
					</label>
				</div>
				<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
				<a class="forgot-password mt-5" href="#">Forgot Password?</a>
			</div><!-- End of actual-form -->
		</div><!-- End of form-container -->
		<p class="copy-right mt-5 mb-3 text-muted text-center">© 2017-2019</p>
	</form>
</body>
</html>