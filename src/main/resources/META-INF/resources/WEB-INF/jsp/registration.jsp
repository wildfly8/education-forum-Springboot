<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Create an account</title>
    <link href="${contextPath}/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/css/common.css" rel="stylesheet">
</head>

<body>
	<div class="container">
		<form:form method="POST" enctype="multipart/form-data" modelAttribute="userForm" class="form-signin">
			<h2 class="form-signin-heading">Create your account</h2>
			
			<div class="form-group ${status.error? 'has-error' : ''}">Username: <span style="color:green">(* required)</span>
				<form:input type="text" path="username" class="form-control" placeholder="Username" autofocus="true"></form:input>
				<form:errors path="username"></form:errors>
			</div>
			
			<br/>

			<div class="form-group ${status.error? 'has-error' : ''}">Email: <span style="color:green">(* required, needs to be verified)</span>
				<form:input type="email" path="email" class="form-control" placeholder="123@abc.com"></form:input>
				<form:errors path="email"></form:errors>
			</div>
			
			<br/>
			
			<div class="form-group ${status.error? 'has-error' : ''}">Password: <span style="color:green">(* required)</span>
				<form:input type="password" path="password" class="form-control" placeholder="Password"></form:input>
				<form:errors path="password"></form:errors>
			</div>
			
			<br/>

			<div class="form-group ${status.error? 'has-error' : ''}">Confirm Password: <span style="color:green">(* required)</span>
				<form:input type="password" path="passwordConfirm" class="form-control" placeholder="Confirm your password"></form:input>
				<form:errors path="passwordConfirm"></form:errors>
			</div>
			
			<br/>
			
			<div class="form-group ${status.error? 'has-error' : ''}">Icon: <br/>
				<img id="output" src="/images/avatar-display.png" style="width:100px; height:80px;"/><br/>
				<script>
  					var loadFile = function(event) {
    					var output = document.getElementById('output');
    					output.src = URL.createObjectURL(event.target.files[0]);
  					};
				</script>
				<form:input type="file" path="icon" accept="image/*" onChange="loadFile(event);" class="form-control"></form:input>
				<form:errors path="icon"></form:errors>
				<span style="color:green">(we only accept JPEG, JPG, PNG, GIF format files, and do NOT exceed 10 MB)</span>
			</div>
			
			<br/>			
			
			<div class="form-group ${status.error? 'has-error' : ''}">Signature:
				<form:input type="text" path="signature" class="form-control" placeholder="My Signature"  size="50"></form:input>
				<form:errors path="signature"></form:errors>
			</div>
			
			<br/>
			
			<div class="form-group ${status.error? 'has-error' : ''}">Check Below Category(s) of Interest:<br/><br/> 
				<form:checkboxes path="userCategories" items="${allCategories}" class="checkbox"/>
				<form:errors path="userCategories"></form:errors>
			</div>	
			
			<br/>		
			
			<%-- <div class="form-group ${status.error? 'has-error' : ''}">User Type:<br/>
				<form:select path="roles" class="form-control">
					<form:options items="${roles}" />
				</form:select>
				<form:errors path="roles"></form:errors>
			</div> --%>			

			<button class="btn btn-lg btn-primary btn-block" type="submit">Submit</button>
			
			<br/>
			
			<span style="color:green">(We'll send out a confirmation email after you register, you have to confirm via our link to login later.)</span>			
		</form:form>
	</div>
	
		<br/><br/>
		<div>
			<c:if test="${not empty errors}">
				<div style="color:red" align="center">
					<h3>Error(s):</h3>
					<c:forEach items="${errors}" var="error">
						<span>${error.getField()}</span><span>: </span><span>${error.getCode()}</span><br/>
					</c:forEach>
				</div>
			</c:if>
		</div>
		
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
