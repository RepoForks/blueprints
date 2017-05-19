import React, { Component } from 'react';
import { Alert, Modal, StyleSheet, View } from 'react-native';
import ActionButton from 'react-native-action-button';
import AddNewItem from './components/AddNewItem';
import TaskList from './components/TaskList';

class Application extends Component {
  constructor (props) {
    super(props);

    this.state = {
      items: [
        { id: '1', text: 'First Item', completed: false },
        { id: '2', text: 'Second Item', completed: true }
      ],
      modalVisible: false
    }
  }

  onNewItem (item) {
    this.setState({
      modalVisible: false,
      items: [ ...this.state.items, item ]
    });
    console.log(`Added item ${JSON.stringify(item)}`);
  }

  render() {
    return (
      <View style={styles.container}>
        <Modal animationType="slide" transparent={false} visible={this.state.modalVisible} onRequestClose={() => this.setState({ modalVisible: false })}>
          <AddNewItem onNewItem={(item) => this.onNewItem(item)} onCancel={() => this.setState({ modalVisible: false })} />
        </Modal>
        <TaskList items={this.state.items} />
        <ActionButton
          buttonColor="#9b59b6"
          onPress={() => this.setState({ modalVisible: true })}/>
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
