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

function checkAll() {
    document.querySelectorAll(".inputCheckBox").forEach(i => {
        if (i.checked) {
            i.checked = false;
            changeValueAttribute(i)
        } else {
            i.checked = true;
            changeValueAttribute(i)
        }
    });
}

function changeValueAttribute(target) {
    if (target.checked) {
        target.value = true
        if (target.nextElementSibling !== undefined) {
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





















