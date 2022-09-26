const MALE_BUTTON = document.getElementById('male-button-id');
const FEMALE_BUTTON = document.getElementById('female-button-id');
const GENDER_VALUE = document.getElementById('gender-value-id');

const ACTIVE_STYLE = 'gender-container-button-active';

function startGenderController() {
    const value = GENDER_VALUE.value;

    if (value == "true") {
        MALE_BUTTON.classList.add(ACTIVE_STYLE)
    } else {
        FEMALE_BUTTON.classList.add(ACTIVE_STYLE)
    }
}

function replaceActive(buttonToActive, buttonToInactive, genderValue) {
    buttonToActive.classList.add(ACTIVE_STYLE);
    buttonToInactive.classList.remove(ACTIVE_STYLE);
    GENDER_VALUE.value = genderValue;
}

function buttonMale() {
    replaceActive(MALE_BUTTON, FEMALE_BUTTON, 'true');
}

function buttonFemale() {
    replaceActive(FEMALE_BUTTON, MALE_BUTTON, 'false');
}

MALE_BUTTON.addEventListener('click', buttonMale);
FEMALE_BUTTON.addEventListener('click', buttonFemale);

startGenderController();