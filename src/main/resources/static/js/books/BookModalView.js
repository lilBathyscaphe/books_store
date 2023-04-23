function BookModalView(bookModel, tagModel, historyModel) {
    this.bookModel = bookModel;
    this.tagModel = tagModel;
    this.historyModel = historyModel;

    this.booksPreview = document.getElementsByClassName("book_preview");
    this.popUpBookContainer = document.getElementsByClassName("popup_book")[0];
    this.popUpModal = document.getElementsByClassName("popup_book_modal")[0];
    this.popUpOverlay = document.getElementsByClassName("popup_overlay")[0];

    this.bookTagsContainer = document.getElementsByClassName("book_tags")[0];
    this.tagsLinks = document.getElementsByClassName("tag");

    this.newTagName = document.getElementsByClassName("new_tag_name")[0];
    this.addNewTag = document.getElementsByClassName("add_new_tag")[0];

    this.closeBtn = document.getElementsByClassName("popup_book_close_btn")[0];
    this.modalCanselBtn = document.getElementsByClassName("popup_book_modal_cancel")[0];
    this.modalSaveBtn = document.getElementsByClassName("popup_book_modal_save")[0];

    this.classManager = new ClassManager();

    this.newRating;

    //Used for HistoryModel purpose. Indicates new assigned or unassigned tags.
    this.activeTagsIdBeforeUpdate = [];

    this.clickedBookDiv;

    this.bookEntity = {};

}


BookModalView.prototype.init = function () {

    this.bookModel.onSearch.subscribe(() => {
        for (const preview of this.booksPreview) {
            preview.addEventListener("click", (clickedPreviewEvent) => {
                this.showModalWindow(clickedPreviewEvent.target);
            })
        }
    });

    this.bookModel.onCreate.subscribe((newBook) => {
        let newBookDiv = document.querySelector(`div[book_id="${newBook.id}"]`);
        let bookPreview = newBookDiv.getElementsByClassName("book_preview")[0];
        bookPreview.addEventListener('click', () => {
            this.showModalWindow(bookPreview);
        })
    });

    this.bookTagsContainer.addEventListener("click", clickedTag => {
        let clickedTagName = clickedTag.target;
        let pressedTagId = +clickedTagName.getAttribute("tag_id");
        this.tagModel.find(pressedTagId)
            .then(tagEntity => {
                console.log("Tag founded")
                console.log(tagEntity)
                let bookTag = this.bookEntity.tags.find((tag) => {
                    return tag.id === pressedTagId;
                })
                if (bookTag) {
                    let pressedTagIndex = this.bookEntity.tags.indexOf(bookTag);
                    if (pressedTagIndex > -1) {
                        this.bookEntity.tags.splice(pressedTagIndex, 1);
                    }
                    this.classManager.removeClass(clickedTagName, "active");
                } else {
                    this.classManager.addClass(clickedTagName, "active");
                    this.bookEntity.tags.push(tagEntity);
                }
            });
    });


    this.addNewTag.addEventListener("click", () => {
        let newTagName = document.getElementsByClassName("new_tag_name")[0];
        let tagName = newTagName.value;

        this.tagModel.create(tagName);

        this.newTagName.value = "";
    });


    this.modalSaveBtn.addEventListener("click", () => {
        if (this.clickedBookDiv !== undefined) {
            this.updateBook(this.bookEntity);

            this.addEventToHistory(this.bookEntity);

            this.closeModalWindow();
        }
    });


    this.modalCanselBtn.addEventListener("click", () => {
        // for (const tag of this.createdTags) {
        //     this.tagModel.delete(tag);
        // }
        this.closeModalWindow();
    });

    this.closeBtn.addEventListener("click", () => {
        // for (const tag of this.createdTags) {
        //     this.tagModel.delete(tag);
        // }
        this.closeModalWindow();
    });
}

BookModalView.prototype.showModalWindow = function (bookPreview) {
    this.classManager.removeClass(this.popUpModal, "hidden");
    this.popUpBookContainer.innerHTML = "";

    this.clickedBookDiv = bookPreview.parentElement.parentElement.cloneNode(true);

    let bookRating = this.clickedBookDiv.getElementsByClassName("rating")[0];
    bookRating.addEventListener("click", (clickedRating) => {
        this.newRating = this.handleBookRate(bookRating, clickedRating.target);
    });

    this.popUpBookContainer.appendChild(this.clickedBookDiv);

    this.classManager.addClass(this.popUpOverlay, "active");

    this.tagModel.read();

    let bookId = this.clickedBookDiv.getAttribute("book_id")
    this.bookModel.searchById(+bookId)
        .then(book => {
            console.log("BOOOK IN MODAL VIEW")
            console.log(book)

            this.activeTagsIdBeforeUpdate = [...book.tags];
            this.bookEntity = book;

        })

}

BookModalView.prototype.handleBookRate = function (rateUl, clickTarget) {
    this.classManager.removeClassFromAll(rateUl, "active");
    this.classManager.addClass(clickTarget, "active");
    return +clickTarget.getAttribute("book_rate");
}


BookModalView.prototype.updateBook = function (book) {
    this.assignNewBookRating(book);
    this.bookModel.update(book);
}



BookModalView.prototype.assignNewBookRating = function (book) {
    if (this.newRating !== undefined) {
        book.rate = this.newRating;
    }
}

BookModalView.prototype.addEventToHistory = function (book) {
    // if (this.createdTags.length !== 0) {
    //     this.historyModel.createNewTagsEvent(this.createdTags);
    // }

    if (this.activeTagsIdBeforeUpdate.length !== this.bookEntity.tags.length) {
        this.sendAssignNewTagsMessage(book, this.activeTagsIdBeforeUpdate);
        this.sendRemoveTagsMessage(book, this.activeTagsIdBeforeUpdate);
    }

    if (this.newRating !== undefined) {
        this.historyModel.createUpdateRatingBookEvent(book);
    }
}

BookModalView.prototype.sendAssignNewTagsMessage = function (book, activeTags) {
    let newTags = findNewTags(book, activeTags);
    if (newTags.length !== 0) {
        this.historyModel.createAddTagsToBookEvent(book, newTags);
    }
}

function findNewTags(book, tags) {
    let resultTags = [];

    for (const tag of book.tags) {
        if (!tags.includes(tag)) {
            resultTags.push(tag);
        }
    }
    return resultTags;
}

BookModalView.prototype.sendRemoveTagsMessage = function (book, activeTags) {
    let removedTags = findRemovedTags(book, this.activeTagsIdBeforeUpdate);
    if (removedTags.length !== 0) {
        this.historyModel.createRemoveTagsFromBookEvent(book, removedTags);
    }
}

function findRemovedTags(book, tags) {
    let resultTags = [];

    for (const tag of tags) {
        if (!book.tags.includes(tag)) {
            resultTags.push(tag);
        }
    }
    return resultTags;
}





BookModalView.prototype.closeModalWindow = function () {
    this.classManager.addClass(this.popUpModal, "hidden");
    this.classManager.removeClass(this.popUpOverlay, "active");
    this.clearUserParameters();
}

BookModalView.prototype.clearUserParameters = function () {
    this.newRating = undefined;
    this.activeTagsIdBeforeUpdate = [];
    // this.createdTags = [];
}