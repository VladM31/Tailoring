const nameColor = document.getElementById('add-color-name');
const codeColor = document.getElementById('add-color-code');
const buttonColor = document.getElementById('add-color-button');

const nameMaterial = document.getElementById('add-material-name');
const costMaterial = document.getElementById('add-material-cost');
const buttonMaterial = document.getElementById('add-material-button');

const numberFormat16 = new RegExp('[1234567890ABCDEFabcdef]{6}');
const urlColor = "http://localhost:8095/api/v1/orders/colors";
const urlMaterial = "http://localhost:8095/api/v1/orders/materials";

async function sendColor() {
    const name = nameColor.value;
    const code = codeColor.value;

    if (hasErrorColor(name, code)) {
        return;
    }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('code', code);

    if (await sendStuffFetch(formData, urlColor, 'POST') === false) {
        alert('Color wasn`t save')
    } else {
        updateSelectColor();
    }

    nameColor.value = "";
    codeColor.value = "";
}

async function sendMaterial() {
    const name = nameMaterial.value;
    const cost = costMaterial.value;

    if (hasErrorMaterial(name, cost)) {
        return;
    }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('cost', cost);

    if (await sendStuffFetch(formData, urlMaterial, 'POST') === false) {
        alert('Material wasn`t save')
    } else {
        updateSelectMaterial();
    }

    nameMaterial.value = "";
    costMaterial.value = "";
}

function hasErrorColor(name, code) {

    if (!numberFormat16.test(code)) {
        alert(`Code ${code} is not exadecimal number system or lenght less 6`);
        return true;
    }

    if (code.length > 6) {
        alert(`Code ${code} more than 6 symbol `);
        return true;
    }

    if (name.length < 2 || name.length > 20) {
        alert(`Name color '${name}' less than 2 or great than 20`);
        return true;
    }

    return false;
}

function hasErrorMaterial(name, cost) {
    if (name.length < 2 || name.length > 60) {
        alert(`Name material '${name}' less than 2 or great than 60`);
        return true;
    }

    const costValue = new Number(cost);

    if (costValue === Number.NaN) {
        alert(`Cost '${cost}'  isn't number`);
        return true;
    }

    if (costValue < 1) {
        alert(`Cost '${cost}' less than one`);
        return true;
    }

    return false;
}

async function sendStuffFetch(formData, url, methodType) {
    const response = await fetch(url, {
        method: methodType,
        body: formData,
        Accept: 'application/json'
    });

    if (response.status > 299) {
        alert(await response.text());
        return false;
    }

    const data = await response.json();

    return new Boolean(data);
}


buttonColor.addEventListener('click', sendColor);
buttonMaterial.addEventListener('click', sendMaterial);