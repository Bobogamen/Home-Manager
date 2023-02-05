function showButton(target) {

    let button = target.form.querySelector('button');
    button.style.display = 'block'

    uncheckOthers(target.form)
    hideAllOthersButtons(button)
}

function  uncheckOthers(form) {
    let forms = Array.from(document.querySelectorAll('form'))
    forms.shift();

    forms.forEach(f => {
        if (f !== form) {
            f.querySelectorAll('input').forEach(i => i.checked = false)
        }
    })
}

function hideAllOthersButtons(button) {
    Array.from(document.querySelectorAll('.edit-times'))
        .filter(b => b !== button)
        .forEach(b => b.style.display = '')
}

function hideAllEditTimesButtons() {
    document.querySelectorAll('.edit-times').forEach(b => b.style.display = '')
}

function uncheckAll() {
    document.querySelectorAll('.inputCheckBox').forEach(i => i.checked = false)
}
