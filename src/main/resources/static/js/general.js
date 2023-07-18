let modal = document.getElementById("mar");
let modalBody = modal.querySelector('.modal-body').innerHTML;
let span = document.getElementById("close");
let modalTitle = document.querySelector("h5");
let modalTitleValue = document.querySelector("h5").textContent

span.onclick = () => xCLose();
window.onclick = (event) => outsideClickClose(event);

function setRawModalTitle(value) {
    modalTitle.textContent = value;
    modal.querySelector('.modal-body').innerHTML = modalBody;
}

function xCLose() {
    modal.style.display = "none";
    setRawModalTitle(modalTitleValue);
}

function outsideClickClose(event) {
    if (event.target === modal) {
        modal.style.display = "none";
        setRawModalTitle(modalTitleValue);
    }
}

function showCalendar(target) {
    const dateInput = target.nextElementSibling

    try {
        dateInput.nextElementSibling.showPicker();
    } catch (error) {
        console.log(error)
    }
}





