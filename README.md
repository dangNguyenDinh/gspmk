# gspmk
trang chủ: 
- Hiển thị một list những sản phẩm sẵn có (hiển thị theo mục, từng mặt hàng có giá tiền, ghi chú thêm) 
- Đầu tiên là mặc định một số mục, có nút add và cập nhật lại database sau mỗi lần điền thêm. Chưa điền giá và chú thích mặt hàng điền thêm, khi nào mua về mới hỏi.
- Có nút để chọn mặt hàng.
- Có nút thông báo để hiển thị những gì người khác đã cập nhật.
- Có nút add vào giỏ hàng hiện tại (lưu vào Session)
- Có một nút bắt đầu đi ST (chuyển sang trang chờ xác nhận đi ST xong). 
- khi ấn nút: xuất hiện bảng người dùng để gửi lời mời đi cùng chỉ những người không trong phiên đi chợ. lời mời có thời hạn trong 5p kể từ khi người kia mở web. 
- Khi nút đó ấn tất cả những mặt hàng có trong Session sẽ được đẩy vào database, bảng hóa đơn dự tính để chốc so sánh với hóa đơn sau khi đi ST về)
//hành động đi siêu thị
- khi hành động đi siêu thị được xác nhận bởi mọi người, chương trình tạo ra một server tạo ra một “phiên đi siêu thị”. 
- phiên này chỉ được truy cập bởi những người được mời trong lần mời đó.
- đầu tiên sẽ xét xem người dùng có trong phiên này không, nếu có thì chuyển hướng tới trang cập nhật sau mỗi lần đi ST.
- cập nhật sau mỗi lần đi ST:
- khi người dùng đi về xong sẽ ra trang cập nhật: hỏi xem giá cả và mặt hàng có thay đổi gì không? nếu có thì chuyển hướng tới trang changepage.
- tính toán sau khi đi ST xong:
- user tạo phiên sẽ up ảnh hóa đơn và thống kê lại những món đồ được mua, thêm bớt dựa trên bảng hóa đơn dự tính. user đó thường là người bỏ tiền ra để trả hóa đơn hôm đó. 
- mặt hàng trong hóa đơn đó sẽ có lựa chọn người mua là ai. sau khi ấn tính toán, sẽ ra số tiền mà mỗi người thực sự cần trả.
- tiến hành ghi nợ.
- trong trang chủ sẽ có: tài khoản, thông báo, thông tin ghi nợ, nút bắt đầu tạo phiên đi ST. 
