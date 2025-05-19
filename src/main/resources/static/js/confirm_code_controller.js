const CODE_TEXT_INFO = '<p> Enter the confirmation code that you received on Telegram </p>';
const ERROR_TEXT_INFO = ' <em class="hidden-text" id="code-incorrect-text-id">Code incorrect</em> ';
const CODE_INPUT = '<input type="text" name="" id="code-value-id" placeholder="Code here" maxlength="20">';
const CODE_CHECK_BUTTON = '<button type="button" onclick="checkCode()">Check</button>';

const CHECK_PHONE_NUMBER_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/authorization/number';
const CONFIRM_CODE_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/authorization/confirm-code';
const LOGIN_URL = 'https://vladyslav-bhe6d5e4arccerfv.westeurope-01.azurewebsites.net/api/v1/authorization/login';

const CONFIRM_FORM = document.getElementById('confirm-form-id');
const TEN_SECONDS = 10000;
const REGEX_CODE = new RegExp('[0-9a-zA-Z]{20,20}');
const CODE_INCORRECT_BOX_ID = 'code-incorrect-text-id';

const NUMBER = getNumber('number-id');
let numberFound = false;
let hasCodeError = false;

function getNumber(numberInputId) {
    const numberInput = document.getElementById(numberInputId);
    const number = numberInput.value;
    numberInput.remove();
    return number;
}

async function checkNumber() {
    const respond = await fetch(CHECK_PHONE_NUMBER_URL + '?number=' + NUMBER);
    if (respond.status > 299) {
        alert('Please reload the page. ' + await respond.text());
        return false;
    }

    return await respond.json();
}

async function chengeFormAfterConfirmNumber() {
    if (numberFound) {
        return;
    }

    if (await checkNumber() == false) {
        return;
    }

    numberFound = true;
    changeFormForInputCode();
}

function changeFormForInputCode() {
    CONFIRM_FORM.innerHTML = "";


    CONFIRM_FORM.innerHTML += CODE_TEXT_INFO;
    CONFIRM_FORM.innerHTML += ERROR_TEXT_INFO;
    CONFIRM_FORM.innerHTML += CODE_INPUT;
    CONFIRM_FORM.innerHTML += CODE_CHECK_BUTTON;
}

async function sendCodeForConfirm(code) {
    const formData = new FormData();
    formData.append('phoneNumber', NUMBER);
    formData.append('code', code);
    const respound = await fetch(CONFIRM_CODE_URL, {
        method: 'POST',
        body: formData
    });
    if (respound.status > 299) {
        alert(await respound.text());
        return false;
    }

    return await respound.json();
}

async function checkCode() {
    const code = document.getElementById("code-value-id").value;

    if (REGEX_CODE.test(code) === false || await sendCodeForConfirm(code) == false) {
        showCodeError();
        return;
    }

    window.location.replace(LOGIN_URL);
    window.location.href = LOGIN_URL;
}

function showCodeError() {
    if (hasCodeError) {
        return;
    }

    const incorrectTextBox = document.getElementById(CODE_INCORRECT_BOX_ID);
    incorrectTextBox.classList.remove('hidden-text');
    hasCodeError = true;
}

chengeFormAfterConfirmNumber();
setInterval(chengeFormAfterConfirmNumber, TEN_SECONDS);

