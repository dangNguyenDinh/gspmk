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
        <title>Home - Admin</title>
    </head>
    <body>
		<% 
			String githubToken = application.getInitParameter("githubToken"); 
		%>
        <h1 id="welcome-text">Hello ${sessionScope.user.username}</h1>
		<h2>Admin</h2>
		<div>
			<form action="${pageContext.request.contextPath}/utils/logout">
				<button type="submit">logout</button>
			</form>
		</div>
		<hr>
		<div>
			<h2>All Users</h2>
			<button onclick="render_all_user()">Reload</button>
			<div id="all-user"></div>
		</div>
		<hr>	
		<div>
			<h2>list product</h2>
			<button onclick="render_popup_add_products()">Add</button>
			<div id="list-product"></div>
		</div>
		<hr>
		<div>
			<h2>Thống kê sản phẩm mua nhiều</h2>
			<label for="so_lieu">Số sản phẩm cần thống kê: </label>
			<input id="so_lieu" type="number"/>
			<button onclick="fetchAndDrawChart()">Reload</button>
			<div id="static"></div>
		</div>
		<style>
			canvas {
				border: 1px solid #000;
			}
		</style>
		<script>
			var githubToken = '<%= githubToken %>';
			var currentUser = document.getElementById("welcome-text").innerText.replace("Hello ", "");
			function render_all_user() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', "${pageContext.request.contextPath}/admin/utils/RenderAllUser", true);
				xhr.onreadystatechange = function () {
					if (xhr.readyState === 4 && xhr.status === 200) {
						var data = JSON.parse(xhr.responseText);
						var tableContent = '<table border="1"><tr><th>ID</th><th>Username</th><th>Password</th><th>Buying Session ID</th><th>Money Account</th><th>Is Admin</th></tr>';
						for (var i = 0; i < data.length; i++) {
							tableContent += '<tr>';
							tableContent += '<td>' + data[i].id + '</td>';
							tableContent += '<td>' + data[i].username + '</td>';
							tableContent += '<td>' + data[i].password + '</td>';
							tableContent += '<td>' + (data[i].buyingSessionId !== null ? data[i].buyingSessionId : 'null') + '</td>';
							tableContent += '<td>' + data[i].moneyAccount + '</td>';
							tableContent += '<td>' + (data[i].isAdmin ? 'Yes' : 'No') + '</td>';
							if (data[i].username !== currentUser) {
								tableContent += '<td><button onclick="delete_user(\'' + data[i].username + '\')">Delete</button></td>';
							}
							tableContent += '</tr>';
						}
						tableContent += '</table>';
						document.getElementById('all-user').innerHTML = tableContent;
					}
				};
				xhr.send();
			}
			//xóa user
			function delete_user(username) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${pageContext.request.contextPath}/admin/utils/DeleteUser', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						var user = xhr.responseText;
						alert("Delete user " + user + " sucessfully.");
						render_all_user();
					}
					;
				};
				xhr.onerror = function () {
					alert('Request failed');
				};

				xhr.send("username=" + username);

			}
			//load product
			function load_products() {
				var xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/admin/RenderProductsView', true);
				xhr.setRequestHeader('Content-Type', 'application/json');

				xhr.onload = function () {
					if (xhr.status === 200) {
						console.log(xhr.responseText)
						var products = JSON.parse(xhr.responseText);

						var tableContent = '<table border="1"><tr><th>ID</th><th>Name</th></tr>';
						for (var i = 0; i < products.length; i++) {
							tableContent += '<tr>';
							tableContent += '<td>' + products[i].id + '</td>';
							tableContent += '<td>' + products[i].name + '</td>';
							tableContent += '<td><button onclick=\'render_popup_details_products(' + JSON.stringify(products[i]) + ')\'>Details</button></td>';
							tableContent += '<td><button onclick=\'render_popup_edit_products(' + JSON.stringify(products[i]) + ')\'>Edit</button></td>';
							tableContent += '<td><button onclick=\'delete_products(' + JSON.stringify(products[i]) + ')\'>Delete</button></td>';
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
						"<button onclick='close_popup()' style='position:absolute; top:10px; right:20px; background:none; border:none; font-size:20px; cursor:pointer;'>&times;</button>" +
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

			function render_popup_edit_products(product) {
				var popupContent =
						"<div id='overlay' style='position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); z-index:9999;'></div>" +
						"<div id='product-popup' style='position:fixed; top:10%; left:25%; width:50%; height:80%; background-color:#fff; border-radius:10px; padding:20px; z-index:10000; overflow-y:auto; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>" +
						"<button onclick='close_popup()' style='position:absolute; top:10px; right:20px; background:none; border:none; font-size:20px; cursor:pointer;'>&times;</button>" +
						"<h2>Chỉnh sửa sản phẩm</h2>" +
						// Form cập nhật dữ liệu
						"<form id='update-data-form'>" +
						"<input type='hidden' id='product-id' name='id' value='" + product.id + "'/>" + // Thêm input ẩn cho ID
						"<label for='product-name'>Tên sản phẩm:</label>" +
						"<input type='text' id='product-name' name='name' value='" + product.name + "' required/><br><br>" +
						"<label for='product-price'>Giá:</label>" +
						"<input type='number' id='product-price' name='price' value='" + product.price + "' required/><br><br>" +
						"<label for='product-type'>Loại:</label>" +
						"<input type='text' id='product-type' name='type' value='" + product.type + "' required/><br><br>" +
						"<label for='product-description'>Mô tả:</label>" +
						"<textarea id='product-description' name='description' required>" + product.description + "</textarea><br><br>" +
						"<label for='product-readyState'>Trạng thái:</label>" +
						"<select id='product-readyState' name='readyState'>" +
						"<option value='1' " + (product.readyState ? "selected" : "") + ">Sẵn sàng</option>" +
						"<option value='0' " + (!product.readyState ? "selected" : "") + ">Không sẵn sàng</option>" +
						"</select><br><br>" +
						"<button onclick='submit_update_data(this)'>Cập nhật dữ liệu</button>" +
						"</form>"
						+
						// Form cập nhật ảnh
						"<hr><form id='update-image-form'>" +
						"<label for='product-image'>Ảnh sản phẩm:</label>" +
						"<input type='file' id='product-update-image' name='imageName' accept='image/*' onchange='previewImage(this)' required/><br><br>" +
						"<img id='image-preview' src='https://raw.githubusercontent.com/konanangel/prj_database/main/products/" + product.imageName + "' alt='Ảnh sản phẩm' style='max-width:100%; height:auto;'/><br><br>" +
						"<button type='button' onclick='submit_update_image(this, " + JSON.stringify(product.id) + ", " + JSON.stringify(product.imageName) + ", " + JSON.stringify(product.sha) + ")'>Cập nhật ảnh</button>" +
						"</form>" +
						"</div>";

				document.body.insertAdjacentHTML('beforeend', popupContent);
			}

			function previewImage(input) {
				// Kiểm tra xem có file được chọn hay không
				if (input.files && input.files[0]) {
					var file = input.files[0];
					var reader = new FileReader();

					// Kiểm tra nếu file là ảnh (Blob)
					if (file.type.match('image.*')) {
						reader.onload = function (e) {
							// Hiển thị preview ảnh
							document.getElementById('image-preview').src = e.target.result;
						};
						// Đọc file dưới dạng URL
						reader.readAsDataURL(file);
					} else {
						alert('Hãy chọn một tệp ảnh hợp lệ.');
					}
				}
			}

			function submit_update_image(button, id, oldImageName, sha) {
				var fileInput = document.getElementById('product-update-image');
				// Kiểm tra nếu input file trống (không có file được chọn)
				if (fileInput.files.length === 0) {
					alert('Vui lòng chọn ảnh trước khi cập nhật.');
					return;
				}

				// Bắt đầu tiến trình xóa ảnh cũ trên GitHub
				deleteImageFromGitHub(oldImageName, sha)
						.then(() => {
							// Sau khi xóa ảnh cũ thành công, chuẩn bị upload ảnh mới
							var imageInput = document.getElementById('product-update-image');
							var imageName = imageInput.files.length > 0 ? imageInput.files[0].name : '';

							// Nếu có ảnh, upload ảnh lên GitHub
							if (imageInput.files.length > 0) {
								var reader = new FileReader();

								// Xử lý khi ảnh được đọc xong dưới dạng base64
								reader.onload = function (event) {
									var imageBase64 = event.target.result.split(',')[1]; // Lấy phần dữ liệu base64 của ảnh

									// Gọi hàm upload ảnh lên GitHub
									uploadToGitHub(imageName, imageBase64)
											.then((newSha) => {
												if (typeof newSha !== 'undefined') {
													// Cập nhật tên ảnh và SHA mới vào database
													var xhr2 = new XMLHttpRequest();
													xhr2.open("POST", `${pageContext.request.contextPath}/admin/utils/UpdateImage`, true);
													xhr2.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

													xhr2.onreadystatechange = function () {
														if (xhr2.readyState === XMLHttpRequest.DONE) {
															close_popup();
															load_products();
														}
													};
													xhr2.send("id=" + id + "&name=" + imageName + "&sha=" + newSha);
												}
											})
											.catch((error) => {
												console.error("Lỗi khi upload ảnh lên GitHub:", error);
											});
								};

								// Đọc file ảnh dưới dạng base64
								reader.readAsDataURL(imageInput.files[0]);
							}
						})
						.catch((error) => {
							console.error("Lỗi khi xóa ảnh cũ trên GitHub:", error);
						});
			}


			function submit_update_data(button) {
				var form = button.form; // Lấy form từ button
				if (!form.checkValidity()) { // Kiểm tra tính hợp lệ của form
					alert('Vui lòng điền đầy đủ thông tin!'); // Hiển thị thông báo
					form.reportValidity(); // Hiển thị thông báo lỗi nếu cần
					return; // Dừng thực hiện nếu không hợp lệ
				}
				var form = button.closest('form'); // Lấy form chứa button
				var formData = new URLSearchParams(new FormData(form)).toString(); // Chuyển đổi dữ liệu form thành chuỗi "a=1&b=2"

				var xhr = new XMLHttpRequest();
				xhr.open("POST", `${pageContext.request.contextPath}/admin/utils/UpdateProduct`, true);
				xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

				xhr.onreadystatechange = function () {
					if (xhr.readyState === XMLHttpRequest.DONE) {
						if (xhr.status === 200) {
							alert("Cập nhật thành công!");
						} else {
							alert("Có lỗi xảy ra, vui lòng thử lại.");
						}
					}
				};

				xhr.send(formData); // Gửi dữ liệu
			}

			//thêm product
			function render_popup_add_products() {
				var popupContent =
						"<div id='overlay' style='position:fixed; top:0; left:0; width:100%; height:100%; background-color:rgba(0,0,0,0.5); z-index:9999;'></div>" +
						"<div id='product-popup' style='position:fixed; top:10%; left:25%; width:50%; height:80%; background-color:#fff; border-radius:10px; padding:20px; z-index:10000; overflow-y:auto; box-shadow: 0 4px 8px rgba(0,0,0,0.1);'>" +
						"<button onclick='close_popup()' style='position:absolute; top:10px; right:20px; background:none; border:none; font-size:20px; cursor:pointer;'>&times;</button>" +
						"<h2>Thêm sản phẩm mới</h2>" +
						// Form nhập dữ liệu sản phẩm mới
						"<form id='add-product-form'>" +
						"<label for='new-product-name'>Tên sản phẩm:</label>" +
						"<input type='text' id='new-product-name' name='name' required/><br><br>" +
						"<label for='new-product-price'>Giá:</label>" +
						"<input type='number' id='new-product-price' name='price' required/><br><br>" +
						"<label for='new-product-type'>Loại:</label>" +
						"<input type='text' id='new-product-type' name='type' required/><br><br>" +
						"<label for='new-product-description'>Mô tả:</label>" +
						"<textarea id='new-product-description' name='description' required></textarea><br><br>" +
						"<label for='new-product-readyState'>Trạng thái:</label>" +
						"<select id='new-product-readyState' name='readyState'>" +
						"<option value='1'>Sẵn sàng</option>" +
						"<option value='0'>Không sẵn sàng</option>" +
						"</select><br><br>" +
						"<label for='new-product-image'>Ảnh sản phẩm:</label>" +
						"<input type='file' id='new-product-image' name='imageName' accept='image/*' onchange='previewImage(this)'/><br><br>" +
						"<img id='image-preview' src='https://raw.githubusercontent.com/konanangel/prj_database/main/products/preview.png' alt='Ảnh xem trước' style='max-width:100%; height:auto;'/><br><br>" +
						"<button type='button' onclick='add_new_products(this)'>Thêm sản phẩm</button>" +
						"</form>" +
						"</div>";

				document.body.insertAdjacentHTML('beforeend', popupContent);
			}

			function add_new_products(button) {
				var form = button.form; // Lấy form từ button
				if (!form.checkValidity()) { // Kiểm tra tính hợp lệ của form
					alert('Vui lòng điền đầy đủ thông tin!'); // Hiển thị thông báo
					return; // Dừng thực hiện nếu không hợp lệ
				}
				var productName = document.getElementById('new-product-name').value;
				var productPrice = document.getElementById('new-product-price').value;
				var productType = document.getElementById('new-product-type').value;
				var productDescription = document.getElementById('new-product-description').value;
				var productReadyState = document.getElementById('new-product-readyState').value;
				var imageInput = document.getElementById('new-product-image');
				var imageName = imageInput.files.length > 0 ? imageInput.files[0].name : '';
				//tạo form bằng tay
				var formData = new FormData();
				formData.append('name', productName);
				formData.append('price', productPrice);
				formData.append('type', productType);
				formData.append('description', productDescription);
				formData.append('readyState', productReadyState);
				//thêm ảnh lên database

				// Nếu có ảnh, upload lên GitHub và thêm tên ảnh và sha vào formData
				if (imageInput.files.length > 0) {
					var reader = new FileReader();

					reader.onload = function (event) {
						var imageBase64 = event.target.result.split(',')[1]; // Lấy dữ liệu ảnh dưới dạng base64

						uploadToGitHub(imageName, imageBase64) // Gọi hàm upload ảnh lên GitHub
								.then((sha) => {
									if (typeof sha !== 'undefined') {
										// Thêm tên ảnh và sha vào formData
										formData.append('imageName', imageName);
										formData.append('sha', sha); // Thêm SHA vào FormData
										sendDataToServlet(formData); // Gửi dữ liệu tới servlet
									}

								})
								.catch((error) => {
								});
					};

					reader.readAsDataURL(imageInput.files[0]); // Đọc file ảnh dưới dạng base64
				}
			}

			function uploadToGitHub(fileName, base64String) {
				const token = githubToken;
				const owner = 'konanangel';
				const repo = 'prj_database';
				const path = 'products/' + fileName;
				const url = 'https://api.github.com/repos/' + owner + '/' + repo + '/contents/' + path;
				const message = 'Upload ' + fileName;

				const data = {
					message: message, // Thông điệp commit
					content: base64String // Chuỗi Base64 của ảnh
				};

				// Gửi request PUT đến GitHub API
				return fetch(url, {
					method: 'PUT',
					headers: {
						'Authorization': 'token ' + token, // Xác thực bằng token
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(data)  // Chuyển dữ liệu thành chuỗi JSON để gửi
				})
						.then(response => response.json())  // Chuyển kết quả về dạng JSON
						.then(result => {
							if (result.content) {
								alert('Thêm sản phẩm thành công!');  // Thông báo thành công
								close_popup();
								return result.content.sha; // Trả về SHA để thêm vào FormData
							} else {
								alert("Vui lòng đổi tên ảnh do đã có mặt hàng trùng tên ảnh.");  // Thông báo lỗi
								throw new Error('trùng ảnh.');
							}
						})
						.catch(error => {
							console.error('Error:', error);

						});
			}

			function sendDataToServlet(formData) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '/gspmk/admin/utils/AddProducts', true); // Chỉnh đường dẫn servlet

				// Thiết lập Content-Type để gửi dữ liệu dưới dạng URL-encoded
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

				xhr.onload = function () {
					if (xhr.status === 200) {
						load_products();  // Gọi hàm render lại danh sách sản phẩm
					} else {
						console.error('Lỗi khi thêm sản phẩm:', xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Yêu cầu thất bại, vui lòng thử lại.');
				};

				// Lấy dữ liệu từ FormData bằng .get()
				var name = formData.get('name');
				var type = formData.get('type');
				var description = formData.get('description');
				var price = formData.get('price');
				var readyState = formData.get('readyState');
				var imageName = formData.get('imageName');
				var sha = formData.get('sha');

				// Tạo chuỗi URL-encoded từ dữ liệu FormData
				var params = 'name=' + encodeURIComponent(name) +
						'&type=' + encodeURIComponent(type) +
						'&description=' + encodeURIComponent(description) +
						'&price=' + encodeURIComponent(price) +
						'&readyState=' + encodeURIComponent(readyState) +
						'&imageName=' + encodeURIComponent(imageName) +
						'&sha=' + encodeURIComponent(sha);

				// Gửi dữ liệu dưới dạng URL-encoded
				xhr.send(params);

			}

			//xoa products
			function delete_products(product) {
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '/gspmk/admin/utils/DeleteProducts', true);
				xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
				xhr.onload = function () {
					if (xhr.status === 200) {
						//xóa ảnh trong github
						deleteImageFromGitHub(product.imageName, xhr.responseText)
								.then(() => {
									alert('Xóa thành công!'); // Thông báo thành công
									load_products();
								});
					} else {
						console.error('Lỗi khi xóa sản phẩm:', xhr.statusText);
					}
				};

				xhr.onerror = function () {
					alert('Yêu cầu thất bại, vui lòng thử lại.');
				};

				// Gửi dữ liệu dưới dạng URL-encoded
				xhr.send("id=" + product.id);
			}

			function deleteImageFromGitHub(imagePath, sha) {
				const token = githubToken;
				const owner = 'konanangel';
				const repo = 'prj_database';
				const url = 'https://api.github.com/repos/' + owner + '/' + repo + '/contents/products/' + imagePath;
				const message = 'Xóa file ' + imagePath;

				const data = {
					message: message, // Thông điệp commit cho việc xóa
					sha: sha // SHA của file cần xóa
				};

				// Gửi request DELETE đến GitHub API
				return fetch(url, {
					method: 'DELETE',
					headers: {
						'Authorization': 'token ' + token, // Xác thực bằng token
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(data) // Chuyển dữ liệu thành chuỗi JSON để gửi
				})
						.then(response => response.json()) // Chuyển kết quả về dạng JSON
						.then(result => {
							if (result.commit) {

							} else {
								console.error('Error:', result);
								alert('Xóa thất bại.');
							}
						})
						.catch(error => {
							console.error('Error:', error);
						});
			}

			//biểu đồ
			function fetchAndDrawChart() {
				var so_lieu = document.getElementById("so_lieu").value;
				if(so_lieu === ""){
					so_lieu = "5";
				}
				const xhr = new XMLHttpRequest();
				xhr.open('GET', '${pageContext.request.contextPath}/admin/utils/GetStatic?so_lieu=' + so_lieu, true);
				xhr.onreadystatechange = function () {
					if (xhr.readyState === XMLHttpRequest.DONE) {
						if (xhr.status === 200) {
							const data = JSON.parse(xhr.responseText);
							const dataMap = new Map(Object.entries(data)); // Chuyển đổi JSON thành Map
							drawBarChart(dataMap);
						} else {
							console.log("Error fetching data:", xhr.statusText);
						}
					}
				};
				xhr.send();
			}

			function drawBarChart(dataMap) {
				// Tạo thẻ canvas
				const canvas = document.createElement('canvas');
				canvas.width = 900;
				canvas.height = 400;
				document.getElementById('static').innerHTML = "";
				document.getElementById('static').appendChild(canvas); // Thêm canvas vào div

				// Lấy ngữ cảnh vẽ
				const ctx = canvas.getContext('2d');

				// Chuyển đổi Map thành mảng và sắp xếp theo số lượt mua
				const sortedData = Array.from(dataMap.entries()).sort((a, b) => b[1] - a[1]);

				// Thiết lập các thông số cho biểu đồ
				const labels = sortedData.map(entry => entry[0]);
				const values = sortedData.map(entry => entry[1]);
				const barWidth = 50;
				const maxBarHeight = 300;
				const xStart = 50;

				// Vẽ biểu đồ
				ctx.clearRect(0, 0, canvas.width, canvas.height); // Xóa canvas trước khi vẽ

				// Vẽ cột
				for (let i = 0; i < values.length; i++) {
					const barHeight = (values[i] / Math.max(...values)) * maxBarHeight;
					const x = xStart + i * (barWidth + 20);
					const y = canvas.height - barHeight;

					ctx.fillStyle = '#4CAF50'; // Màu cột
					ctx.fillRect(x, y, barWidth, barHeight); // Vẽ cột

					// Vẽ nhãn dưới cột
					ctx.fillStyle = '#000'; // Màu chữ
					ctx.fillText(labels[i], x, canvas.height - 10);
				}
			}



			window.onload = () => {
				render_all_user();
				load_products();
				fetchAndDrawChart();

			};
		</script>
    </body>
</html>
