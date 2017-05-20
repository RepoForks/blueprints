const defaultState = [
    { id: '1', text: 'First Item', completed: false },
    { id: '2', text: 'Second Item', completed: true }
];

export default function tasksReducer (state = defaultState, action) {
    switch (action.type) {
        case 'tasks-save-item':
            let idx = state.findIndex(t => t.id === action.item.id);
            if (idx < 0) {
                return [ ...state, action.item ];
            }
            return state.map((t, i) => i === idx ? action.item : t);
        case 'tasks-delete-item':
            return state.filter(t => t.id !== action.id);
        case 'tasks-complete-item':
            return state.map(item => {
                if (item.id === action.id) {
                    item.completed = action.flag;
                }
                return item;
            });
        default:
            return state;
    }
}
