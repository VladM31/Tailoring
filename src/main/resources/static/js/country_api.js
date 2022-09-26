const COUNTRY_API_URL = 'https://countriesnow.space/api/v0.1/countries';
const CITY_MAP = new Map();

async function getCountries() {
    const respound = await fetch(COUNTRY_API_URL);
    const respoundData = await respound.json();
    return respoundData.data;
}

async function startCounties() {
    const counties = await getCountries();
    for (const countryIter of counties) {

        const cities = [];

        for (const city of countryIter.cities) {
            cities.push(new Option(city, city));
        }

        CITY_MAP.set(countryIter.country, cities);
    }
}

async function setCountryList(idDatalist, clear) {
    const datalist = document.getElementById(idDatalist);
    if (clear) {
        datalist.innerHTML = "";
    }

    const counties = await getCountries();

    for (const countryIter of counties) {
        datalist.innerHTML += ` <option value="${countryIter.country}">${countryIter.country}</option>`;
    }
}

async function setCityList(idCountryInput, idDatalist) {
    const countryInput = document.getElementById(idCountryInput).value;
    const cityDatalist = document.getElementById(idDatalist);
    cityDatalist.innerHTML = '';

    const foundCities = CITY_MAP.get(countryInput);

    if (foundCities == undefined) {
        return;
    }

    for (const city of foundCities) {
        cityDatalist.appendChild(city);
    }
}

