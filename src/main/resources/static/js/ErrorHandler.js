function ErrorHandler(bookModel, historyModel, tagModel) {
    this.bookModel = bookModel;
    this.historyModel = historyModel;
    this.tagModel = tagModel;
}



ErrorHandler.prototype.init = function () {

    this.bookModel.onError.subscribe((exceptionMessage)=>{
        // let message = this.errorMessageContainer.getElementsByClassName("message")[0];
        // console.log(exceptionMessage.message);
        // message.textContent = exceptionMessage.message;
        console.log(exceptionMessage)
        console.log(exceptionMessage.status);
        if(exceptionMessage.status === 403){
            alert("You do not have permission");
        } else {
            alert(exceptionMessage.message);
        }
    })

    this.historyModel.onError.subscribe((exceptionMessage)=>{
        console.log(exceptionMessage)

        alert(exceptionMessage.message);

    })

    this.tagModel.onError.subscribe((exceptionMessage)=>{
        console.log(exceptionMessage)
        alert(exceptionMessage.message);
    })

}