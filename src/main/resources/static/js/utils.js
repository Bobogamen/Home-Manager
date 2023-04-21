document.querySelectorAll(".notification  p").forEach(p => {
    let pText = p.textContent;

    if (pText.length > 0) {
        p.style.display = 'block'
        setTimeout(() => hide(p), 7000)
    }
})

function hide(tag) {
    tag.style.display = 'none';
}

let boxChecked = [...document.querySelectorAll('.inputCheckBox')].some(i => i.checked === true);

function checkAll() {

    boxChecked === false ?
        (document.querySelectorAll('.inputCheckBox').forEach(i => {
            i.checked = true;
            changeValueAttribute(i)
        }), boxChecked = true) :
        (document.querySelectorAll('.inputCheckBox').forEach(i => {
            i.checked = false
            changeValueAttribute(i)
        }), boxChecked = false);
}

function changeValueAttribute(target) {
    if (target.checked) {
        target.value = true
        if (target.nextElementSibling !== null) {
            let hiddenInput = target.nextElementSibling;
            target.parentElement.removeChild(hiddenInput);
        }
    } else {
        target.value = false
        let hidden = target.cloneNode(true);
        hidden.type = 'hidden'
        hidden.className = '';
        hidden.removeAttribute("onclick");
        target.parentElement.append(hidden);
    }
}

document.onload((e) => console.log('Loaded!'))

























