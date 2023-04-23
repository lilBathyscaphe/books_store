function TagView(bookModel, tagModel, historyModel) {
    this.tagModel = tagModel;
    this.bookModel = bookModel;
    this.historyModel = historyModel;

    this.classManager = new ClassManager();

    this.tagsContainer = document.getElementsByClassName("book_tags")[0];
    this.tagsErrorMessageCotainer = document.getElementsByClassName("tag_error_message_container")[0];

    this.modalBook = document.getElementsByClassName("popup_book")[0];
}

TagView.prototype.init = function () {
    let that = this;

    this.tagModel.onRead.subscribe((tags) => {
        this.clearErrorMessageContainer();
        let bookDiv = this.modalBook.getElementsByClassName("book")[0];
        if(bookDiv !== undefined) {
            let bookId = +bookDiv.getAttribute("book_id");
            this.bookModel.searchById(bookId)
                .then((bookBean) => {
                    console.log(bookBean)
                    this.tagsContainer.innerHTML = ""; //make method not clear tag container.
                    for (const tag of tags._embedded.tags) {           //If method clear container,
                        // then pressed active tag will be removed until save is pressed
                        let newTag = that.displayTag(tag);
                        if (that.isTagActive(bookBean, tag)) {
                            that.highlightActiveTag(newTag);
                        }
                    }
                })
        }
    });
    this.tagModel.onCreate.subscribe((tag) => {
        this.clearErrorMessageContainer();
        that.displayTag(tag);
        this.historyModel.createNewTagsEvent(tag);
    });

    this.tagModel.onError.subscribe((errorMessage) => {
        let errorMessageField = this.tagsErrorMessageCotainer.getElementsByClassName("tag_message")[0];
        errorMessageField.textContent = errorMessage.errorMessage;
    })


}

TagView.prototype.clearErrorMessageContainer = function () {
    let errorMessageField = this.tagsErrorMessageCotainer.getElementsByClassName("tag_message")[0];
    errorMessageField.textContent = "";
}

//make method to look for book TAG and render border to assignedTags
//todo make composite VIEW for tags
TagView.prototype.displayTag = function (tagBean) {
    // let newTag = this.tagPattern.cloneNode(true);
    let newTag = document.createElement("li");
    newTag.className = "tag";
    newTag.setAttribute("tag_id", tagBean.id);
    newTag.textContent = tagBean.name;

    let style = newTag.style;
    style.setProperty('--bullet_color', tagBean.bulletColor);

    this.tagsContainer.appendChild(newTag);
    return newTag;

    // newTag.addEventListener("click", clickedTag => {
    //     console.log(tag.tagName)
    //     let clickedTagName = clickedTag.target;
    //     let tag = this.tagModel.find(+clickedTagName.getAttribute("tag_id"));
    //     console.log(tag)

    //     if (!this.activeTags.includes(tag)) {
    //         this.classManager.addClass(clickedTagName, "active");
    //         this.activeTags.push(tag);
    //     } else {
    //         let index = this.activeTags.indexOf(clickedTagName);
    //         this.classManager.removeClass(clickedTagName, "active")
    //         if (index > -1) {
    //             this.activeTags.splice(index, 1);
    //         }
    //     }
    //     console.log(this.activeTags)
    //     console.log(this.createdTags)
    // });


    // let li = document.createElement("li");
    // // let tagLink = document.createElement("a");

    // li.textContent = tag[1].tagName;
    // // tagLink.href = "#"
    // li.setAttribute("tag_id", tag[0]);
    // li.className = "tag";

    // // li.appendChild(tagLink);
    // this.bookTagsUl.appendChild(li);
}

TagView.prototype.isTagActive = function (book, tag) {
    let bookTags = book.tags;
    let active;
    for (const bookTag of bookTags) {
        if (bookTag.id === tag.id) {
            active = true;
            break;
        }
    }
    return active;
}

TagView.prototype.highlightActiveTag = function (tag) {
    this.classManager.addClass(tag, "active");
}



