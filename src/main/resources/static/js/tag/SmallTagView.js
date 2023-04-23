function SmallTagView(bookModel, tagModel) {
    this.tagModel = tagModel;
    this.bookModel = bookModel;
    this.tagsContainer = document.getElementsByClassName("main_menu_tags")[0];
}

SmallTagView.prototype.init = function () {
    let that = this;
    this.tagModel.onRead.subscribe((tags) => {
        that.displayTags(tags);
    });
    this.tagModel.onCreate.subscribe((tag) => {
        that.displayTag(tag);
    });
    this.tagsContainer.addEventListener("click", clickedTag => {
        let clickedTagName = clickedTag.target;
        let pressedTagId = +clickedTagName.getAttribute("tag_id");
        this.bookModel.search(pressedTagId)
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
    })
}


SmallTagView.prototype.displayTags = function (tags) {
    this.tagsContainer.innerHTML = "";
    let tagsArray = tags._embedded.tags;
    for (const tag of tagsArray) {
        this.displayTag(tag);
    }
}

SmallTagView.prototype.displayTag = function (tag) {
    let newTag = document.createElement("li");
    newTag.className = "tag";
    newTag.setAttribute("tag_id", tag.id);
    newTag.textContent = tag.name;

    let style = newTag.style;
    style.setProperty('--bullet_color', tag.bulletColor);
    this.tagsContainer.appendChild(newTag);
}

