import React, { Component } from 'react';
import { Alert, StyleSheet, View } from 'react-native';
import TaskListItem from './components/TaskListItem';

class Application extends Component {
  onCompleteItem (id, flag) {
    Alert.alert(
      'onCompleteItem',
      `id=${id} flag=${flag}`,
      [
        { text: 'OK', onPress: () => console.log('OK')}
      ],
      { cancelable: false }
    );
  }

  onSelectItem (id) {
    Alert.alert(
      'onSelectItem',
      `id=${id}`,
      [
        { text: 'OK', onPress: () => console.log('OK')}
      ],
      { cancelable: false }
    );
  }

  onDeleteItem (d) {
    Alert.alert(
      'onDeleteItem',
      `id=${id}`,
      [
        { text: 'OK', onPress: () => console.log('OK')}
      ],
      { cancelable: false }
    );
  }

  render() {
    const item = { id: 'newitem', text: 'First Item', completed: false };

    return (
      <View style={styles.container}>
        <TaskListItem
          item={item}
          onCompleteItem={(id, flag) => this.onCompleteItem(id, flag)}
          onSelectItem={(id) => this.onSelectItem(id)}
          onDeleteItem={(id) => this.onDeleteItem(id)}/>
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
    marginTop: 16
  }
});

export default Application;
