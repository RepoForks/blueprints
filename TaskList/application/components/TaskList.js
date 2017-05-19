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
        let rows = this.props.items.map(item => { return { id: item.id, text: item.text, completed: item.completed, action: false }; });
        this.state = {
            rows: rows,
            dataSource: this.dataSource.cloneWithRows(rows)
        };
    }

    onCompleteItem (id, flag) {
        Alert.alert(
            'onCompleteItem',
            `id=${id},flag=${flag}`,
            [
                { text: 'OK', onPress: () => console.log('OK Pressed') }
            ],
            { cancelable: false }
        );
    }

    onDeleteItem (item) {
        Alert.alert(
            'onDeleteItem',
            `id=${item.id}`,
            [
                { text: 'OK', onPress: () => console.log('OK Pressed') }
            ],
            { cancelable: false }
        );
        // Close all the swipeouts
        this.onSwipeOut ("-1", "-1");
    }

    onSelectItem (id) {
        Alert.alert(
            'onSelectItem',
            `id=${id}`,
            [
                { text: 'OK', onPress: () => console.log('OK Pressed') }
            ],
            { cancelable: false }
        );
    }

    onSwipeOut (sectionID, rowID) {
        let rows = this.state.rows;
        for (let i = 0 ; i < rows.length ; i++) {
            rows[i].active = (i.toString() === rowID);
        }
        this.setState({
            rows: rows,
            dataSource: this.state.dataSource.cloneWithRows(this.props.items)
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
    })).isRequired
};

export default TaskList;
