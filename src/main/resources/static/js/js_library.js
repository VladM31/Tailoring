function getObjectById(array, id) {
    for (let i = 0; i < array.length; i++) {
        if (array[i].id === id) {
            return array[i];
        }
    }
    return null;
}

function getObjectByFunctionId(array, id, functionGetId) {
    for (let i = 0; i < array.length; i++) {
        if (functionGetId(array[i]) === id) {
            return array[i];
        }
    }
    return null;
}