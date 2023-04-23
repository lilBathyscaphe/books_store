function HistoryView(historyModel,historyMessageCreator) {
    this.historyModel = historyModel;
    this.historyMessageCreator = historyMessageCreator;

    this.classManager = new ClassManager();

    this.historyPageContent = document.getElementsByClassName("history_page_content")[0];
    this.historyPaggination = document.getElementsByClassName("history_pagination")[0];

    this.historyPagginationItems = document.querySelectorAll(".page_item");

    this.historyMessagesContainer = document.getElementsByClassName("history_page_content")[0];

    this.totalPages;
}

HistoryView.prototype.init = function () {
    let that = this;
    this.historyModel.onGetMessages.subscribe((historyPages) => {
        that.configureNavigationButtons(historyPages);
        this.historyMessagesContainer.innerHTML = "";
        let beanArray = historyPages.content;
        for (const bean of beanArray) {
            // that.generateMessageForBothView(historyMessage);
            let newMessage = that.historyMessageCreator.createMessage(bean)
            that.showMessage(newMessage,bean);
        }
    })


}

HistoryView.prototype.configureNavigationButtons = function (historyPages) {
    this.totalPages = historyPages.totalPages;
    this.createNavigationButtons(historyPages.number);
    this.addEventListenerToPageButtons();
}

HistoryView.prototype.addEventListenerToPageButtons = function () {
    let that = this;
    this.historyPagginationItems = document.querySelectorAll(".page_item");

    this.historyPagginationItems.forEach((button) => {
        button.addEventListener("click", (pressedPage) => {
            let page = +pressedPage.target.getAttribute("id");
            that.sendRequestToHistory(page);
            that.createNavigationButtons(page)
        })
    });

}

HistoryView.prototype.sendRequestToHistory = function (pressedPageNumber) {
    let historyFilter = new HistoryFilter(14, pressedPageNumber, "id,DESC");
    this.historyModel.getMessages(historyFilter)
}


HistoryView.prototype.createNavigationButtons = function (pressedPageNumber) {
    this.historyPaggination.innerHTML = "";

    // let pressedPageNumber = historyPages.number;
    let minButtonNumber;
    let maxButtonNumber;

    let buttonsCount;
    if (this.totalPages > 10) {
        buttonsCount = 10;
        if (pressedPageNumber > this.totalPages - 5) {
            maxButtonNumber = this.totalPages;
        } else {
            maxButtonNumber = pressedPageNumber + 5;
        }
        if (pressedPageNumber < 5) {
            minButtonNumber = 0;
        } else {
            minButtonNumber = pressedPageNumber - 5;
        }
    } else if (this.totalPages === 0) {
        buttonsCount = 1;
        minButtonNumber = 0;
        maxButtonNumber = 1;
    } else {
        buttonsCount = this.totalPages;
        minButtonNumber = 0;
        maxButtonNumber = this.totalPages;
    }
    let buttonNumber;
    for (let i = 0; i < buttonsCount; i++) {
        buttonNumber = minButtonNumber + i;
        if (buttonNumber <= maxButtonNumber) {
            this.createButton(buttonNumber, pressedPageNumber);
        }
    }

}

HistoryView.prototype.createButton = function (buttonId, activeButtonId) {
    let buttonContainer = document.createElement("li");
    buttonContainer.setAttribute("id", buttonId);
    buttonContainer.className = "page_item";
    if (buttonId === activeButtonId) {
        this.classManager.addClass(buttonContainer, "active");
    }
    buttonContainer.textContent = buttonId;
    this.historyPaggination.appendChild(buttonContainer);
}

HistoryView.prototype.showMessage = function (element, historyBean) {
    let newMessage = element.cloneNode(true);

    let messageContainer = document.createElement("div");
    this.classManager.addClass(messageContainer, "history_message_container")
    messageContainer.appendChild(newMessage);

    let eventTime = messageContainer.getElementsByClassName("history_message_ago")[0];
    this.createTimeDiv(eventTime, historyBean);

    setInterval(() => {
        this.createTimeDiv(eventTime, historyBean);
    }, 60000);

    // this.historyPageContent.insertBefore(messageContainer, this.historyPageContent.firstChild);
    this.historyPageContent.appendChild(messageContainer);
}


HistoryView.prototype.createTimeDiv = function (eventTime, historyBean) {
    let milliseconds = this.calculateTime(historyBean);
    eventTime.textContent = Math.floor(milliseconds / 60000) + " minutes ago";
}


HistoryView.prototype.calculateTime = function (lastEvent) {
    return Date.now() - Date.parse(lastEvent.eventDate);
}