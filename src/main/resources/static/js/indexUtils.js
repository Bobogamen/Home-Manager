document.getElementById('registerBtn').addEventListener('click', register);

function register() {
    location.href = '/register';
}

setTimeout(() => {
    document.querySelector('.notificationLogin').style.display = 'none';
}, 6000);





















