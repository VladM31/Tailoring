const divPartName = "dimension-of-part-element-id";
const namePart = "namePart";
var generatorId = 0;

function getDimensionOfPart() {
    let part = new Object();

    part.id = generatorId++;
    part.name = "";
    part.width = [];
    part.volume = [];
    part.length = [];
    part.height = [];

    return part;
}

const dimensionOfParts = [];

const containerDimensionOfPart = document.getElementById('container-dimension-of-part-id');

function addDimensionOfPart() {

    const part = getDimensionOfPart();
    dimensionOfParts.push(part);

    updateContainer();

    //console.log(dimensionOfParts);
}

function getFunctionUpdateValue(id) {
    return function (e) {
        for (let i = 0; i < dimensionOfParts.length; i++) {
            if (dimensionOfParts[i].id === id) {
                dimensionOfParts[i].name = e.target.value;
                // console.log("value " + e.target.value);
                // console.log("id "+ id);
            }

        }
    }
}

function createDivDimensionOfPart(part) {

    return "" +
        `
    <div class="add-dimension-of-part-box" id="dimension-of-part-element-id${part.id}">
        ${setDimensionOfPartInDiv(part)}
    </div>
    `;
}

function updateContainer() {
    containerDimensionOfPart.innerHTML = "";
    for (let i = 0; i < dimensionOfParts.length; i++) {
        containerDimensionOfPart.innerHTML += createDivDimensionOfPart(dimensionOfParts[i]);
    }

    for (let i = 0; i < dimensionOfParts.length; i++) {
        var inputNamePart = document.getElementById('namePart' + dimensionOfParts[i].id);
        inputNamePart.addEventListener('input', getFunctionUpdateValue(dimensionOfParts[i].id));
        // console.log("id "+ dimensionOfParts[i].id);
    }
}

function updateAllContainerWithRemoveOneElement(id) {
    containerDimensionOfPart.innerHTML = "";
    for (let index = 0; index < dimensionOfParts.length; index++) {
        if (dimensionOfParts[index].id === id) {
            dimensionOfParts.splice(index, 1);
            break;
        }
    }
    updateContainer();
}

function setDimensionOfPartInDiv(dimensionOfPart) {

    return `
        <div class="row-flex-center-box name-lable">
            <input type="text" class="input-dimension-of-part-name" placeholder="Name part" value="${dimensionOfPart.name}" 
            id="namePart${dimensionOfPart.id}">
            <button type="button" class="input-dimension-of-part-name-button" onclick="updateAllContainerWithRemoveOneElement(${dimensionOfPart.id})">¯</button>
        </div>

        <div class="row-flex-space-between-box-pading">
            <div class="container-parts-size-box">
                <label for="button_width${dimensionOfPart.id}" class="parts-size-lable">Width (cm)</label>
                <div class="row-flex-center-box">
                    <input type="number" class="parts-size-box" id="input_width${dimensionOfPart.id}">
                    <button type="button" class="parts-size-button" id="button_width${dimensionOfPart.id}"
                    onclick="addSizeForPart('width',${dimensionOfPart.id})">ᐩ</button>
                </div>

                ${getListOfPartSizeValue(dimensionOfPart.width, "width", dimensionOfPart.id)}
            </div>

            <div class="container-parts-size-box">
                <label for="width12" class="parts-size-lable">Volume (cm)</label>
                <div class="row-flex-center-box">
                    <input type="number" class="parts-size-box" id="input_volume${dimensionOfPart.id}">
                    <button type="button" class="parts-size-button"
                    onclick="addSizeForPart('volume',${dimensionOfPart.id})">ᐩ</button>
                </div>

                ${getListOfPartSizeValue(dimensionOfPart.volume, "volume", dimensionOfPart.id)}
            </div>

            <div class="container-parts-size-box">
                <label for="width12" class="parts-size-lable">Length (cm)</label>
                <div class="row-flex-center-box">
                    <input type="number" class="parts-size-box" id="input_length${dimensionOfPart.id}">
                    <button type="button" class="parts-size-button"
                    onclick="addSizeForPart('length',${dimensionOfPart.id})" >ᐩ</button>
                </div>

                ${getListOfPartSizeValue(dimensionOfPart.length, "length", dimensionOfPart.id)}
            </div>

            <div class="container-parts-size-box">
                <label for="width12" class="parts-size-lable">Height (cm)</label>
                <div class="row-flex-center-box">
                    <input type="number" class="parts-size-box" id="input_height${dimensionOfPart.id}">
                    <button type="button" class="parts-size-button" 
                    onclick="addSizeForPart('height',${dimensionOfPart.id})">ᐩ</button>
                </div>

                ${getListOfPartSizeValue(dimensionOfPart.height, "height", dimensionOfPart.id)}
            </div>

        </div>`;
}


function getPartById(id) {
    for (let i = 0; i < dimensionOfParts.length; i++) {
        if (dimensionOfParts[i].id === id) {
            return dimensionOfParts[i];
        }
    }

    return null;
}

function addSizeForPart(name, id) {
    const input = document.getElementById('input_' + name + id);

    if (input.value === "" || input.value < 1) {
        input.value = "";
        return;
    }

    const part = getPartById(id);

    if (part[name].includes(input.value)) {
        input.value = "";
        return;
    }

    part[name].push(input.value);

    input.value = "";

    updateContainer();
}

function getListOfPartSizeValue(list, name, id) {
    var string = "";

    for (let index = 0; index < list.length; index++) {
        string += `
            <div class="row-flex-center-box parts-size-value">
                <input type="number" readonly class="parts-size-box parts-size-value-box" value="${list[index]}">
                <button type="button" class="parts-size-button parts-size-value-button"  onclick="removeSizeForPart('${name}',${id},${list[index]})"
               >¯</button>
            </div>
        `
    }

    return string;
}


function removeSizeForPart(name, id, value) {

    const part = getPartById(id);

    for (let i = 0; i < part[name].length; i++) {

        if (part[name][i] == value) {
            part[name].splice(i, 1);
            break;
        }
    }

    updateContainer();
}

function setJson(id) {
    const placeFoJson = document.getElementById(id);

    placeFoJson.value = JSON.stringify(dimensionOfParts);
}

function setPartSizeFromJson() {
    const jsonPartSize = document.getElementById('hidden-json-part-size-id') ?? null;

    if (jsonPartSize == null || jsonPartSize == undefined || jsonPartSize.value == '[]' || jsonPartSize.value == '') {
        console.log(null)
        return;
    }

    const parts = JSON.parse(jsonPartSize.value);


    parts.forEach(e => {
        e.id = generatorId++;
        dimensionOfParts.push(e);
    })

    updateContainer();
}

setPartSizeFromJson();