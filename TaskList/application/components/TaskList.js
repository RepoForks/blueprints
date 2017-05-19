import React, { Component, PropTypes } from 'react';
import { Alert, ListView, StyleSheet, View } from 'react-native';
import Swipeout from 'react-native-swipeout';
import TaskListItem from './TaskListItem';

const styles = StyleSheet.create({
    container: {
        alignSelf: 'stretch'
    }
});

class TaskList extends Component {
    constructor (props) {
        super(props);

        this.dataSource = new ListView.DataSource({
            rowHasChanged: (r1, r2) => r1 !== r2
        });
        let rows = this.propsToRows(this.props);
        this.state = {
            rows: rows,
            dataSource: this.dataSource.cloneWithRows(rows)
        };
    }

    propsToRows (props) {
        return props.items.map(item => {
            return {
                id: item.id,
                text: item.text,
                completed: item.completed,
                active: false
            };
        });
    }

    componentWillReceiveProps (props) {
        let rows = this.propsToRows(props);
        this.setState({
            rows: rows,
            dataSource: this.dataSource.cloneWithRows(rows)
        });
    }

    onCompleteItem (id, flag) {
        if (this.props.onCompleteItem !== null) {
            this.props.onCompleteItem(id, flag);
        }
    }

    onDeleteItem (item) {
        if (this.props.onDeleteItem !== null) {
            this.props.onDeleteItem(item.id);
        }
        this.onSwipeOut ("-1", "-1");
    }

    onSelectItem (id) {
        if (this.props.onSelectItem !== null) {
            this.props.onSelectItem(id);
        }
    }

    onSwipeOut (sectionID, rowID) {
        let rows = this.propsToRows(this.props);
        let i = parseInt(rowID);
        if (i >= 0) {
            rows[i].active = true;
        }
        this.setState({
            rows: rows,
            dataSource: this.state.dataSource.cloneWithRows(rows)
        });
    }

    renderRow (item, sectionID, rowID) {
        const buttons = [
            {
                text: 'Delete',
                backgroundColor: '#FF0000',
                onPress: () => this.onDeleteItem(item)
            }
        ];
        return (
            <Swipeout
                right={buttons}
                rowID={rowID}
                close={!item.active}
                onOpen={(sid, rid) => this.onSwipeOut(sid, rid)}>
                <TaskListItem
                    item={item}
                    onCompleteItem={(id,flag) => this.onCompleteItem(id, flag)}
                    onSelectItem={(id) => this.onSelectItem(id)} />
            </Swipeout>
        );
    }

    render () {
        return (
            <View style={styles.container}>
                <ListView dataSource={this.state.dataSource}
                    renderRow={(item, sid, rid) => this.renderRow(item, sid, rid)} />
            </View>
        );
    }
}

TaskList.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        text: PropTypes.string.isRequired,
        completed: PropTypes.bool.isRequired
    })).isRequired,
    onDeleteItem: PropTypes.func,
    onCompleteItem: PropTypes.func,
    onSelectItem: PropTypes.func
};

TaskList.defaultProps = {
    onDeleteItem: null,
    onCompleteItem: null,
    onSelectItem: null
};

export default TaskList;
