function Authorization(cookieFinder) {
    this.onError = new EventEmitter();
    this.onSuccess = new EventEmitter();
    this.onLogout = new EventEmitter();

    this.cookieFinder = cookieFinder;
}

Authorization.prototype.authorize = function (authorizeData) {
    let that = this;

    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");
    console.log("CSRF TOKEN")
    console.log(csrfToken)

    let stringData = JSON.stringify(authorizeData);
    $.ajax({
        type: "POST",
        beforeSend: function (request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/user/login",
        data: stringData,
        dataType: "json",
        contentType: 'application/json',
        success: function (data) {
            console.log("Success!!!")
            that.onSuccess.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let responseObject = JSON.parse(jqXHR.responseText);
            that.onError.notify(responseObject.message);
        }
    });

}

Authorization.prototype.logout = function () {
    let that = this;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    $.ajax({
        url: "v1/user/logout",
        type: "POST",
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        contentType: 'application/json',
        statusCode: {
            200:
                function () {
                    that.onLogout.notify("You are logout!")
                }
        }
    });
}

Authorization.prototype.getUser = function () {
    let that = this;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");
    console.log("COOOKIE")
    console.log(document.cookie)
    $.ajax({
        type: "GET",
        beforeSend: function (request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/user",
        dataType: "json",
        contentType: 'application/json',
        statusCode: {
            401:
                function () {
                    that.onError.notify("You are not authorized. Please login.");
                },
            200:
                function (data) {
                    that.onSuccess.notify(data);

                }
        }
        // success: function (data, textStatus, request) {
        //     console.log("SUCCESS")
        //     that.onSuccess.notify(data);
        // },
        // error: function (jqXHR, textStatus, errorThrown) {
        //     console.log("ERROR")
        //     console.log(jqXHR.responseText)
        //     console.log(jqXHR)
        //     let responseObject = JSON.parse(jqXHR.responseText);
        //     that.onError.notify(responseObject.message);
        // }
    })


}






