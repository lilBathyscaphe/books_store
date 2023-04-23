function AuthorizationView(authorization, historyModel) {
    this.authorization = authorization;
    this.historyModel = historyModel;

    this.classManager = new ClassManager();

    this.errorMessageContainer = document.getElementsByClassName("authorization_error_message")[0];
    this.headerUsernameContainer = document.getElementsByClassName("header_username")[0];

    this.mainPageContainer = document.getElementsByClassName("main_page")[0];
    this.authorizationForm = document.getElementById("authorizationForm");
    this.addBookButton = document.getElementsByClassName("main_menu_add_btn")[0];

    this.browseMenuBtn = document.getElementsByClassName("main_menu_browse")[0];
}

AuthorizationView.prototype.showUserInfo = function (userInfo) {
    this.headerUsernameContainer.textContent = userInfo.firstname + " " + userInfo.lastname;
}
AuthorizationView.prototype.init = function () {

    // let that = this;

    this.authorization.getUser();

    this.authorization.onSuccess.subscribe((data) => {
        this.showUserInfo(data);
        this.manageRoleBasedButton(data.roles);
        this.showMainPage();
    })

    this.authorization.onError.subscribe((data) => {
        this.showAuthorizationForm();
        console.log("Authorization error")
        console.log(data)
        this.errorMessageContainer.textContent = data;
    })

    this.authorization.onLogout.subscribe((data) => {
        console.log("LOGOUT FUNC")
        console.log(data)
        this.showAuthorizationForm();
        this.errorMessageContainer.textContent = data;
    })

    $("#authorizationForm").submit(() => {
        let data = getFormData($("#authorizationForm"));
        this.authorization.authorize(data);
        return false;
    })

    $(".header_user_profile").click(() => {
        this.authorization.logout();
    })


}

AuthorizationView.prototype.manageRoleBasedButton = function (userRoles) {
    let userAuthorities = userRoles.map(role => {
        return role.authority;
    })

    if (userAuthorities.includes("ROLE_ADMIN")) {
        this.classManager.removeClass(this.addBookButton, "hidden");
    } else {
        this.classManager.addClass(this.addBookButton, "hidden");
    }
}

AuthorizationView.prototype.showAuthorizationForm = function () {
    this.classManager.addClass(this.mainPageContainer, "hidden");
    this.classManager.removeClass(this.authorizationForm, "hidden");
}

AuthorizationView.prototype.showMainPage = function () {
    console.log("ShowMainPage")
    this.classManager.addClass(this.authorizationForm, "hidden");
    this.classManager.removeClass(this.mainPageContainer, "hidden");
    this.browseMenuBtn.click();
    let historyFilter = new HistoryFilter(2, 0, "id,DESC");
    this.historyModel.getMessages(historyFilter);
}

function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}