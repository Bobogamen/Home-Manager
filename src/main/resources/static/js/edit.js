function editResident(target) {
    clearAll();

    let buttonDiv = target.querySelector('span')

    target.parentElement.parentElement.querySelectorAll('button').forEach(b => {
        if (b.id === buttonDiv.children[0].id) {
            buttonDiv.className = 'd-inline float-end';
        } else {
            b.parentElement.className = 'd-none';
        }
    });
}

let formDiv = document.querySelector('.edit-times');
let form = formDiv.querySelector('form');
let basicUrl = form.action;

function editFeeTimes(target) {
    clearAll();

    target.style.backgroundColor = 'slategray';

    form.action = basicUrl + target.id;

    let input = form.querySelector('.inputNumber');
    input.value = Number(target.textContent);

    document.getElementById('fees').appendChild(formDiv);
    formDiv.style.display = 'flex'
}

function clearAll() {
    document.querySelectorAll('#button').forEach(b => b.className = 'd-none')
    document.querySelectorAll('.table-fee tbody tr td:nth-child(3)').forEach(td => td.style.backgroundColor = '')
    formDiv.style.display = 'none';
    form.action = basicUrl
}
