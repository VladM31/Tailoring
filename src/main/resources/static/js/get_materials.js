const selectColor = document.getElementById('select-color');
const selectMaterial = document.getElementById('select-material');
const head = document.getElementsByTagName('head');

var materialsArray = [];
var colorsArray = [];


async function updateSelectColor() {
    await removeChildren(selectColor);

    colorsArray = await getStuffByUrl(urlColor);

    setColorOption(colorsArray, selectColor, ':hover', 'mult');
}

async function updateSelectMaterial() {
    await removeChildren(selectMaterial);

    materialsArray = await getStuffByUrl(urlMaterial);

    await setMaterilasOption(materialsArray, selectMaterial);
}

async function getStuffByUrl(url) {
    const response = await fetch(url, {
        method: 'GET',
        Accept: 'application/json',
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': "http://localhost:8095"
        },
    });

    const data = await response.json();
    return data;
}

async function removeChildren(element) {
    var first = element.firstElementChild;
    while (first) {
        first.remove();
        first = element.firstElementChild;
    }
}


function getMaterialById(arrayMaterials, id) {
    for (let i = 0; i < arrayMaterials.length; i++) {
        if (arrayMaterials[i].id == id) {
            return arrayMaterials[i];
        }
    }
    return null;
}


function setColorOption(colors, selectElement, subClass, dopName) {

    colors.forEach(color => {
        const opt = document.createElement('option');
        opt.value = color.id;
        opt.innerHTML = color.name;

        const style = document.createElement('style');
        const styleName = ('styleOption' + color.name + color.code + dopName).replace(/\s/g, '_');

        style.type = 'text/css';
        style.innerHTML = `.${styleName}${subClass}{
            background-color: #${color.code};
            ${parseInt(color.code, 16) < 10000 ? 'color: #ffffff' : ''}
        }`;

        head[0].appendChild(style);
        opt.className = styleName;
        //console.log(parseInt(color.code, 16) + " " + color.name);

        selectElement.appendChild(opt);
    });
}

async function setMaterilasOption(materials, selectTheMaterial) {
    materials.forEach(mat => {
        const opt = document.createElement('option');
        opt.value = mat.id;
        opt.innerHTML = mat.name;

        selectTheMaterial.appendChild(opt);
    });
}

updateSelectColor();

updateSelectMaterial();

setInterval(updateSelectColor, 10 * 60 * 1000);
