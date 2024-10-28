<%-- 
    Document   : waitingforgspmk
    Created on : Oct 23, 2024, 12:57:54 PM
    Author     : vdang
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>waiting for gspmk</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/waiting.css">
		<link rel="icon" href="${pageContext.request.contextPath}/static/images/icon.jpg" type="image/x-icon">
    </head>
    <body>
        <div class="container">
			<div class="left">
				<div class="header">
					<h1>Waiting for GSPMK</h1>
					<h2 id="welcome-text">Hello ${sessionScope.user.username}</h2>
					<h2 id="SSID">SSID: ${sessionScope.user.buyingSessionId}</h2>
				</div><hr>
				<div>
					<h2>Message</h2>
					<div id="message">
						<div id="chat-box"></div>
					</div>
					<input type="text" id="chat-input"/> 
					<button onclick="sendMessage()">Send</button>
				</div>
			</div>

			<div class="right">
				<h2>Pretending Cart</h2>
				<div id="pretending-cart"></div>
			</div>
		</div>

		<script>
			var currentUser = document.getElementById("welcome-text").innerText.replace("Hello ", "");


			function render_destroy_buyingsession_button() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/utils/DestroyBuyingSession?currentUser=' + currentUser, true);

				xhr.onload = function () {
					if (xhr.status === 200) {
						if (xhr.responseText === "true") {
							document.getElementById("SSID").insertAdjacentHTML('afterend', "<button onclick=\'destroy_buying_session()\'>End session</button>");
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
						window.location.href = '${pageContext.request.contextPath}' + '/user/home';
					}
				};
				xhr.onerror = function () {
					alert('Request failed');
				};
				var data = 'currentUser=' + currentUser;
				xhr.send(data);
			}

			function render_cart() {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/utils/RenderCart', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var response = xhr.responseText;
						var cartItems = response.split(';').filter(item => item.trim() !== ''); // Tách từng sản phẩm

						var totalOrderPrice = 0;
						var cartHtml = '<table class="cart-table"><tr><th>Ảnh</th><th>Tên sản phẩm</th><th>Số lượng</th><th>Giá</th><th>Tổng giá</th></tr>';

						cartItems.forEach(function (item) {
							var productData = item.split(','); // Tách thông tin từng sản phẩm
							var productName = productData[0];
							var imageName = productData[1];
							var quantity = parseInt(productData[2]);
							var price = parseFloat(productData[3]);
							var totalProductPrice = price * quantity;

							// Tính tổng đơn giá
							totalOrderPrice += totalProductPrice;

							// Tạo HTML dạng bảng cho mỗi sản phẩm
							cartHtml += '<tr>';
							cartHtml += '<td><img class="product-image" src="https://raw.githubusercontent.com/konanangel/prj_database/main/products/' + imageName + '" alt="' + productName + '"/></td>';
							cartHtml += '<td>' + productName + '</td>';
							cartHtml += '<td>' + quantity + '</td>';
							cartHtml += '<td>' + price.toFixed(2) + ' VND</td>';
							cartHtml += '<td>' + totalProductPrice.toFixed(2) + ' VND</td>';
							cartHtml += '</tr>';
						});

// Thêm tổng đơn giá vào cuối bảng
						cartHtml += '<tr class="total-row"><td colspan="4"><strong>Tổng đơn giá</strong></td><td><strong>' + totalOrderPrice.toFixed(2) + ' VND</strong></td></tr>';
						cartHtml += '</table>';

// Chèn nội dung bảng vào phần tử có id 'cart'
						document.getElementById('pretending-cart').innerHTML = cartHtml;


					}
				};

				xhr.onerror = function () {
					alert('Request failed');
				};

				var data = "currentUser=" + currentUser;
				xhr.send(data);
			}

			function sendMessage() {
				const username = currentUser; // ID của input username
				const content = document.getElementById("chat-input").value;
				if(content !== ""){
					// Tạo một đối tượng XMLHttpRequest
				const xhr = new XMLHttpRequest();
				xhr.open("POST", '${pageContext.request.contextPath}/utils/send', true); // Thiết lập yêu cầu POST đến SendMessage
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

				// Xử lý phản hồi từ server
				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4 && xhr.status === 200) {
						console.log('Message sent successfully!');
						document.getElementById("chat-input").value = ""; // Xóa ô nhập
					} else if (xhr.readyState === 4) {
						console.error('Failed to send message.');
					}
				};

				// Gửi dữ liệu
				xhr.send("username=" + encodeURIComponent(username) +
						"&content=" + encodeURIComponent(content));
				}
				
			}

			function render_message() {
				const chatBox = document.getElementById('chat-box');
				// Khởi tạo kết nối SSE
				const eventSource = new EventSource('${pageContext.request.contextPath}/utils/render?currentUser=' + currentUser);

				// Xử lý dữ liệu nhận được
				eventSource.onmessage = function (event) {
					chatBox.innerHTML = "";
					const messages = JSON.parse(event.data);
					messages.forEach(function (msg) {
						const messageElement = document.createElement('div');
						messageElement.classList.add('chat-message'); // Thêm class cho tin nhắn
						messageElement.textContent = msg.from + ': ' + msg.content; // Sử dụng phép nối chuỗi
						chatBox.appendChild(messageElement);
					});
					// Tự động cuộn xuống cuối chat box
					chatBox.scrollTop = chatBox.scrollHeight;
				};
			}


			window.onload = () => {
				render_destroy_buyingsession_button();
				render_cart();
				render_message();
			};
		</script>

	</body>
</html>
