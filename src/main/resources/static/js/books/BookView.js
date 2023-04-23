function BookView(bookModel, tagView, historyModel) {
    this.bookModel = bookModel;
    this.historyModel = historyModel;

    this.userFilter = new UserFilter();

    this.classManager = new ClassManager();

    this.booksDiv = document.getElementsByClassName('books')[0];
    this.searchBar = document.getElementById('input_search');

    this.errorMessageContainer = document.getElementsByClassName("error_message_container")[0];

    this.pageMenu = document.getElementsByClassName("page_menu")[0];

}

BookView.prototype.init = function () {

    let that = this;

    this.bookModel.onSearch.subscribe((books) => {
        this.clearErrorMessageContainer();
        that.displayBooks(books);
    });

    this.bookModel.onCreate.subscribe((newBook) => {
        this.clearErrorMessageContainer();
        that.displayBook(newBook);
    });

    this.bookModel.onUpdate.subscribe((updatedBook) => {
        this.clearErrorMessageContainer();
        this.bookModel.search(this.userFilter);
    });

    // this.bookModel.onError.subscribe((exceptionMessage) =>{
    //     let message = this.errorMessageContainer.getElementsByClassName("message")[0];
    //     console.log(exceptionMessage.message);
    //     message.textContent = exceptionMessage.message;
    //     alert(exceptionMessage.message);
    // })

    this.searchBar.addEventListener('input', (searchBarEvent) => {
        this.clearErrorMessageContainer();
        this.fillUserFilter(searchBarEvent);
        this.bookModel.search(this.userFilter);

        this.historyModel.createSearchBookEvent(searchBarEvent.target.value);

    });

    let filterBtn = document.querySelectorAll(".filter_menu_btn");
    filterBtn.forEach(btn => {
        btn.addEventListener('click', (clickEvent) => {
            this.clearErrorMessageContainer();
            this.enableFilterBtn(clickEvent.target);
        })
    });

}


BookView.prototype.clearErrorMessageContainer = function () {
    let errorMessage = this.errorMessageContainer.getElementsByClassName("message")[0];
    errorMessage.textContent = "";
}

BookView.prototype.enableFilterBtn = function (enableBtn) {
    this.classManager.removeClassFromAll(this.pageMenu, "active_filter_menu_btn");

    this.classManager.addClass(enableBtn, "active_filter_menu_btn");

    this.userFilter.filterType = enableBtn.textContent.trim()
        .replaceAll(" ", "_").toUpperCase();
    this.bookModel.search(this.userFilter);
}



BookView.prototype.fillUserFilter = function (searchBarEvent) {
    const searchString = searchBarEvent.target.value;
    this.userFilter.searchQuery = searchString;
}


BookView.prototype.displayBooks = function (books) {
    this.booksDiv.innerHTML = "";

    for (let book of books) {
        console.log(book)
        this.displayBook(book);
    }
}

BookView.prototype.displayBook = function (book) {
    const bookDiv = document.createElement("div");
    bookDiv.className = "book";
    bookDiv.setAttribute("book_id", book.id);

    const imgDiv = document.createElement("div");
    imgDiv.className = "book_preview_container";

    const preview = document.createElement("img");
    preview.src = book.previewImg;
    preview.className = "book_preview";

    imgDiv.appendChild(preview);
    bookDiv.appendChild(imgDiv);


    const nameDiv = document.createElement("div");
    nameDiv.className = "book_name";
    nameDiv.appendChild(document.createTextNode(book.name));

    bookDiv.appendChild(nameDiv);


    const authorDiv = document.createElement("div");
    authorDiv.className = "book_author";
    authorDiv.appendChild(document.createTextNode(book.author));

    bookDiv.appendChild(authorDiv);


    const rateUl = this.createRateUl(book);
    bookDiv.appendChild(rateUl);


    this.booksDiv.appendChild(bookDiv);
}

BookView.prototype.createRateUl = function (book) {
    const rateUl = document.createElement("ul");
    rateUl.className = "rating";
    if (book.rate === 0) {
        this.classManager.addClass(rateUl, "not_rated");
    }
    this.createRatingStars(rateUl, book);

    this.addRatingListener(rateUl, book);

    return rateUl;
}

BookView.prototype.createRatingStars = function (rateUl, book) {
    for (let i = 1; i < 6; i++) {
        let li = document.createElement("li");
        li.className = "rating_item";
        li.setAttribute("book_rate", i);
        if (i === book.rate) {
            this.classManager.addClass(li, "active");
        }
        rateUl.appendChild(li);
    }
}

BookView.prototype.addRatingListener = function (rateContainer, book) {
    rateContainer.addEventListener("click", clickedRatingStar => {
        this.classManager.removeClass(rateContainer, "not_rated");

        let clickedStar = clickedRatingStar.target;
        book.rate = +clickedStar.getAttribute("book_rate");


        this.bookModel.update(book);
        this.historyModel.createUpdateRatingBookEvent(book);
    });
}


