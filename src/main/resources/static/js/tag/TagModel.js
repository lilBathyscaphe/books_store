function TagModel(cookieFinder) {
    this.onRead = new EventEmitter();
    this.onCreate = new EventEmitter();
    this.onError = new EventEmitter();

    this.cookieFinder = cookieFinder;
}


TagModel.prototype.create = async function (tagName) {
    let that = this;
    let tagBean = new TagBean();
    tagBean.name = tagName;
    tagBean.bulletColor = generateRandomColor();
    let jsonData = JSON.stringify(tagBean);
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");
        $.ajax({
            type: "POST",
            beforeSend: function(request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: "v1/tags",
            dataType: "json",
            contentType: 'application/json',
            data: jsonData,
            success: function (data, textStatus, request) {
                console.log("Success")
                console.log(data)
                that.onCreate.notify(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR.responseText)
                that.onError.notify(JSON.parse(jqXHR.responseText));
            }
        })
}

TagModel.prototype.read = async function () {
    let that = this;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");
    $.ajax({
        type: "GET",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/tags?sort=name",
        dataType: "json",
        contentType: 'application/json',
        // data: userFilter,
        success: function (data, textStatus, request) {
            console.log("Read TAGS")
            console.log(data);
            that.onRead.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });
}

TagModel.prototype.find = async function (tagId) {
    let that = this;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    let url = "v1/tags/" + tagId;
    return $.when(
        $.ajax({
            type: "GET",
            beforeSend: function(request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: url,
            data:+tagId,
            error: function (jqXHR, textStatus, errorThrown) {
                that.onError.notify(JSON.parse(jqXHR.responseText));
            }
        })
    );

}

function generateRandomColor() {
    return "#" + ((1 << 24) * Math.random() | 0).toString(16);
}