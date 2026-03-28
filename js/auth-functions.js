function togglePasswordVisibility(passwordId, iconId) {
    const passwordInput = document.getElementById(passwordId);
    const icon = document.getElementById(iconId);

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        if (icon) { icon.className = "ti ti-eye-off"; }
    } else {
        passwordInput.type = "password";
        if (icon) { icon.className = "ti ti-eye"; }
    }
}

document.addEventListener("DOMContentLoaded", function() {

    var closeButtons = document.querySelectorAll(".btn-close");

    closeButtons.forEach(function(button) {

        button.addEventListener("click", function() {

            var alert = this.closest(".alert");

            if (alert) {
                alert.style.display = "none";
            }
        });
    });
});