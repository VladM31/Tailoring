const divUpdateStuff = document.getElementById('test-div');
const divUpdateMaterial = document.getElementById('div-update-material');

const selectUpdateColorsId = 'select-color-change';
const selectUpdateMaterialsId = 'select-material-change';

const selectColorHtml = `<select class="update-select-style" name="" id="${selectUpdateColorsId}"> </select>`;
const selectMaterialsHtml = `<select  class="update-select-style" name="" id="${selectUpdateMaterialsId}"> </select>`;

const updateButtonColor = `<button type="button" class="update-stuff-button" onclick="setSelectColorUpdate()">Update color</button>`;
const updateButtonMaterial = `
<button type="button" class="update-stuff-button" onclick="setSelectMaterialUpdate()">Material color</button>

`;


function setSelectColorUpdate() {

    divUpdateStuff.innerHTML = selectColorHtml;

    const selectUpdateMaterials = document.getElementById(selectUpdateColorsId);

    setColorOption(colorsArray, selectUpdateMaterials, '', '');

    setOptionsSkipAndChooseAndEvent(selectUpdateMaterials, 'Choose color', getEventWithId);

}

function setSelectMaterialUpdate() {

    divUpdateMaterial.innerHTML = selectMaterialsHtml;

    const selectUpdateMaterials = document.getElementById(selectUpdateMaterialsId);

    setMaterilasOption(materialsArray, selectUpdateMaterials);

    setOptionsSkipAndChooseAndEvent(selectUpdateMaterials, 'Choose material', getEventWithIdForMaterial);

}

function setOptionsSkipAndChooseAndEvent(selectStuff, nameLableChoose, functionInsideEvent) {
    const optionChoose = document.createElement('option');
    optionChoose.value = -1;
    optionChoose.innerHTML = nameLableChoose;
    optionChoose.disabled = true;
    optionChoose.selected = true;

    const optionSkip = document.createElement('option');
    optionSkip.value = 'skip';
    optionSkip.innerHTML = 'SKIP';
    optionSkip.selected = false;

    selectStuff.add(optionChoose, 0);
    selectStuff.add(optionSkip, 1);

    selectStuff.addEventListener('change', (event) => {
        functionInsideEvent(event.target.value)
    });
}

function getEventWithId(id) {
    if (id === 'skip') {
        divUpdateStuff.innerHTML = updateButtonColor;
        return;
    }
    const color = getMaterialById(colorsArray, id);
    divUpdateStuff.innerHTML = getUpdateColorDiv(color);
}

function getEventWithIdForMaterial(id) {
    if (id === 'skip') {
        divUpdateMaterial.innerHTML = updateButtonMaterial;
        return;
    }
    const material = getMaterialById(materialsArray, id);
    divUpdateMaterial.innerHTML = getUpdateMaterialDiv(material);
}

const idUpdateColorName = 'update-color-name';
const idUpdateColorCode = 'update-color-code';

function getUpdateColorDiv(color) {
    return `
    <div class="input-other-information-select-container">
        <div class="container-elements-column">
            <label>Update cplor</label>
            <div class="add-materials-and-colors">
                <input type="text" id="${idUpdateColorName}" class="add-name" value="${color.name}">
                <input type="text" id="${idUpdateColorCode}" class="add-code-or-cost" value="${color.code}">
                <button type="button" id="update-color-button" 
                onclick="getDataForUpdate('${idUpdateColorName}','${idUpdateColorCode}',${color.id}
                    )">ᐩ</button>
                    <input class="skip-button" type="button" value="Skip" onclick="setButtonUpdateColor()">
            </div>
           
        </div>
    </div>
    `
}

const idUpdateMaterialName = 'update-material-name';
const idUpdateMaterialCost = 'update-material-cost';

function getUpdateMaterialDiv(material) {
    return `
    <div class="input-other-information-select-container">
        <div class="container-elements-column">
            <label>Update materila</label>
            <div class="add-materials-and-colors">
                <input type="text" id="${idUpdateMaterialName}" class="add-name" value="${material.name}">
                <input type="number" id="${idUpdateMaterialCost}" class="add-code-or-cost" value="${material.cost}">
                <button type="button" id="update-color-button" 
                onclick="getDataForUpdateMaterial('${idUpdateMaterialName}','${idUpdateMaterialCost}',${material.id}
                    )">ᐩ</button>

                    <input class="skip-button" type="button" value="Skip" onclick="setButtonUpdateMaterial()">
            </div>
        </div>
    </div>
    `
}

function setButtonUpdateMaterial() {
    divUpdateMaterial.innerHTML = updateButtonMaterial;
}

function setButtonUpdateColor() {
    divUpdateStuff.innerHTML = updateButtonColor;
}

async function getDataForUpdateMaterial(idMaterialName, idMaterialCode, idMaterial) {
    const name = document.getElementById(idMaterialName).value;
    const cost = document.getElementById(idMaterialCode).value;

    if (hasErrorMaterial(name, cost)) {
        return;
    }


    divUpdateMaterial.innerHTML = updateButtonMaterial;

    const formData = new FormData();
    formData.append('name', name);
    formData.append('cost', cost);
    formData.append('id', idMaterial);

    const wasUpdate = await sendStuffFetch(formData, urlMaterial, 'PUT');

    if (wasUpdate) {
        await updateSelectMaterial();
    }
}


async function getDataForUpdate(idColorName, idColorCode, idColor) {
    const name = document.getElementById(idColorName).value;
    const code = document.getElementById(idColorCode).value;

    if (hasErrorColor(name, code)) {
        return;
    }

    divUpdateStuff.innerHTML = updateButtonColor;

    const formData = new FormData();
    formData.append('name', name);
    formData.append('code', code);
    formData.append('id', idColor);

    const wasUpdate = await sendStuffFetch(formData, urlColor, 'PUT');

    if (wasUpdate) {
        await updateSelectColor();
    }
}
