function showPayment(data) {
    fillFooter(fillRows(data));
}

function fillRows(data) {
    let tbody = document.querySelector('.table-fee').tBodies[0];
    let row = document.querySelector('.table-fee').tBodies[0].children[0];

    let total = 0.0;

    if (data.length > 0) {
        tbody.removeChild(document.querySelector('.table-fee').tBodies[0].children[0]);

        data.forEach(o => {
            let newRow = row.cloneNode(true)
            let match = matchSelected(newRow.cells[2].children[0], o.times);

            newRow.id = o.id;
            newRow.cells[0].textContent = o.name
            newRow.cells[1].textContent = o.value
            match.selected = true
            newRow.cells[3].textContent = o.total.toFixed(2)

            total += o.value * o.times

            tbody.appendChild(newRow)
        })
    } else {
        row.textContent = 'Няма зададени такси.'
    }

    return total;
}

function fillFooter(data) {
    let tfoot = document.querySelector('.table-fee').tFoot.children[0];
    tfoot.children[0].textContent = `Общо: ${data.toFixed(2)}лв`
}

function matchSelected(target, match) {

    for (let i = 0; i < target.length; i++) {
        if (target[i].value == match) {
            return target[i]
        }
    }
}

function calculateRowTotal(target) {
    let times = Number(target.selectedIndex)
    let value = Number(target.parentElement.parentElement.cells[1].textContent)
    target.parentElement.parentElement.cells[3].textContent = (times * value).toFixed(2)

    fillFooter(calculateTotal())
}

function calculateTotal() {
    let rows = Array.from(document.querySelector('.table-fee').tBodies[0].rows)

    let total = 0.00;
    rows.forEach(r => {
        total += Number(r.children[3].textContent)
    })

    return total;
}

function collectDataFromRows() {

    let rows = document.querySelector('.table-fee').tBodies[0].rows
    let arrData = []

    for (let row of rows) {

        let obj = {
            id: row.id,
            value: row.cells[1].textContent,
            timesPaid: row.cells[2].children[0].selectedIndex,
            total: row.cells[3].textContent
        }
        arrData.push(obj)

    }

    return JSON.stringify(arrData);
}

function fillMonthHomeId(monthHomeId) {
    document.getElementById('monthHomeId').value = monthHomeId;
}

function pay(target) {
    target.parentElement[4].value = collectDataFromRows()
}

async function getPayment(target) {
    const homesGroupId = target.id;
    const monthId = target.name;
    const monthHomeId = target.value

    fillMonthHomeId(monthHomeId)

    let data = await request(`/cashier/homesGroup${homesGroupId}/get-payment?monthId=${monthId}&monthHomeId=${monthHomeId}`, 'GET');
    modal.style.display = 'block';
    let floorName = target.parentElement.parentElement.parentElement.parentElement.tHead.children[0].children[0].textContent
    let homeName = target.parentElement.parentElement.parentElement.parentElement.tHead.children[0].children[1].textContent
    let floorValue = target.parentElement.parentElement.children[0].textContent
    let homeValue = target.parentElement.parentElement.children[1].textContent;
    modal.querySelector('p').textContent = `${floorName} ${floorValue} ${homeName} ${homeValue}`;
    showPayment(data.response)
}

async function request(url, method, data) {
    const options = {
        credentials: 'include',
        method: method,
        headers: {
            'Accept': 'application/json',
            'Content': 'application/json',
            'Cache': 'no-cache'
        }
    }

    if (data !== undefined) {
        options.body = data
    }

    const result = await fetch(url, options);

    if (result.status === 204) {
        return result
    } else {
        return result.json();
    }
}

