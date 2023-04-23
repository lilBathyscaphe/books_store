function NavigationView(bookModel, historyModel, tagModel,bookView) {
    this.bookModel = bookModel;
    this.tagModel = tagModel;
    this.historyModel = historyModel;
    this.bookView = bookView;

    this.classManager = new ClassManager();

    this.mainMenuBtns = document.getElementsByClassName("main_menu_btns")[0];

    this.pages = document.getElementsByClassName("page_content")[0];

    this.browsePage = document.getElementsByClassName("books_page_container")[0];
    this.historyPage = document.getElementsByClassName("history_page_container")[0];

    this.historyMenuBtn = document.getElementsByClassName("main_menu_history")[0];
    this.browseMenuBtn = document.getElementsByClassName("main_menu_browse")[0];
   
}


NavigationView.prototype.init = function () {


    this.browseMenuBtn.addEventListener('click', () => {
        this.classManager.addClassToAll(this.pages, "hidden");
        this.classManager.removeClass(this.browsePage, "hidden");

        this.classManager.removeClassFromAll(this.mainMenuBtns, 'active_main_menu_row');

        this.classManager.addClass(this.browseMenuBtn, "active_main_menu_row");
        this.bookModel.search(this.bookView.userFilter);
        this.tagModel.read();
    });


    this.historyMenuBtn.addEventListener('click', () => {
        this.classManager.addClassToAll(this.pages, "hidden");
        this.classManager.removeClass(this.historyPage, "hidden");

        this.classManager.removeClassFromAll(this.mainMenuBtns, 'active_main_menu_row');
        this.classManager.addClass(this.historyMenuBtn, "active_main_menu_row");

        let historyFilter = new HistoryFilter(14,0, "id,DESC");
        this.historyModel.getMessages(historyFilter);
    });
}
