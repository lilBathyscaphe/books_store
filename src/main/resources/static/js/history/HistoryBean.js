function HistoryBean(eventType, book, searchQuery, tag, eventDate) {
    this.eventType = eventType;
    this.searchQuery = searchQuery;
    this.tag = tag;
    this.book = book;

    let rawEventDate = new Date();

    //https://stackoverflow.com/questions/39163462/json-stringify-date-produces-a-wrong-date-compared-to-javascript-date
    this.eventDate = new Date(Date.UTC(
        rawEventDate.getFullYear(),
        rawEventDate.getMonth(),
        rawEventDate.getDate(),
        rawEventDate.getHours(),
        rawEventDate.getMinutes()));
}
