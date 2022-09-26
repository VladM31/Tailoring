var entityFormFilter = document.getElementById('filter-id');

const MULTIPLE_SELECTS = [];

const MAP_PAIR = new Map();

async function getEntities(url){
    const response = await fetch(url,{
        method : 'GET'
    });
    if(response.status > 299){
        alert( "Inputed incorrect data : " + await response.text())
        return [];
    }

    const entities = await response.json();
    return entities;
}

async function getFormDataFromFilter(){
    const formData = new FormData();
    for (let i = 0, size = entityFormFilter.childElementCount +1; i < size; i++) {

        if(MULTIPLE_SELECTS.includes(entityFormFilter[i].name)){
            const name = entityFormFilter[i].name;
            for (let j = 0; j < entityFormFilter[i].childElementCount; j++) {
                if (entityFormFilter[i][j].selected) {
                    formData.append(name,entityFormFilter[i][j].value)
                }
            }
            continue;
        }

        if (MAP_PAIR.has(entityFormFilter[i].name)) {
            const name = entityFormFilter[i].name ;
            MAP_PAIR.get(name)[name] = entityFormFilter[i].checked;
            continue;
        }

        if(hasParameters(i)){
            formData.append(entityFormFilter[i].name,entityFormFilter[i].value);
        }
    }
    mapPairsToFormData(formData);
    // logFormData(formData);
    return formData;
}

function mapPairsToFormData(formData){
    const iterator = MAP_PAIR.values();

    let it = null;
    const array = [];

    while((it = iterator.next()).done == false){
        if(!array.includes(it.value) && it.value){
            const element = it.value;
           
            array.push(element);
            if(element.toValue() != null){
                formData.append(element.requestName,element.toValue())
            }
        }
    }
}

function hasParameters(i) {
    return entityFormFilter[i].name 
    && entityFormFilter[i].value 
}

function setPair(trueName,falseName,requestName){
    let  pair = {
        requestName : requestName,
    }

    pair[trueName] = null;
    pair[falseName] = null;

    pair.toValue = function(){
        if(pair[trueName] == pair[falseName]){
            return null;
        }
        if(pair[trueName]){
            return true;
        }
        return false;
    }

    MAP_PAIR.set(trueName,pair);
    MAP_PAIR.set(falseName,pair);

    return 
}

function setMultipleSelects(multipleSelects){
    for (let i = 0; i < multipleSelects.length; i++) {
        MULTIPLE_SELECTS.push(multipleSelects[i]);
    }
}

function logFormData(formData){
    let it = null;
    const iterator = formData.entries();

    while((it = iterator.next()).done == false){
        console.log(`${it.value[0]} : ${it.value[1]}`);
    }
}

function formDataToUrlParameter(formData){
    var parameters = []
    for (var pair of formData.entries()) {
        parameters.push(
            encodeURIComponent(pair[0]) + '=' +
            encodeURIComponent(pair[1])
        );
    }
    return "?" + parameters.join('&');
}