const activeValue = document.getElementById('check-active-order-value');
const activeButtom = document.getElementById('check-active-order-buttom');

function changeActive() {

    activeValue.value = activeValue.value == 'true' ? 'false' : 'true';

    setChangeActive();
}

function setChangeActive() {
    let active = activeValue.value == 'true';

    active == true ? activeButtom.classList.remove("active") : activeButtom.classList.add("active");

    activeButtom.textContent = active == true ? "Inactive" : "Active";
}

setChangeActive();