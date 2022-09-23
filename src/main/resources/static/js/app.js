let modal = document.getElementById('login');
// When the user clicks anywhere outside the modal, close it
window.onclick = function(event) {
    if (event.target === modal) {
        modal.style.display = "none";
    }
}