//check login hợp lệ
function check_login(event, form) {
	event.preventDefault();
	var xhr = new XMLHttpRequest();
	xhr.open('POST', 'validate/CheckExistedUsername', true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	xhr.onreadystatechange = () => {
		var checking_status = document.querySelector("#checking-state-login");
		if (xhr.status === 200) {
			if (xhr.readyState === 4) {
				if (xhr.responseText.includes("false")) {
					checking_status.innerHTML = "I have never met this man before.";
				} else {
					checking_status.innerHTML = "";
					event.target.form.submit();
				}
			}
		} else {
			checking_status.innerHTML = "checking";
		}
	};

	var data = `username=${form.username.value}&password=${form.password.value}`;
	xhr.send(data);
}

//check signup hợp lệ
function check_signup(event, form) {
	event.preventDefault();
	var xhr = new XMLHttpRequest();
	xhr.open('POST', 'validate/CheckExistedUsername', true);
	xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

	xhr.onreadystatechange = () => {
		var checking_status = document.querySelector("#checking-state-signup");
		if (xhr.status === 200) {
			if (xhr.readyState === 4) {
				if (xhr.responseText.includes("true")) {
					checking_status.innerHTML = "User existed.";
				} else {
					if(document.getElementById("password-signup").value === document.getElementById("repeatpassword-signup").value){
						checking_status.innerHTML = "";
						event.target.form.submit();
					}else{
						document.querySelector("#password-not-match").innerHTML = "Password not match.";
					}
				}
			}
		} else {
			checking_status.innerHTML = "validating";
		}
	};

	var data = `username=${form['username-signup'].value}&password=${form['password-signup'].value}`;
	xhr.send(data);
}

