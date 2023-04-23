function AddBookView(bookModel, historyModel) {
    this.bookModel = bookModel;
    this.historyModel = historyModel;

    this.classManager = new ClassManager();

    this.openModalWindowBtn = document.getElementsByClassName("main_menu_add_btn")[0];

    this.modalWindow = document.getElementsByClassName("add_popup")[0];
    this.popUpOverlay = document.getElementsByClassName("popup_overlay")[0];

    this.addPopUpBookName = document.getElementsByClassName("add_popup_cell_book_name")[0];
    this.addPopUpBookAuthor = document.getElementsByClassName("add_popup_cell_author")[0];
    this.addPopUpBookImage = document.getElementsByClassName("add_popup_cell_image")[0];


    this.addPopUpButton = document.getElementsByClassName("add_popup_submit")[0];
    this.addPopUpClose = document.getElementsByClassName("add_popup_close")[0];
    this.addPopUpCancel = document.getElementsByClassName("add_popup_cancel")[0];

}

AddBookView.prototype.init = function () {
    this.addBookListener();
    this.openModalWindowListener();
    this.closeModalWindowListeners();
}

AddBookView.prototype.closeModalWindowListeners = function () {
    this.addPopUpClose.addEventListener('click', (event) => {
        event.preventDefault();                 //PREVENTION. Refresh page after press btn/input/a
        this.closeModalWindow();
    });
    this.addPopUpCancel.addEventListener('click', (event) => {
        event.preventDefault();                 //PREVENTION. Refresh page after press btn/input/a
        this.closeModalWindow();
    });
}

AddBookView.prototype.openModalWindowListener = function () {
    this.openModalWindowBtn.addEventListener('click', (event) => {
        event.preventDefault();                 //PREVENTION. Refresh page after press btn/input/a
        this.classManager.removeClass(this.modalWindow, "hidden");
        this.classManager.addClass(this.popUpOverlay, "active");
    });
}

AddBookView.prototype.addBookListener = function () {
    this.addPopUpButton.addEventListener('click', (event) => {
        event.preventDefault();                 //PREVENTION. Refresh page after press btn/input/a
        let userBook = this.parseUserInput();

        this.bookModel.createBook(userBook);

        this.closeModalWindow();
    });

    this.bookModel.onCreate.subscribe((createdBook) => {
        this.historyModel.createAddBookEvent(createdBook);
    });
}

AddBookView.prototype.closeModalWindow = function () {
    this.classManager.addClass(this.modalWindow, "hidden");
    this.classManager.removeClass(this.popUpOverlay, "active");
    this.clearUserParameters();
}

AddBookView.prototype.clearUserParameters = function () {
    this.addPopUpBookName.value = "";
    this.addPopUpBookAuthor.value = "";
    this.addPopUpBookImage.value = "";
}


AddBookView.prototype.parseUserInput = function () {
    let userBook = new BookBean();

    let bookName = this.addPopUpBookName.value;
    userBook.name = bookName;

    let bookAuthor = this.addPopUpBookAuthor.value;
    userBook.author = bookAuthor;

    let bookImg = this.addPopUpBookImage.files[0];
    // userBook.previewImg = "uploadedFiles/" + bookImg.name;
    userBook.previewImgFile = bookImg;

    return userBook;
}
