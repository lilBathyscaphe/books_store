(function () {
    let cookieFinder = new CookieFinder();

    let historyModel = new HistoryModel(cookieFinder);
    let bookModel = new BookModel(cookieFinder);
    let tagModel = new TagModel(cookieFinder);

    let tagView = new TagView(bookModel, tagModel, historyModel);
    let bookView = new BookView(bookModel, tagView, historyModel);

    let smallTagView = new SmallTagView(bookModel, tagModel);

    let navigationView = new NavigationView(bookModel, historyModel,tagModel, bookView);
    let addBookView = new AddBookView(bookModel, historyModel);

    let historyMessageCreator = new HistoryMessageCreator();
    let historyView = new HistoryView(historyModel,historyMessageCreator);
    let historySmallView = new HistorySmallView(historyModel, bookModel,historyMessageCreator);

    let bookModalView = new BookModalView(bookModel, tagModel, historyModel);


    let authorization = new Authorization(cookieFinder);

    let authorizationView = new AuthorizationView(authorization,historyModel);

    let exceptionHandler = new ErrorHandler(bookModel, historyModel, tagModel);

    authorizationView.init();

    addBookView.init();
    bookView.init();
    tagView.init();
    bookModalView.init();
    smallTagView.init();


    navigationView.init();
    historyView.init();
    historySmallView.init();
    exceptionHandler.init();

})();
