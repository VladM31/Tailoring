const INPUT_FILES = document.querySelector('input');

INPUT_FILES.style.opacity = 0;

INPUT_FILES.addEventListener('change', updateImageDisplay);

const IMAGE_BOX_CONTAINER = document.getElementById('imageBoxContainer');

function updateImageDisplay() {

    IMAGE_BOX_CONTAINER.innerHTML = "";
    var curFiles = INPUT_FILES.files;

    if (curFiles.length === 0) {
        IMAGE_BOX_CONTAINER.innerHTML = `<p class="files-warn-info"> Files not choose </p>`
        return;
    }

    if (curFiles.length > 10) {
        IMAGE_BOX_CONTAINER.innerHTML = `<p class="files-warn-info"> Files more than 10 </p>`
        return;
    }

    for (var i = 0; i < curFiles.length; i++) {
        if (validFileType(curFiles[i])) {
            IMAGE_BOX_CONTAINER.innerHTML += createImageBox(curFiles[i].name, returnFileSize(curFiles[i].size), window.URL.createObjectURL(curFiles[i]));
        } else {
            IMAGE_BOX_CONTAINER.innerHTML += createImageBox(curFiles[i].name, "...", "");
        }
    }
}


var fileTypes = [
    'image/jpeg',
    'image/pjpeg',
    'image/png'
]

function validFileType(file) {
    for (var i = 0; i < fileTypes.length; i++) {
        if (file.type === fileTypes[i]) {
            return true;
        }
    }
    return false;
}

function returnFileSize(number) {
    if (number < 1024) {
        return number + 'bytes';
    } else if (number > 1024 && number < 1048576) {
        return (number / 1024).toFixed(1) + 'KB';
    } else if (number > 1048576) {
        return (number / 1048576).toFixed(1) + 'MB';
    }
}

function createImageBox(fileName, fileSize, imageSrc) {
    return ` 
    <div class="images-box">
        <p class="image-info">File name: ${fileName}, file size: ${fileSize}. </p>
        <img class="image-style" src="${imageSrc}" alt="${fileName}">
    </div>
    `
}