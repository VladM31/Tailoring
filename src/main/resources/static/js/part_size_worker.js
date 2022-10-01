const containerPartSize = document.getElementById('part-size-container-id');

const jsonTextPartSizeTemplates = document.getElementById('part-size-template-id');

const hiddenInput = document.getElementById('part-orders-id');

const partSizeTemplates = JSON.parse(jsonTextPartSizeTemplates.textContent.trim());

const partSizeOrders = [];


const SELECTED_PART_SIZES = JSON.parse(hiddenInput.value.length == 0 ? '[]' : hiddenInput.value);

let generatorPartId = 0;

jsonTextPartSizeTemplates.remove();

for (let i = 0; i < partSizeTemplates.length; i++) {

    const partOrder = getPartOrder(
        partSizeTemplates[i].name,
        partSizeTemplates[i]?.width[0],
        partSizeTemplates[i]?.volume[0],
        partSizeTemplates[i]?.length[0],
        partSizeTemplates[i]?.height[0])

    containerPartSize.innerHTML += getPartOrderFromPartTemplate(partSizeTemplates[i], partOrder.id);

    partSizeOrders.push(partOrder);
}

function getPartOrder(name, width, volume, length, height) {
    return {
        id: ++generatorPartId,
        name: name,
        width: width ?? null,
        volume: volume ?? null,
        length: length ?? null,
        height: height ?? null,
    }
}

function findSelectedPartSizeOrder(name, sizeName) {
    for (const partOrderElement of SELECTED_PART_SIZES) {
        if (partOrderElement.name === name) {
            return partOrderElement[sizeName];
        }
    }
    return null;
}

function getPartOrderFromPartTemplate(part, id) {

    const newLocal = `
    <div class="part-size-box">
        <p class="label-part-size">${part.name}</p>
        <div class="part-size-items">
            <div class="box-part-name-and-select">
                <label for="part-select-width-${id}" class="part-size-name">Width <em>cm</em></label>
                <select id="part-select-width-${id}" class="part-size-select" onchange="ser(this,${id},'width')">
                    ${getOption(part.width, findSelectedPartSizeOrder(part.name, 'width'))}
                </select>
            </div>
            <div class="box-part-name-and-select">
                <label for="part-select-volume-${id}" class="part-size-name">Volume <em>cm</em></label>
                <select id="part-select-volume-${id}" class="part-size-select" onchange="ser(this,${id},'volume')">
                    ${getOption(part.volume, findSelectedPartSizeOrder(part.name, 'volume'))}
                </select>
            </div>
            <div class="box-part-name-and-select">
                <label for="part-select-length-${id}" class="part-size-name">Length <em>cm</em></label>
                <select id="part-select-length-${id}" class="part-size-select" onchange="ser(this,${id},'length')">
                ${getOption(part.length, findSelectedPartSizeOrder(part.name, 'length'))}
                </select>
            </div>
            <div class="box-part-name-and-select">
                <label for="part-select-height-${id}" class="part-size-name">Height <em>cm</em></label>
                <select id="part-select-height-${id}" class="part-size-select" onchange="ser(this,${id},'height')">
                ${getOption(part.height, findSelectedPartSizeOrder(part.name, 'height'))}
                </select>
            </div>
        </div>
    </div>                 
    `;
    return newLocal
}

function getOption(arrayValues, partOrderValue) {
    let options = "";

    arrayValues.forEach(element => {
        options += `<option ${element == partOrderValue ? 'selected' : ''} value="${element}">${element}</option>`
    });

    return options;
}

function ser(even, id, fieldName) {
    const partOrder = getById(partSizeOrders, id);
    partOrder[fieldName] = even[even.selectedIndex].value;
}

function getById(array, id) {
    for (let index = 0; index < array.length; index++) {
        if (array[index].id === id) {
            return array[index];
        }
    }
}

function partOrdersToHiddenInput() {
    hiddenInput.value = JSON.stringify(partSizeOrders);
}