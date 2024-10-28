document.addEventListener("DOMContentLoaded", function () {
    const avatar = document.querySelector(".avatar");
    const dropdown = document.querySelector(".dropdown");

    // Hiện dropdown khi nhấp vào avatar
    avatar.addEventListener("click", function (event) {
        event.stopPropagation(); // Ngăn chặn sự kiện click lan ra ngoài
        dropdown.style.display = dropdown.style.display === "block" ? "none" : "block";
    });

    // Ẩn dropdown khi nhấp ra ngoài
    document.addEventListener("click", function () {
        dropdown.style.display = "none";
    });
});

