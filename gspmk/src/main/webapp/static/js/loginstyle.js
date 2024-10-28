window.onload = () => {
	// Lấy các phần tử cần thiết
	const loginBtn = document.getElementById('login-btn');
	const signupBtn = document.getElementById('signup-btn');
	const loginSection = document.getElementById('login-section');
	const signupSection = document.getElementById('signup-section');

// Hiển thị phần đăng nhập và ẩn phần đăng ký
	loginBtn.addEventListener('click', function () {
		loginSection.classList.add('visible');
		signupSection.classList.remove('visible');
	});

// Hiển thị phần đăng ký và ẩn phần đăng nhập
	signupBtn.addEventListener('click', function () {
		signupSection.classList.add('visible');
		loginSection.classList.remove('visible');
	});

}


