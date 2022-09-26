const URL_USER_RESOURCE = "http://localhost:8095/api/v1/users";
const USER_CONTAINER = document.getElementById('user-container-id');
const PAGE = {
    limit : null,
    offset : null,
    desc : false,
    columnName : 'DATE_REGISTRATION',
    addToFormData: function(formData){
        if (this.limit != null) {
            formData.append('limit', this.limit);
        }
        if (this.offset != null) {
            formData.append('offset', this.offset);
        }
        formData.append('desc', this.desc);
        formData.append('userSortColumn', this.columnName);
    }
}


async function updateUserContainer(){
    const formData = await getFormDataFromFilter();
    PAGE.addToFormData(formData);
    const users = (await getEntities(URL_USER_RESOURCE  + formDataToUrlParameter(formData)
        )).userDtos;

    USER_CONTAINER.innerHTML = '';
    users.forEach(element => {
        USER_CONTAINER.innerHTML += userToDiv(element);
    });
}

function changeSort(columnName){
    PAGE.columnName = columnName;
    updateUserContainer();
}



function changeDirectionSort(){
    PAGE.desc = !PAGE.desc;
    updateUserContainer();
}

function userToDiv(user){
    return `
        <div class="user-box">
          <div class="user-data-container">
            <div class="user-data-box">
              <h5>Firstname</h5>
              <p>${user.firstname}</p>
            </div>
            <div class="user-data-box">
              <h5>Lastname</h5>
              <p>${user.lastname}</p>
            </div>
            <div class="user-data-box">
              <h5>Phone number</h5>
              <p>${user.phoneNumber}</p>
            </div>
          </div>

          <div class="user-data-container">
            <div class="user-data-box">
              <h5>Date registration</h5>
              <p>${user.dateRegistration.replace('T',' ').substr(0,16)}</p>
            </div>
            <div class="user-data-box">
              <h5>Status</h5>
              <p>${toTitleCase(user.userState)}</p>
            </div>
            <button class="activety-button ${user.active ? '' : 'inactive-button'}">
                ${user.active ? 'Active' : 'Inactive'}
            </button>
          </div>
          <div class="user-data-text-box">
            <p>Email : <samp>${user.email}</samp></p>
            <p>City : <samp>>${user.city}</samp></p>
            <p>Country : <samp>${user.country}</samp></p>
            <p>Gender : <samp>${user.male ? 'Man' : 'Female'}</samp></p>
            <p>Role : <samp>${toTitleCase(user.role)}</samp></p>

          </div>
        </div>
    
    `
}

function toTitleCase(txt) {
    txt.replace('_',' ');
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
}