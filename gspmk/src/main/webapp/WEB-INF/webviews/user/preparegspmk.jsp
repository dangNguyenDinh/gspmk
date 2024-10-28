<%-- 
    Document   : preparegspmk
    Created on : Oct 18, 2024, 12:58:19 AM
    Author     : vdang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prepare Gspmk</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/prepare.css">
		<link rel="icon" href="${pageContext.request.contextPath}/static/images/icon.jpg" type="image/x-icon">
    </head>
    <body>
        <div class="container">
			<!-- Khu 1: Người dùng và lời chào -->
			<div id="header">
				<h1>Prepare Gspmk</h1>
				<h2 id="welcome-text">Hello ${sessionScope.user.username}</h2>
			</div>

			<!-- Khu 2: Danh sách online -->
			<div id="online-list">
				<h3>Online list</h3>
				<div id="list-online"></div>
			</div>

			<!-- Khu 3: Khu team hiện tại -->
			<div id="team-area">
				<h3 id="current-team">Current Team</h3>
				<button onclick="render_current_team()">Reload</button>
			</div>

			<!-- Khu 4: Khu sản phẩm -->
			<div id="product-list">
				<h2>List Product</h2>
				<div id="list-product"></div>
			</div>

			<!-- Khu 5: Khu giỏ hàng -->
			<div id="shopping-cart">
				<h2>Shopping Cart</h2>
				<div id="cart-items"></div>
			</div>
		</div>
		<script>
			var currentUser = document.getElementById("welcome-text").innerText.replace("Hello ", "");
			let socket;

			function load_online_user() {
				// Render những người dùng đang online ra màn hình (SSE)
				if (typeof (EventSource) !== "undefined") {
					const eventSource = new EventSource("${pageContext.request.contextPath}/utils/RenderOnlineUser");

					eventSource.onmessage = function (event) {
						const onlineList = JSON.parse(event.data); // onlineList bây giờ là Map<username, Users>
						const listOnlineDiv = document.getElementById("list-online");
						listOnlineDiv.innerHTML = "";

						// Tạo phần tử bảng
						const table = document.createElement("table");
						table.setAttribute("border", "1");

						// Tạo dòng tiêu đề của bảng
						const headerRow = document.createElement("tr");

						const userHeader = document.createElement("th");
						userHeader.innerText = "User";

						const sessionIdHeader = document.createElement("th");
						sessionIdHeader.innerText = "Action";

						// Thêm tiêu đề vào dòng tiêu đề
						headerRow.appendChild(userHeader);
						headerRow.appendChild(sessionIdHeader);

						// Thêm dòng tiêu đề vào bảng
						table.appendChild(headerRow);

						if (Object.keys(onlineList).length > 0) {
							for (const user in onlineList) {
								const row = document.createElement("tr");
								row.classList.add("online-row");

								// Cột username
								const userCell = document.createElement("td");
								userCell.innerText = user;
								userCell.classList.add("online-username");

								// Cột nút mời hoặc trạng thái
								const sessionIdCell = document.createElement("td");
								sessionIdCell.classList.add("online-action");

								if (onlineList[user].buyingSessionId === null) {
									console.log(onlineList[user].isAdmin);
									if (("" + onlineList[user].isAdmin) === "false") {
										sessionIdCell.innerHTML = '<button class="invite-button" onclick=\'invite_user(' + JSON.stringify(user) + ')\'>Invite</button>';
									}															
								} else {
									sessionIdCell.innerText = "Going spmk";
								}

								row.appendChild(userCell);
								row.appendChild(sessionIdCell);

								// Thêm dòng dữ liệu vào bảng
								table.appendChild(row);
							}
						} else {
							const emptyRow = document.createElement("tr");
							emptyRow.classList.add("online-empty-row");

							const emptyCell = document.createElement("td");
							emptyCell.colSpan = 2; // Cập nhật số cột
							emptyCell.innerText = "Everyone is gone.";
							emptyCell.classList.add("online-empty-cell");

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
			function load_products() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/user/RenderProductsView', true);
				xhr.setRequestHeader('Content-Type', 'application/json');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var products = JSON.parse(xhr.responseText);

						var tableContent = '<table class="product-table"><tr><th>ID</th><th>Name</th><th>Details</th><th>Action</th></tr>';
						for (var i = 0; i < products.length; i++) {
							tableContent += '<tr class="product-row">';
							tableContent += '<td class="product-id">' + products[i].id + '</td>';
							tableContent += '<td class="product-name">' + products[i].name + '</td>';
							tableContent += '<td class="product-action"><button class="details-button" onclick=\'render_popup_details_products(' + JSON.stringify(products[i]) + ')\'>Details</button></td>';
							tableContent += '<td class="product-action"><button class="add-cart-button" onclick=\'add_to_shopping_cart(' + JSON.stringify(products[i]) + ')\'>Add to cart</button></td>';
							tableContent += '</tr>';
						}
						tableContent += '</table>';
						document.getElementById('list-product').innerHTML = tableContent;
					} else {
						alert('Error loading products: ' + xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send();
			}


			function render_popup_details_products(product) {
				// Tạo nội dung của popup
				var popupContent =
						"<div id='overlay' style='position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); z-index:9999;'></div>" +
						"<div id='product-popup' style='position:fixed; top:10%; left:25%; width:50%; height:80%; background-color:#fff; border-radius:10px; padding:20px; z-index:10000; overflow-y:auto; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>" +
						"<button onclick='close_popup()' style='position:absolute; top:10px; right:20px; background:none; border:none; font-size:20px; cursor:pointer; color: black;'>&times;</button>" +
						"<h2>Chi tiết sản phẩm</h2>" +
						"<p><strong>Tên sản phẩm:</strong> " + product.name + "</p>" +
						"<p><strong>Giá:</strong> " + product.price + " USD</p>" +
						"<p><strong>Loại:</strong> " + product.type + "</p>" +
						"<p><strong>Mô tả:</strong> " + product.description + "</p>" +
						"<p><strong>Trạng thái:</strong> " + (product.readyState ? 'Sẵn sàng' : 'Không sẵn sàng') + "</p>" +
						"<p><img src='https://raw.githubusercontent.com/konanangel/prj_database/main/products/" + product.imageName + "' alt='" + product.name + "' style='width:100%; height:auto;'/></p>" +
						"</div>";


				// Chèn popup và overlay vào body
				document.body.insertAdjacentHTML('beforeend', popupContent);
			}

			function close_popup() {
				// Xóa popup và overlay khỏi DOM
				var popup = document.getElementById('product-popup');
				var overlay = document.getElementById('overlay');
				if (popup) {
					popup.remove();
				}
				if (overlay) {
					overlay.remove();
				}
			}

			function invite_user(user) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/SendInvitation', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var status = xhr.responseText;
						alert(status);
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				var data = "currentUser=" + currentUser + "&user=" + user;
				xhr.send(data);
			}

			function render_current_team() {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/RenderCurrentTeamWithBuyingSessionId', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						document.getElementById("current-team").innerHTML = xhr.responseText;
						if (document.getElementById("current-team").innerText === "Your team") {
							window.location.reload();
						}
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				var data = "currentUser=" + currentUser;
				xhr.send(data);
			}

			function render_destroy_buyingsession_button() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/utils/DestroyBuyingSession?currentUser=' + currentUser, true);

				xhr.onload = function () {
					if (xhr.status === 200) {
						if (xhr.responseText === "true") {
							document.getElementById("team-area").innerHTML += "<button onclick=\'destroy_buying_session()\'>End session</button>";
							document.getElementById("cart-items").insertAdjacentHTML('beforebegin', '<button onclick="start_gspmk()">Start GSPMK</button>');
						}
					}
				};
				xhr.onerror = function () {
					alert('Request failed');
				};
				xhr.send();
			}

			function destroy_buying_session() {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/DestroyBuyingSession', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						window.location.reload();
					}
				};
				xhr.onerror = function () {
					alert('Request failed');
				};
				var data = 'currentUser=' + currentUser;
				xhr.send(data);
			}

			function render_shopping_cart() {
				var currentBuyingSessionId = ${sessionScope.user.buyingSessionId};

				// Thiết lập SSE
				var eventSource = new EventSource('${pageContext.request.contextPath}/utils/RenderShoppingCartWithBuyingSessionId?buyingsessionid=' + currentBuyingSessionId);

				eventSource.onmessage = function (event) {
					// Parse dữ liệu JSON từ server
					var products = JSON.parse(event.data);

					// Tạo bảng hiển thị sản phẩm
					var tableContent = '<table class="product-display-table"><tr><th>ID</th><th>Name</th><th>Price</th><th>Action</th></tr>';

// Sử dụng vòng lặp for để duyệt qua danh sách sản phẩm
					for (var i = 0; i < products.length; i++) {
						var product = products[i];
						tableContent += '<tr class="product-display-row">' +
								'<td class="product-display-id">' + product.id + '</td>' +
								'<td class="product-display-name">' + product.name + '</td>' +
								'<td class="product-display-price">' + product.price + '</td>' +
								'<td class="product-display-action"><button class="remove-cart-button" onclick="remove_from_cart(' + currentBuyingSessionId + ', ' + i + ')">Xóa khỏi giỏ hàng</button></td>' +
								'</tr>';
					}

					tableContent += '</table>';
					document.getElementById('cart-items').innerHTML = tableContent;
				};

				eventSource.onerror = function () {
					console.log("SSE bị lỗi hoặc đã ngắt kết nối.");
					eventSource.close(); // Đóng EventSource nếu có lỗi
				};
			}

			function add_to_shopping_cart(product) {
				var currentBuyingSessionId = ${sessionScope.user.buyingSessionId};
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/AddToShoppingCart', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						//thông báo đã thêm vào giỏ hàng
						console.log("Đã thêm " + xhr.responseText);
					}
				};
				xhr.onerror = function () {
					alert('Request failed');
				};
				var data = 'id=' + product.id +
						'&name=' + encodeURIComponent(product.name) +
						'&price=' + product.price +
						'&type=' + encodeURIComponent(product.type) +
						'&description=' + encodeURIComponent(product.description) +
						'&imageName=' + encodeURIComponent(product.imageName) +
						'&sha=' + encodeURIComponent(product.sha) +
						'&readyState=' + product.readyState +
						'&currentBuyingSessionId=' + currentBuyingSessionId;
				xhr.send(data);

			}

			function remove_from_cart(buyingSessionId, index) {
				// Gửi yêu cầu xóa sản phẩm
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/RemoveFromShoppingCart', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				xhr.onload = function () {
					if (xhr.status === 200) {
					}
				};
				xhr.send('buyingsessionid=' + buyingSessionId + '&index=' + index);
			}

			function connectWebSocket() {
				socket = new WebSocket('ws://' + window.location.host + '${pageContext.request.contextPath}' + '/sockets/socket');
				socket.onmessage = function (event) {
					if (event.data === 'redirect') {
						window.location.href = '${pageContext.request.contextPath}' + '/user/WaitingForGSPMK';
						//bây giờ phải chuyển cái ongoing thành true để bịt lối cũ
						active_ongoing_in_this_buyingsession(currentUser);
					}
				};

				socket.onopen = function () {
					console.log('Kết nối WebSocket đã mở.');
				};

				socket.onclose = function () {
					console.log('Kết nối WebSocket đã đóng.');
				};

				socket.onerror = function (error) {
					console.error('Lỗi WebSocket:', error);
				};
			}

			function start_gspmk() {
				if (socket && socket.readyState === WebSocket.OPEN) {
					socket.send('start_gspmk'); // Gửi yêu cầu đến server
				}
				//xóa giỏ hàng, đẩy data xuống database
				handle_shopping_cart();
			}

			function active_ongoing_in_this_buyingsession(currentUser) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/ActiveOnGoingInBuyingSession', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				xhr.onload = function () {
					if (xhr.status === 200) {
						console.log("change!");
					}
				};
				xhr.send('currentUser=' + currentUser);
			}

			function handle_shopping_cart() {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/HandleShoppingCart', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var size = xhr.responseText;
						alert("Add " + size + " products to cart!");
					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				var data = "currentUser=" + currentUser;
				xhr.send(data);
			}

			// Gọi hàm khi trang được tải
			window.onload = function () {
				render_destroy_buyingsession_button();
				load_products();
				load_online_user();
				render_current_team();
				render_shopping_cart();
				connectWebSocket();
			};



		</script>
    </body>
</html>
