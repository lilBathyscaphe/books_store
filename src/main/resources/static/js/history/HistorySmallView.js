function HistorySmallView(historyModel, bookModel, historyMessageCreator) {
    this.historyModel = historyModel;
    this.bookModel = bookModel;
    this.historyMessageCreator = historyMessageCreator;

    this.historySmallDiv = document.getElementsByClassName("main_menu_last_action_list")[0];

    this.addBookMessage = document.getElementsByClassName("add_new_book_history_message")[0];
    this.searchBookMessage = document.getElementsByClassName("search_book_history_message")[0];
    this.updateRatingMessage = document.getElementsByClassName("book_rating_change_history_message")[0];
}

HistorySmallView.prototype.init = function () {
    let that = this;

    this.historyModel.onCreateEvent.subscribe((historyBean)=>{
        let message = that.createMessage(historyBean);
        that.appendToContainerStart(message);
    })

    this.historyModel.onGetMessages.subscribe((historyPages)=>{
        let beanArray = historyPages.content;
        for (const bean of beanArray) {
            let message = that.createMessage(bean);
            that.appendToContainer(message);
        }
    })

}

HistorySmallView.prototype.createMessage = function (historyBean){
    let newMessage = this.historyMessageCreator.createMessage(historyBean);
    return this.createMessageDiv(newMessage, historyBean);
}

HistorySmallView.prototype.createMessageDiv = function (element, historyBean) {
    
    let eventDiv = document.createElement("div");
    eventDiv.className = "main_menu_last_action";

    let eventIcon = document.createElement("div");
    eventIcon.className = "last_added_icon";
    eventDiv.appendChild(eventIcon);

    let eventInfo = document.createElement("div");
    eventInfo.className = "last_added_book";

    let book = document.createElement("div");
    book.className = "last_added";

    book.appendChild(element.cloneNode(true));
    

    eventInfo.appendChild(book);

    let eventTime = document.createElement("div");
    eventTime.className = "last_added_time";

    this.createTimeDiv(eventTime, historyBean);

    setInterval(() => {
        this.createTimeDiv(eventTime, historyBean);
    }, 60000);

    eventInfo.appendChild(eventTime);

    eventDiv.appendChild(eventInfo);

    return eventDiv;
    // this.appendToStart(eventDiv);
}

HistorySmallView.prototype.createTimeDiv = function (eventTime, historyBean) {
    let milliseconds = this.calculateTime(historyBean);
    eventTime.textContent = Math.floor(milliseconds / 60000) + " minutes ago";
}

HistorySmallView.prototype.calculateTime = function (lastEvent) {
    return Date.now() - Date.parse(lastEvent.eventDate);
}

HistorySmallView.prototype.appendToContainerStart = function (eventDiv) {
    this.historySmallDiv.insertBefore(eventDiv,this.historySmallDiv.firstChild);

    if (this.historySmallDiv.children.length > 2) {
        this.historySmallDiv.removeChild(this.historySmallDiv.lastChild);
    }
}

HistorySmallView.prototype.appendToContainer = function (eventDiv) {
    this.historySmallDiv.appendChild(eventDiv);

    if (this.historySmallDiv.children.length > 2) {
        this.historySmallDiv.removeChild(this.historySmallDiv.lastChild);
    }
}