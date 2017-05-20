export function saveItem(item) {
    return {
        type: 'tasks-save-item',
        item: item
    };
}

export function deleteItem(id) {
    return {
        type: 'tasks-delete-item',
        id: id
    };
}

export function completeItem(id, flag) {
    return {
        type: 'tasks-complete-item',
        id: id,
        flag: flag
    };
}
