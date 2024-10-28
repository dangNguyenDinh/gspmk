<%-- 
    Document   : home
    Created on : Oct 13, 2024, 10:14:01 PM
    Author     : vdang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
		<link rel="icon" href="${pageContext.request.contextPath}/static/images/icon.jpg" type="image/x-icon">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css?v=2.0">
    </head>
    <body>
		<!-- Navbar -->
		<div class="navbar">
			<h2 id="welcome-text">Hello ${sessionScope.user.username}</h2>
			<form action="${pageContext.request.contextPath}/user/prepareGSPMK">
				<button id="gspmk" type="submit" onclick="active_buying_session_id('${sessionScope.user.username}')">GSPMK</button>
			</form>
			<div>
				<input type="text" id="search">
				<button id="button-search">SEARCH</button>
			</div>

			<div class="avatar-container">
				<img src="${pageContext.request.contextPath}/static/images/avatar.png" alt="Avatar" class="avatar">
				<div class="dropdown">
					<form action="${pageContext.request.contextPath}/utils/logout">
						<button type="submit" class="dropdown-btn">Logout</button>
					</form>
					<form action="${pageContext.request.contextPath}/utils/DeleteUser?user=${sessionScope.user.username}" method="POST">
						<button type="submit" class="dropdown-btn">Delete Account</button>
					</form>
					<button type="submit" class="dropdown-btn" id="change-pass" onclick="change_pass()">Change Password</button>
				</div>
			</div>
		</div>

		<!-- Main Container -->
		<div class="main-container">
			<!-- Online List -->
			<div class="sidebar" id="online-list">
				<h4>Online List</h4>
				<div id="list-online"></div>
			</div>

			<!-- Product List -->
			<div class="content" id="product-list">
				<h2>Product List</h2>
				<div id="list-product"></div>
				<div id="pagination" class="pagination-container"></div> <!-- Thêm class cho phân trang -->

			</div>

			<!-- Notification -->
			<div class="sidebar" id="notification-panel">
				<h3>Notification</h3>
				<div id="notification"></div>
			</div>
		</div>



		<script>

			var currentUser = document.getElementById("welcome-text").innerText.replace("Hello ", "");

			function change_pass() {
				const popupHTML = `
					<div id="popupProduct" style="
						display: block; 
						position: fixed; 
						top: 50%; 
						left: 50%; 
						transform: translate(-50%, -50%);
						background: #f9f9f9;
						padding: 20px 25px;
						border: 1px solid #ccc;
						border-radius: 8px;
						box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.15);
						z-index: 1000;
						width: 300px;">

						<form id="oldPasswordForm" style="margin-bottom: 15px;">
							<label style="
								display: block;
								font-size: 14px;
								color: #333;
								margin-bottom: 8px;
								font-weight: 500;">
								Enter Old Password:
							</label>
							<input type="password" id="oldPassword" required style="
								width: 100%;
								padding: 8px;
								margin-bottom: 15px;
								border: 1px solid #ccc;
								border-radius: 4px;
								box-sizing: border-box;
								font-size: 14px;">
							<button type="button" onclick="checkOldPassword()" style="
								width: 100%;
								padding: 10px;
								font-size: 14px;
								color: #fff;
								background-color: #007bff;
								border: none;
								border-radius: 4px;
								cursor: pointer;
								transition: background-color 0.3s;">
								Check
							</button>
						</form>

						<form id="newPasswordForm" style="display: none;">
							<label style="
								display: block;
								font-size: 14px;
								color: #333;
								margin-bottom: 8px;
								font-weight: 500;">
								Enter New Password:
							</label>
							<input type="password" id="newPassword" required style="
								width: 100%;
								padding: 8px;
								margin-bottom: 15px;
								border: 1px solid #ccc;
								border-radius: 4px;
								box-sizing: border-box;
								font-size: 14px;">

							<label style="
								display: block;
								font-size: 14px;
								color: #333;
								margin-bottom: 8px;
								font-weight: 500;">
								Confirm New Password:
							</label>
							<input type="password" id="confirmPassword" required style="
								width: 100%;
								padding: 8px;
								margin-bottom: 15px;
								border: 1px solid #ccc;
								border-radius: 4px;
								box-sizing: border-box;
								font-size: 14px;">

							<button type="button" onclick="changePassword()" style="
								width: 100%;
								padding: 10px;
								font-size: 14px;
								color: #fff;
								background-color: #007bff;
								border: none;
								border-radius: 4px;
								cursor: pointer;
								transition: background-color 0.3s;">
								Change Password
							</button>
						</form>
					</div>

					<div id="popupOverlay" style="
						position: fixed;
						top: 0;
						left: 0;
						width: 100%;
						height: 100%;
						background: rgba(0, 0, 0, 0.5);
						z-index: 999;" onclick="close_popup()">
					</div>
				`;


				// Thêm popup vào body
				document.body.insertAdjacentHTML('beforeend', popupHTML);
			}


			function checkOldPassword() {
				const xhr = new XMLHttpRequest();
				xhr.open("POST", "${pageContext.request.contextPath}/validate/CheckPasswordChange", true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

				const username = currentUser;
				const password = document.getElementById("oldPassword").value;

				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4 && xhr.status === 200) {
						if (xhr.responseText === "true") { // Mật khẩu cũ đúng
							document.getElementById("oldPasswordForm").style.display = "none";
							document.getElementById("newPasswordForm").style.display = "block";
						} else {
							alert("Mật khẩu cũ không đúng.");
						}
					}
				};

				// Gửi dữ liệu username và password
				xhr.send("username=" + encodeURIComponent(username) + "&password=" + encodeURIComponent(password));
			}

			function changePassword() {
				const username = currentUser;
				const newPassword = document.getElementById("newPassword").value;
				const confirmPassword = document.getElementById("confirmPassword").value;

				// Kiểm tra xem mật khẩu mới và xác nhận mật khẩu có trùng nhau không
				if (newPassword !== confirmPassword) {
					alert("Mật khẩu mới không trùng khớp.");
					return;
				}

				// Tạo XMLHttpRequest để gửi dữ liệu đến servlet ChangePassword
				const xhr = new XMLHttpRequest();
				xhr.open("POST", "${pageContext.request.contextPath}/validate/ChangePassword", true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4 && xhr.status === 200) {
						if (xhr.responseText === "success") {
							alert("Đổi mật khẩu thành công.");
							close_popup(); // Đóng popup sau khi đổi mật khẩu thành công
						} else {
							alert("Đổi mật khẩu không thành công. Vui lòng thử lại.");
						}
					}
				};

				// Gửi dữ liệu username và newPassword đến servlet
				xhr.send("username=" + encodeURIComponent(username) + "&newPassword=" + encodeURIComponent(newPassword));
			}


			function load_online_user() {
				// Render những người dùng đang online ra màn hình (SSE)
				if (typeof (EventSource) !== "undefined") {
					const eventSource = new EventSource("${pageContext.request.contextPath}/utils/RenderOnlineUser");

					eventSource.onmessage = function (event) {
						const onlineList = JSON.parse(event.data); // onlineList là Map<username, Users>
						const listOnlineDiv = document.getElementById("list-online");
						listOnlineDiv.innerHTML = "";

						// Tạo phần tử bảng với id và class
						const table = document.createElement("table");
						table.setAttribute("id", "online-users-table");
						table.classList.add("table-styled");

						// Tạo dòng tiêu đề của bảng
						const headerRow = document.createElement("tr");
						headerRow.classList.add("header-row");

						const userHeader = document.createElement("th");
						userHeader.innerText = "User";
						userHeader.classList.add("header-cell");

						// Thêm tiêu đề vào dòng tiêu đề
						headerRow.appendChild(userHeader);

						// Thêm dòng tiêu đề vào bảng
						table.appendChild(headerRow);

						if (Object.keys(onlineList).length > 0) {
							for (const user in onlineList) {
								const row = document.createElement("tr");
								row.classList.add("data-row");

								// Cột username
								const userCell = document.createElement("td");
								userCell.innerText = user;
								userCell.classList.add("data-cell");

								row.appendChild(userCell);

								// Thêm dòng dữ liệu vào bảng
								table.appendChild(row);
							}
						} else {
							const emptyRow = document.createElement("tr");
							emptyRow.classList.add("empty-row");

							const emptyCell = document.createElement("td");
							emptyCell.colSpan = 1;
							emptyCell.innerText = "Everyone is gone.";
							emptyCell.classList.add("empty-cell");

							emptyRow.appendChild(emptyCell);
							table.appendChild(emptyRow);
						}

						// Thêm bảng vào div
						listOnlineDiv.appendChild(table);
					};
				} else {
					document.getElementById("list-online").innerText = "SSE not supported in this browser.";
				}
			}

			//render sản phẩm đang có (non SSE)
			var currentPage = 0; // Biến để theo dõi trang hiện tại
			var productsPerPage = 5; // Số sản phẩm trên mỗi trang
			var products = []; // Mảng chứa tất cả sản phẩm

			function searchProducts(findstr) {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/user/RenderProductsView', true); // Thay URL phù hợp với ứng dụng
				xhr.setRequestHeader('Content-Type', 'application/json');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var allProducts = JSON.parse(xhr.responseText); // Lấy tất cả sản phẩm từ phản hồi

						// Gọi searchWithoutDiacritics để lọc danh sách sản phẩm theo tên
						var result = searchWithoutDiacritics(findstr, allProducts.map(product => product.name));

						// Lọc danh sách sản phẩm dựa trên kết quả từ searchWithoutDiacritics
						products = allProducts.filter(function (product) {
							return result.includes(product.name);
						});

						// Gọi lại các hàm render ban đầu
						render_products();
						render_pagination();
					} else {
						alert('Error loading products: ' + xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send();
			}

			// Hàm tìm kiếm không dấu
			function searchWithoutDiacritics(findstr, results) {
				// Chuẩn hóa chuỗi: xóa dấu và chuyển về chữ thường
				function normalize(input) {
					return input.normalize("NFD").toLowerCase().replace(/[\u0300-\u036f]/g, "");
				}

				// Hàm kiểm tra nếu chuỗi result khớp với findstr
				function isMatch(findstr, result) {
					let a1 = 0, a2 = 0;
					while (a1 < findstr.length && a2 < result.length) {
						if (findstr[a1] === result[a2]) {
							a1++;
							a2++;
						} else {
							a2 = result.indexOf(findstr[a1], a2 + 1);
							if (a2 === -1)
								return false;
						}
					}
					return a1 === findstr.length;
				}

				// Chuẩn hóa findstr
				const normalizedFindstr = normalize(findstr);
				// Lọc và trả về các kết quả khớp
				return results.filter(result => isMatch(normalizedFindstr, normalize(result)));
			}

			function load_selected_products(selectedNames) {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/user/RenderProductsView', true); // Sửa URL phù hợp với ứng dụng
				xhr.setRequestHeader('Content-Type', 'application/json');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var allProducts = JSON.parse(xhr.responseText); // Lấy tất cả sản phẩm từ phản hồi

						// Lọc những sản phẩm có tên trong selectedNames
						products = allProducts.filter(function (product) {
							return selectedNames.includes(product.name);
						});

						// Gọi lại các hàm render ban đầu
						render_products();
						render_pagination();
					} else {
						alert('Error loading products: ' + xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send();
			}

			function load_products() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/user/RenderProductsView', true); // Sửa URL để không dùng 
				xhr.setRequestHeader('Content-Type', 'application/json');

				xhr.onload = function () {
					if (xhr.status === 200) {
						products = JSON.parse(xhr.responseText); // Lưu tất cả sản phẩm vào biến
						render_products(); // Gọi hàm render để hiển thị sản phẩm
						render_pagination(); // Gọi hàm phân trang
					} else {
						alert('Error loading products: ' + xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send();
			}

			function render_products() {
				var start = currentPage * productsPerPage;
				var end = start + productsPerPage;
				var displayProducts = products.slice(start, end); // Lấy các sản phẩm cho trang hiện tại

				var productContainer = '<div class="product-container">'; // Thay đổi thành div để chứa sản phẩm
				for (var i = 0; i < displayProducts.length; i++) {
					productContainer += '<div class="product-item">'; // Thêm class cho từng sản phẩm
					productContainer += '<div class="product-image"><img src="https://raw.githubusercontent.com/konanangel/prj_database/main/products/' + displayProducts[i].imageName + '" alt="' + displayProducts[i].name + '"/></div>'; // Đóng dấu nháy cho alt

					productContainer += '<h3 class="product-name">' + displayProducts[i].name + '</h3>';
					productContainer += '<button class="details-button" onclick=\'render_popup_details_products(' + JSON.stringify(displayProducts[i]) + ')\'>Details</button>';
					productContainer += '</div>'; // Đóng div sản phẩm
				}

				productContainer += '</div>'; // Đóng div chứa sản phẩm

				document.getElementById('list-product').innerHTML = productContainer; // Chèn vào DOM
			}

			function render_pagination() {
				var paginationContainer = '<div class="pagination">';
				var totalPages = Math.ceil(products.length / productsPerPage); // Tính tổng số trang

				for (var i = 0; i < totalPages; i++) {
					paginationContainer += '<button class="pagination-button" onclick="goToPage(' + i + ')">' + (i + 1) + '</button>'; // Thêm nút cho mỗi trang
				}
				paginationContainer += '</div>';

				document.getElementById('pagination').innerHTML = paginationContainer; // Chèn vào DOM
			}

			function goToPage(pageNumber) {
				currentPage = pageNumber; // Cập nhật trang hiện tại
				render_products(); // Gọi lại hàm để hiển thị sản phẩm
			}

			function render_popup_details_products(product) {
				// Tạo nội dung của popup
				var popupContent =
						"<div id='popupOverlay' class='popup-overlay'></div>" +
						"<div id='popupProduct' class='popup-product'>" +
						"<button onclick='close_popup()' class='popup-close-button'>&times;</button>" +
						"<div class='popup-product-details'>" +
						"<h2>Chi tiết sản phẩm</h2><br>" +
						"<p><strong>Tên sản phẩm: </strong> " + product.name + "</p>" +
						"<p><strong>Giá: </strong> " + product.price + " USD</p>" +
						"<p><strong>Loại: </strong> " + product.type + "</p>" +
						"<p><strong>Mô tả: </strong> " + product.description + "</p>" +
						"<p><strong>Trạng thái: </strong> " + (product.readyState ? 'Sẵn sàng' : 'Không sẵn sàng') + "</p>" +
						"</div>" +
						"<div class='popup-product-image'><br><br>" +
						"<img src='https://raw.githubusercontent.com/konanangel/prj_database/main/products/" + product.imageName + "' alt='" + product.name + "' />" +
						"</div>" +
						"</div>";

				// Chèn popup và overlay vào body
				document.body.insertAdjacentHTML('beforeend', popupContent);
			}

			function close_popup() {
				// Xóa popup và overlay khỏi DOM
				var popup = document.getElementById('popupProduct');
				var overlay = document.getElementById('popupOverlay');
				if (popup) {
					popup.remove();
				}
				if (overlay) {
					overlay.remove();
				}
			}

			function load_notification() {
				// Render những thông báo ra màn hình (SSE)
				if (typeof (EventSource) !== "undefined") {
					const currentUser = document.getElementById("welcome-text").innerText.replace("Hello ", "");
					const eventSource = new EventSource("${pageContext.request.contextPath}/utils/RenderNotification?currentUser=" + currentUser);

					eventSource.onmessage = function (event) {
						const notifications = JSON.parse(event.data); // notifications bây giờ là mảng các lời mời
						const notificationDiv = document.getElementById("notification");
						notificationDiv.innerHTML = ""; // Xóa nội dung cũ trước khi thêm mới

						// Tạo phần tử bảng
						const table = document.createElement("table");
						table.className = "notification-table"; // Thêm class cho bảng

						// Tạo dòng tiêu đề của bảng
						const headerRow = document.createElement("tr");
						headerRow.className = "notification-header"; // Thêm class cho dòng tiêu đề

						const fromHeader = document.createElement("th");
						fromHeader.innerText = "From";

						const actionHeader = document.createElement("th");
						actionHeader.innerText = "Action";

						// Thêm tiêu đề vào dòng tiêu đề
						headerRow.appendChild(fromHeader);
						headerRow.appendChild(actionHeader);

						// Thêm dòng tiêu đề vào bảng
						table.appendChild(headerRow);

						if (notifications.length > 0) {
							notifications.forEach(function (notification) {
								const row = document.createElement("tr");
								row.className = "notification-row"; // Thêm class cho dòng thông báo

								// Cột người gửi
								const fromCell = document.createElement("td");
								fromCell.className = "notification-from"; // Thêm class cho cột người gửi
								fromCell.innerText = notification.from;

								// Hành động
								const actionCell = document.createElement("td");
								actionCell.className = "notification-action"; // Thêm class cho cột hành động
								actionCell.innerHTML = `<button class="accept-button" onclick='accept_invitation(` + JSON.stringify(notification.from) + `)'>Accept</button>`;

								row.appendChild(fromCell);
								row.appendChild(actionCell);
								// Thêm dòng dữ liệu vào bảng
								table.appendChild(row);
							});
						} else {
							const emptyRow = document.createElement("tr");
							const emptyCell = document.createElement("td");
							emptyCell.colSpan = 2; // Cập nhật số cột ở đây
							emptyCell.className = "notification-empty"; // Thêm class cho ô trống
							emptyCell.innerText = "Không có thông báo nào.";
							emptyRow.appendChild(emptyCell);

							table.appendChild(emptyRow);
						}

						// Thêm bảng vào div
						notificationDiv.appendChild(table);
					};
				} else {
					document.getElementById("notification").innerText = "SSE not supported in this browser.";
				}
			}

			// Gọi hàm khi trang được tải
			window.onload = function () {
				load_products();
				load_notification();
				load_online_user();
				document.getElementById('search').addEventListener('input', function () {
					var findstr = this.value; // Lấy giá trị từ ô input
					searchProducts(findstr); // Gọi hàm tìm kiếm với giá trị này
				});

				document.getElementById('button-search').addEventListener('click', function () {
					var findstr = document.getElementById('search').value; // Lấy giá trị từ ô input
					searchProducts(findstr); // Gọi hàm tìm kiếm với giá trị này
				});
			};

			function active_buying_session_id(username) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/ActiveBuyingSessionId', true);
				xhr.onload = function () {
					if (xhr.status === 200) {
						var buyingSessionId = xhr.responseText;
						active_new_shopping_cart(buyingSessionId);

					}
					;
				};
				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send(username);
				alert("You are preparing to gspmk.");
			}

			function accept_invitation(user) {
				//set cho buyingSessionId của người accept bằng với buyingSessionId của người mời
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/AddBuyingSessionId', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var status = xhr.responseText;
						alert(status);
						window.location.href = "${pageContext.request.contextPath}/user/prepareGSPMK";
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				var data = "currentUser=" + currentUser + "&user=" + user;
				xhr.send(data);
			}

			function active_new_shopping_cart(buyingSessionId) {
				//hàm này là tạo mới một giỏ hàng
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/ActiveNewShoppingCart', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				xhr.onload = function () {
					if (xhr.status === 200) {
						//làm gì đó tùy


					}
					;
				};
				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send("buyingSessionId=" + buyingSessionId);
			}

		</script>
		<script src="${pageContext.request.contextPath}/static/js/home/homestyle.js?v=1.0"></script>
    </body>
</html>
