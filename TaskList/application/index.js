import React, { Component } from 'react';
import { Alert, Modal, StyleSheet, View } from 'react-native';
import ActionButton from 'react-native-action-button';
import ViewItem from './components/ViewItem';
import TaskList from './components/TaskList';

class Application extends Component {
  constructor (props) {
    super(props);

    this.state = {
      items: [
        { id: '1', text: 'First Item', completed: false },
        { id: '2', text: 'Second Item', completed: true }
      ],
      modalVisible: false,
      modalItem: {}
    }
  }

  onSaveItem (item) {
    let items = this.state.items;
    let idx = items.findIndex(t => t.id === item.id);
    if (idx < 0) {
      items.push(item);
    } else {
      items[idx] = item;
    }
    this.setState({
      modalVisible: false,
      items: items
    });
  }

  onDeleteItem (id) {
    this.setState({
      items: this.state.items.filter(item => item.id !== id)
    });
  }

  onCompleteItem (id, flag) {
    this.setState({
      items: this.state.items.filter(item => {
        if (item.id === id) {
          item.completed = flag;
        }
        return item;
      })
    });
  }

  onSelectItem (id) {
    this.setState({
      modalItem: this.state.items.find(item => item.id === id),
      modalVisible: true
    });
  }

  render() {
    return (
      <View style={styles.container}>
        <Modal animationType="slide" transparent={false} visible={this.state.modalVisible} onRequestClose={() => this.setState({ modalVisible: false })}>
          <ViewItem
            item={this.state.modalItem}
            onSaveItem={(item) => this.onSaveItem(item)}
            onCancel={() => this.setState({ modalVisible: false })} />
        </Modal>
        <TaskList
          items={this.state.items}
          onCompleteItem={(id, flag) => this.onCompleteItem(id, flag)}
          onDeleteItem={(id) => this.onDeleteItem(id)}
          onSelectItem={(id) => this.onSelectItem(id)}/>
        <ActionButton
          buttonColor="#9b59b6"
          onPress={() => this.setState({ modalItem: { id: null, text: '', completed: false }, modalVisible: true })} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    backgroundColor: '#F5FCFF',
    marginTop: 22
  }
});

export default Application;
