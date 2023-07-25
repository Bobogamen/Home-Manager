document.onreadystatechange = () => {
    if (document.readyState === 'loading' || document.readyState === 'interactive') {
        loading();
    } else  {
        setTimeout(() => loaded(), 150);
    }
}


function loading() {
    document.getElementById('main').style.display = 'none'
    document.getElementById('loader').style.display = ''


}

function loaded (){
    document.getElementById('main').style.display = ''
    document.getElementById('loader').style.display = 'none'
}

// document.onreadystatechange = function () {
//     if (document.readyState !== "complete") {
//         document.getElementById('loader').style.display = "";
//         document.body.style.display = "none";
//     } else {
//         document.getElementById('loader').style.display = "none";
//         document.body.style.display = "";
//     }
// }