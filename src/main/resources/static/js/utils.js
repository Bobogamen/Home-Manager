document.querySelectorAll(".notification  p").forEach(p => {
    let pText = p.textContent;

    if (pText.length > 0) {
        p.style.display = 'block'
        setTimeout(() => hideTag(p), 7000)
    }
})

function hideTag(tag) {
    tag.style.display = 'none';
}


























