let modal = document.getElementById("mar");
let span = document.getElementById("close");
let modalTitle = document.querySelector("h5");
let modalTitleValue = document.querySelector("h5").textContent

function setRawModalTitle(value) {
    modalTitle.textContent = value;
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
