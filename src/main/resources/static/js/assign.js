let cashiersGroupsAssigned = {}
document.querySelectorAll('.cashier').forEach(c => getCashiersAssignedGroups(c))

function showButton(target) {

    let button = target.form.querySelector('button');
    if (button.style.display !== 'block') {
        button.style.display = 'block'
    }

    let skipCashierName = target.form.parentElement.previousElementSibling.children[0].textContent

    uncheckOthers(skipCashierName)
    hideAllOthersButtons(button)
}

function  uncheckOthers(skipCashierName) {
    let cashiers = document.querySelectorAll('.cashier');

    cashiers.forEach(c => checkInputs(c, skipCashierName))
}

function checkInputs(cashier, skipCashierName) {
    let name = getCashierName(cashier)

    if (name !== skipCashierName) {
        let rows = cashier.querySelector('table').tBodies[0].children

        let cashierGroups = cashiersGroupsAssigned[name];

        if (cashierGroups.length !== 0) {
            for (let tr of rows) {
                let rowName = tr.firstElementChild.textContent;

                for (let groupName of cashierGroups) {
                    let input = tr.children[1].firstElementChild

                    if (rowName === groupName) {
                        input.checked = true;
                        break;
                    } else {
                        input.checked = false;
                    }
                }
            }
        } else {
            for (let row of rows) {
                row.children[1].firstElementChild.checked = false
            }
        }
    }
}

function hideAllOthersButtons(button) {
    Array.from(document.querySelectorAll('.edit-times'))
        .filter(b => b !== button)
        .forEach(b => b.style.display = '')
}

function getCashierName(cashier) {
    return cashier.children[0].children[0].textContent;
}

function getCashiersAssignedGroups(cashier) {
    let name = getCashierName(cashier)
    let groups = []
        cashier.children[1].children[0].children[1].querySelectorAll('input').forEach(i => {
        if (i.checked) {
            groups.push(i.parentElement.previousElementSibling.textContent);
        }
    })

    cashiersGroupsAssigned[name] = groups
}


