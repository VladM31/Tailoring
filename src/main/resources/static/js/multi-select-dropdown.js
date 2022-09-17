const customSelects = [];

const mapOldSelectIdToNewSelectId = new Map();

const selectIdToOptionsArray = new Map();

var generatorId = 0;

const addNewSelectIdPiece = 'new_';
const positionDefaultOption = 0;

function startCustomSelect(arrayCustoms,) {
    arrayCustoms.forEach(element => {
        customSelects.push(element);
    });

    setJustSelect();
}

function setJustSelect() {
    for (let i = 0; i < customSelects.length; i++) {
        const optionArray = [];
        const select = document.getElementById(customSelects[i].id);
        const newSelect = document.createElement('select');
        copy(newSelect, select);

        selectIdToOptionsArray.set(newSelect.id, []);

        optionArray.push(getDefaultOption(customSelects[i].defaultText));

        for (let j = 0; j < select.children.length; j++) {
            const opt = createOptionForNewSelect(select.children[j]);

            if (opt.selected) {
                opt.classList.add(customSelects[i].style);
            }

            optionArray.push(opt);
        }

        hideAndLinkOldSelectToNewSelect(select, newSelect);

        newSelect.addEventListener('change', eventChangeAndResetSelect, true);

        setOptionsToSelectAndMyOptionalArray(newSelect, optionArray, customSelects[i].defaultText);
    }
}

function setOptionsToSelectAndMyOptionalArray(newSelect, optionArray, defaultText) {
    myOptionArray = selectIdToOptionsArray.get(newSelect.id);

    for (let i = 0; i < optionArray.length; i++) {
        myOptionArray.push(optionToMyObject(optionArray[i]));
        newSelect.appendChild((optionArray[i]));
    }

    newSelect.selectedIndex = 0;

    const text = setTextFromSelectedOptionsOrDefault(myOptionArray, defaultText);
    optionArray[positionDefaultOption].innerText = text;
    myOptionArray[positionDefaultOption].text = text;
}

function copy(newSelect, oldSelect) {
    newSelect.id = addNewSelectIdPiece + oldSelect.id;
    newSelect.classList = oldSelect.classList;
}

function createOptionForNewSelect(optionFromOldSelect) {
    const opt = document.createElement('option');
    opt.text = optionFromOldSelect.text;
    opt.id = 'option' + generatorId++;
    opt.value = optionFromOldSelect.value;
    opt.selected = optionFromOldSelect.selected;
    opt.disabled = optionFromOldSelect.disabled;

    return opt;
}

function hideAndLinkOldSelectToNewSelect(oldSelect, newSelect) {
    oldSelect.classList.add('hidden-style');

    mapOldSelectIdToNewSelectId.set(oldSelect.id, newSelect.id);

    oldSelect.parentNode.insertBefore(newSelect, oldSelect);
}

function eventChangeAndResetSelect(e) {

    const myOpt = getById(selectIdToOptionsArray.get(e.target.id), e.target[e.target.selectedIndex].id);
    e.target.selectedIndex = 0;

    const customSelect = getById(customSelects, e.target.id.substring(addNewSelectIdPiece.length));
    myOpt.isSelected = !myOpt.isSelected;
    setStyleDependingOnSelect(myOpt, customSelect.style);

    const textDef = setTextFromSelectedOptionsOrDefault(selectIdToOptionsArray.get(e.target.id), customSelect.defaultText);
    selectIdToOptionsArray.get(e.target.id)[0].text = textDef;

    document.getElementById(selectIdToOptionsArray.get(e.target.id)[0].id).text = textDef;
}

function optionToMyObject(opt) {
    return {
        id: opt.id,
        text: opt.text,
        classList: opt.classList,
        isSelected: opt.disabled ? false : opt.selected,
        value: opt.value
    }
}

function getById(array, id) {
    for (let index = 0; index < array.length; index++) {
        if (array[index].id === id) {
            return array[index];
        }
    }
}

function setStyleDependingOnSelect(myopt, textStyle) {
    if (myopt.isSelected) {
        myopt.classList.add(textStyle);
    } else {
        myopt.classList.remove(textStyle);
    }
}

function getDefaultOption(text) {
    const opt = document.createElement('option');
    opt.text = text;
    opt.selected = 'selected';
    opt.disabled = true;
    opt.id = 'option' + generatorId++;
    return opt;
}

function selectToMultipleSelect() {
    for (let i = 0; i < customSelects.length; i++) {
        const select = document.getElementById(customSelects[i].id);

        select.innerHTML = "";

        const idNew = mapOldSelectIdToNewSelectId.get(customSelects[i].id);

        const arrayMyOptions = selectIdToOptionsArray.get(idNew);

        arrayMyOptions.forEach(element => {

            select.innerHTML += testMultiplOption(element);


        });
    }
}

function testMultiplOption(myopt) {
    return `<option ${myopt.isSelected ? 'selected' : ''} value="${myopt.value}">${myopt.text}</option>`
}

function setTextFromSelectedOptionsOrDefault(myOptArray, defaultText) {

    const myOptTextArray = [];

    for (let i = 0; i < myOptArray.length; i++) {
        if (myOptArray[i].isSelected) {
            myOptTextArray.push(myOptArray[i].text);
        }
    }

    return myOptTextArray.length > 0 ? myOptTextArray.join(", ") : defaultText;
}

function multipleCustomeSelect(idSelect, defaultText, selectedStyle) {
    return {
        id: idSelect,
        defaultText: defaultText,
        style: selectedStyle
    }
}