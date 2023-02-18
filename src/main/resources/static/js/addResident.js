function addResident(target) {
    modal.style.display = 'block'
    modalTitle.textContent += target.parentElement.parentElement.children[1].textContent

    let form = document.getElementById('add-resident');
    let formAction = form.attributes[1];

    formAction.value = formAction.value.replace('/home/', `/home${target.attributes[0].value}/`);
}