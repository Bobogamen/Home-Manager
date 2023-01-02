document.getElementById('resident').addEventListener('click', () => {
    let addResident = document.querySelector('.add-resident');

    showHideTag(addResident, document.getElementById('resident').checked);
})

function showHideTag(tag, checked) {
    if(checked)  {
        tag.style.display = 'none'
    } else {
        tag.style.display = 'block'
    }
}