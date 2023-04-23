function ClassManager() {
}


ClassManager.prototype.addClass = function (element, className) {
    if (element.className !== undefined && !element.className.includes(className)) {
        let addClassName = " " + className;
        element.className += addClassName;
    }
}

ClassManager.prototype.removeClass = function (element, className) {
    if (element.className !== undefined) {
        let updatedClassName = element.className.replace(className, '');
        element.className = updatedClassName.trim();
    }
}

ClassManager.prototype.addClassToAll = function (elements, newClassName) {
    let childNodes = elements.childNodes;
    let addClassName = " " + newClassName
    for (const node of childNodes) {
        if (node.className !== undefined) {
            if (!node.className.includes(newClassName)) {
                let classList = node.className.trim() + addClassName;
                node.className = classList;
            }
        }
    }

}

ClassManager.prototype.removeClassFromAll = function (elements, className) {
    let childNodes = elements.childNodes;

    for (const node of childNodes) {
        if (node.className !== undefined) {
            let disabledClassName = node.className.replace(className, '');
            node.className = disabledClassName.trim();
        }
    }
}

