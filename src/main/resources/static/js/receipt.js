function receiptCalculateTotal(target) {
    let rows = Array.from(target.parentElement.parentElement.parentElement.rows)

    let total = 0.0;

    rows.forEach(r => {
        let value =  Number(r.children[1].textContent)
        let times =  Number(r.children[2].children[0].value)

        total += value * times

        r.children[3].textContent = (value * times).toFixed(2)
    })

    let totalRow = target.parentElement.parentElement.parentElement.nextElementSibling.children[0].children[0];

    totalRow.textContent = `${totalRow.textContent.substring(0, 3)} ${Number(total.toFixed(2))}лв`
}

function doPDF(target) {
    const options = {
        margin: [5, 1, 5 ,1],
        filename: 'receipts.pdf',
        pagebreak: {
            mode: 'css',
            after: '.page-break-after',
        },

        html2canvas: {
            dpi: 200,
            scale: 1,
            scrollY: 0,
            scrollX: 0,
            letterRendering: true
        },

        jsPDF: {
            unit: 'pt',
            format: 'a4',
            orientation: 'landscape',
        }
    }

    if (target === 'homes_list') {
        options.margin = [20, 6, 6, 6]
        options.jsPDF.orientation = 'portrait'
    }

    const element = document.getElementById(target);

    html2pdf().from(element).set(options).save();

}