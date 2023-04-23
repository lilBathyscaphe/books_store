function HistoryMessageCreator() {
    // this.historyModel = historyModel;
    // this.bookModel = bookModel;
    // this.historyView = historyView;
    // this.historySmallView = historySmallView;

    this.classManager = new ClassManager();

    this.historyMessagesContainer = document.getElementsByClassName("history_page_content")[0];


    this.addBookMessage = document.getElementsByClassName("add_new_book_history_message")[0];
    this.searchBookMessage = document.getElementsByClassName("search_book_history_message")[0];
    this.updateRatingMessage = document.getElementsByClassName("book_rating_change_history_message")[0];
    this.createTagsMessage = document.getElementsByClassName("create_tag_history_message")[0];
    this.addTagToBookMessage = document.getElementsByClassName("add_tag_to_book_history_message")[0];
    this.removeTagFromBoomMessage = document.getElementsByClassName("remove_tag_from_book_history_message")[0];


}


HistoryMessageCreator.prototype.init = function () {
    let that = this;
    //
    // this.historyModel.onCreateEvent.subscribe((historyMessage) => {
    //     let newMessage = that.createMessage(historyMessage);
    //     this.historySmallView.showMessage(newMessage, historyMessage);
    //
    // })
    //
    // this.historyModel.onGetMessages.subscribe((historyPages) => {
    //     this.historyMessagesContainer.innerHTML = "";
    //     let messagesArray = historyPages.content;
    //     for (const historyMessage of messagesArray) {
    //         that.generateMessageForBothView(historyMessage);
    //     }
    // })

    // this.historyModel.onHistoryUpdate.subscribe((historyMessages) => {
    //     console.log("Get messages")
    //     console.log(historyMessages)
    //     this.historyMessagesContainer.innerHTML = "";
    //     let arrayMessages = [];
    //     if (historyMessages.content !== undefined) {
    //         arrayMessages = historyMessages.content;
    //     } else {
    //         arrayMessages = Array.from(historyMessages);
    //     }
    //
    //     console.log("Array Messages")
    //     console.log(arrayMessages)
    //
    //     // if (Array.isArray(historyMessages.content)) {
    //     //     let historyBeans = historyMessages.content;
    //     //     console.log("HistoryPage Content")
    //     //     console.log(historyBeans)
    //     //     for (const historyBeanElement of historyBeans) {
    //     //         that.generateMessage(historyBeanElement);
    //     //     }
    //     // } else {
    //     //     console.log("History Pagging check")
    //     //     console.log(historyMessages.content)
    //     // }
    // });
    //
    // let historyFilter = new HistoryFilter(10, 0);
    //
    // this.historyModel.getMessages(historyFilter);
}

// HistoryMessageCreator.prototype.generateMessageForBothView = function (historyBean) {
//     let newMessage = this.createMessage(historyBean);
//
//     this.historySmallView.showMessage(newMessage, historyBean);
//     this.historyView.showMessage(newMessage, historyBean);
// }




HistoryMessageCreator.prototype.createMessage = function (bean) {
    let eventType = bean.eventType;
    let book = bean.book;
    let tag = bean.tag;

    let newMessage;

    if (eventType === "add_book") {
        newMessage = this.cloneMessage(this.addBookMessage);
        fillBookName(newMessage, book);
        fillBookAuthor(newMessage, book);
    } else if (eventType === "search_book") {
        newMessage = this.cloneMessage(this.searchBookMessage);
        let searchQuery = bean.searchQuery;
        fillSearchQuery(newMessage, searchQuery);
    } else if (eventType === "update_rating") {
        newMessage = this.cloneMessage(this.updateRatingMessage);
        fillBookName(newMessage, book);
        fillBookAuthor(newMessage, book);
        fillBookRating(newMessage, book);
    } else if (eventType === "add_tag_to_book") {
        newMessage = this.cloneMessage(this.addTagToBookMessage);
        fillTags(newMessage, tag);
        fillBookName(newMessage, book);
        fillBookAuthor(newMessage, book);
    } else if (eventType === "remove_tag_from_book") {
        newMessage = this.cloneMessage(this.removeTagFromBoomMessage);
        fillTags(newMessage, tag);
        fillBookName(newMessage, book);
        fillBookAuthor(newMessage, book);
    } else if (eventType === "create_tag") {
        newMessage = this.cloneMessage(this.createTagsMessage);
        fillTags(newMessage, tag);
    }
    return newMessage;
}

HistoryMessageCreator.prototype.cloneMessage = function (message) {
    let newMessage = message.cloneNode(true);
    this.classManager.removeClass(newMessage, "hidden");
    return newMessage;
}

function fillTags(element, tag) {
    let bookNameContainer = element.querySelector("b[tag]");
    bookNameContainer.textContent += tag.name + " ";
}

function fillBookName(element, book) {
    let bookNameContainer = element.querySelector("b[book_name]");
    bookNameContainer.textContent = book.name;
}

function fillBookAuthor(element, book) {
    let bookAuthorContainer = element.querySelector("b[book_author]");
    bookAuthorContainer.textContent = book.author;
}

function fillBookRating(element, book) {
    let ratingContainer = element.querySelector("b[book_rating]");
    ratingContainer.textContent = book.rate;
}

function fillSearchQuery(element, searchQuery) {
    let searchQueryContainer = element.querySelector("b[search_query]");
    searchQueryContainer.textContent = searchQuery;
}
