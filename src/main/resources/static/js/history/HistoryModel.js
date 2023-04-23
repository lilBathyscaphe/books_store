function HistoryModel(cookieFinder) {
    this.onGetMessages = new EventEmitter();
    this.onCreateEvent = new EventEmitter();
    this.onError = new EventEmitter();

    this.cookieFinder = cookieFinder;
}

HistoryModel.prototype.getMessages = function (historyFilter) {
    let that = this;
    let data = $.param(historyFilter);

    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");
    $.ajax({
        type: "GET",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/history",
        dataType: "json",
        data: data,
        contentType: 'application/json',
        success: function (data) {
            that.onGetMessages.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });
}

HistoryModel.prototype.createAddBookEvent = function (book) {
    let that = this;

    let historyBean = new HistoryBean();
    historyBean.eventType = "add_book";
    historyBean.book = book;
    let jsonData = JSON.stringify(historyBean);

    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    $.ajax({
        type: "POST",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/history",
        dataType: "json",
        contentType: 'application/json',
        data: jsonData,
        success: function (data) {
            that.onCreateEvent.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });

}


HistoryModel.prototype.createSearchBookEvent = function (searchQuery) {
    let searchBean = new HistoryBean();
    searchBean.eventType = "search_book";
    searchBean.searchQuery = searchQuery;
    let jsonData = JSON.stringify(searchBean);

    let that = this;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    $.ajax({
        type: "POST",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/history",
        dataType: "json",
        contentType: 'application/json',
        data: jsonData,
        success: function (data) {
            that.onCreateEvent.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });

}


HistoryModel.prototype.createUpdateRatingBookEvent = function (book) {
    let that = this;

    let updatedBean = new HistoryBean();
    updatedBean.eventType = "update_rating";
    updatedBean.book = book;
    let jsonData = JSON.stringify(updatedBean);
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    $.ajax({
        type: "POST",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/history",
        dataType: "json",
        contentType: 'application/json',
        data: jsonData,
        success: function (data) {
            that.onCreateEvent.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });
}



HistoryModel.prototype.createNewTagsEvent = function (tag) {
    let that = this;

    let historyBean = new HistoryBean();
    historyBean.eventType = "create_tag";
    historyBean.tag = tag;
    let jsonData = JSON.stringify(historyBean);

    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");


    $.ajax({
        type: "POST",
        beforeSend: function(request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/history",
        dataType: "json",
        contentType: 'application/json',
        data: jsonData,
        success: function (data) {
            that.onCreateEvent.notify(data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });
}


HistoryModel.prototype.createAddTagsToBookEvent = function (book, newTags) {

    let that = this;

    for (const tag of newTags) {
        let historyBean = new HistoryBean();
        historyBean.eventType = "add_tag_to_book";
        historyBean.tag = tag;
        historyBean.book = book;

        let stringJsonData = JSON.stringify(historyBean);
        let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

        $.ajax({
            type: "POST",
            beforeSend: function(request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: "v1/history",
            dataType: "json",
            contentType: 'application/json',
            data: stringJsonData,
            success: function (data) {
                that.onCreateEvent.notify(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                that.onError.notify(JSON.parse(jqXHR.responseText));
            }
        });

    }
}

HistoryModel.prototype.createRemoveTagsFromBookEvent = function (book, removedTags) {
    let that = this;

    for (const tag of removedTags) {
        let historyBean = new HistoryBean();
        historyBean.eventType = "remove_tag_from_book";
        historyBean.tag = tag;
        historyBean.book = book;

        let stringJsonData = JSON.stringify(historyBean);
        let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

        $.ajax({
            type: "POST",
            beforeSend: function(request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: "v1/history",
            dataType: "json",
            contentType: 'application/json',
            data: stringJsonData,
            success: function (data) {
                that.onCreateEvent.notify(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                that.onError.notify(JSON.parse(jqXHR.responseText));
            }
        });

    }
}

