const photoSlider = {
    position: 0,
    size: 3,
    toPrevious: function () {
        --this.position;
        if (this.position < 0) {
            this.position = this.size - 1;
        }
    },
    toNext: function () {
        ++this.position;
        if (this.position == this.size) {
            this.position = 0;
        }
    }
}

const photoSrc = [];

const slider = {
    img: document.getElementById('image-slide-id')
}

function startPhotoSlider(size, photoSrcs) {
    photoSlider.size = size;

    photoSrcs.forEach(element => {
        photoSrc.push(element);
    });
}

function next() {
    photoSlider.toNext();
    changeImage();
}

function previous() {
    photoSlider.toPrevious();
    changeImage();
}


function changeImage() {
    const oldImg = slider.img;
    slider.img = document.createElement('img');
    slider.img.src = photoSrc[photoSlider.position];
    slider.img.classList.add('template-order-image');
    slider.img.classList.add('photo-fade');
    oldImg.replaceWith(slider.img);
}

