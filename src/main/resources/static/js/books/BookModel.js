function BookModel(cookieFinder) {
    this.onSearch = new EventEmitter();
    this.onCreate = new EventEmitter();
    this.onUpdate = new EventEmitter();
    this.onError = new EventEmitter();

    this.cookieFinder = cookieFinder;

}

BookModel.prototype.search = async function (userFilter) {

    let requestsId = 0;

    let lastRequestId = 0;

    let currentRequestId;

    let that = this;
    let data = $.param(userFilter);
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    $.ajax({
        type: "GET",        //because method GET send parameters in url, encoding JSON make url in wrong format
        beforeSend: function (request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/books",
        dataType: "json",
        contentType: 'application/json',
        data: data,
        success: function (data, textStatus, request) {
            // localStorage.setItem("userToken", request.getResponseHeader("X-CSRF-TOKEN"));
            // clearErrorMessageForm();
            // overfillProductsTable(data);
            // that.onSearch.notify(data);
            let id = ++requestsId;
            console.log("Hello Book Search")
            console.log(data)
            if (lastRequestId < id) {
                console.log("Notify")
                lastRequestId = id;
                currentRequestId = 0;
                that.onSearch.notify(data);
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            that.onError.notify(JSON.parse(jqXHR.responseText));
        }
    });

    // let requestsId = 0;
    //
    // let lastRequestId = 0;
    //
    // let currentRequestId;
    //
    // let that = this;
    //
    // let id = ++requestsId;

    // let test = this.find(userFilter).then(responseData => {
    //     currentRequestId = id;
    //     filteredBooks = responseData;
    //     console.log(responseData)
    //
    // });

    // this.find(userFilter).then(data => {
    //     console.log(data);
    //     if (lastRequestId < currentRequestId) {
    //         console.log("Notify")
    //         lastRequestId = currentRequestId;
    //         currentRequestId = 0;
    //         this.onSearch.notify(data);
    //     }
    // })

    // let response = (function () {


    // for (let book of that.books) {
    //
    //     if (isFiltered(userFilter, book[1])) {
    //         filteredBooks.push(book);
    //     }
    //
    // }

    // })();

    // console.log(filteredBooks)
    //
    // if (lastRequestId < currentRequestId) {
    //     lastRequestId = currentRequestId;
    //     currentRequestId = 0;
    //     this.onSearch.notify(response);
    // }


}


BookModel.prototype.createBook = async function (userBook) {
    let that = this;

    let formData = new FormData();
    let file = userBook.previewImgFile;

    let bookString = JSON.stringify(userBook);
    let book = new Blob([bookString], {
        type: 'application/json'
    })

    formData.append("imageFile", file)
    formData.append("book", book)

    let csrfToken = that.cookieFinder.getCookie("XSRF-TOKEN");
    $.ajax({
        type: "POST",
        beforeSend: function (request) {
            request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
        },
        url: "v1/books",
        contentType: false,
        processData: false,
        enctype: 'multipart/form-data',
        data: formData,
        success: function (data, textStatus, request) {
            that.onCreate.notify(data);
        },
        error: function (xhr, ajaxOptions, thrownError) {
            that.onError.notify(JSON.parse(xhr.responseText));
        }

    });

}

BookModel.prototype.searchById = function (bookId) {

    let that = this;
    return new Promise((resolve, reject) => {
            that.test1(bookId).then(book => {
                // test2(bookId).then(tags => {
                //     book.tags = tags
                resolve(book);
                // })
            })
        }
    );

}

BookModel.prototype.test1 = function (bookId) {
    let url = "v1/books/" + bookId;
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    return $.when(
        $.ajax({
            type: "GET",
            beforeSend: function (request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: url,
            dataType: "json",
            contentType: 'application/json'
        })
    );
}


// BookModel.prototype.updateTagsList = async function (bookBean) {
//     console.log("BookBean on update TAGS");
//     console.log(bookBean);
//     let url = "v1/books/" + bookBean.id +"/tags";
//     let jsonData = JSON.stringify(bookBean);
//     console.log("JSON DATA")
//     console.log(jsonData)
//     $.ajax({
//             type: "PUT",
//             // beforeSend: function (request) {
//             //     request.setRequestHeader("X-CSRF-TOKEN", localStorage.getItem("userToken"));
//             // },
//             url: url,
//             dataType: "json",
//             contentType: 'application/json',
//             data: jsonData,
//             success: function (data, textStatus, request) {
//                 console.log("SUCCESS!!!!!");
//             },
//
//             error: function (jqXHR, textStatus, errorThrown) {
//                 console.log("EROROROROROROROROR")
//                 console.log(errorThrown)
//
//                 // handleError(jqXHR);
//             }
//         }
//     );
//
// }

BookModel.prototype.update = async function (bookBean) {

    let that = this;
    console.log("UPDATE BOK")
    console.log(bookBean)

    bookBean.tags = bookBean.tags.map((tag) => {
        return tag.id
    });


    let url = "v1/books/" + bookBean.id;
    let stringData = JSON.stringify(bookBean);
    let csrfToken = this.cookieFinder.getCookie("XSRF-TOKEN");

    console.log(stringData)
    $.ajax({
            type: "PUT",
            beforeSend: function (request) {
                request.setRequestHeader("X-XSRF-TOKEN", csrfToken);
            },
            url: url,
            dataType: "json",
            contentType: 'application/json',
            data: stringData,
            success: function (data, textStatus, request) {
                that.onUpdate.notify(bookBean);
            },

            error: function (jqXHR, textStatus, errorThrown) {
                that.onError.notify(JSON.parse(jqXHR.responseText));
            }
        }
    );
}