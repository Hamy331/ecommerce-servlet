<%@ page contentType="text/html" pageEncoding="UTF-8" %>
</main>
<script>
    // bật/tắt hiện mật khẩu khi bấm icon con mắt
    document.querySelectorAll(".toggle-password").forEach(function (btn) {
        btn.addEventListener("click", function () {
            var input = btn.previousElementSibling;
            var show = input.type === "password";
            input.type = show ? "text" : "password";
            btn.classList.toggle("on", show);
            btn.setAttribute("aria-label", show ? "Ẩn mật khẩu" : "Hiện mật khẩu");
        });
    });
</script>
</body>
</html>
