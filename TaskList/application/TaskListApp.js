import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { Alert, Modal, StyleSheet, View } from 'react-native';
import ActionButton from 'react-native-action-button';
import * as taskActions from './redux/actions/tasks';
import ViewItem from './components/ViewItem';
import TaskList from './components/TaskList';

class Application extends Component {
  constructor (props) {
    super(props);

    this.state = {
      modalVisible: false,
      modalItem: {}
    }
  }

  onSaveItem (item) {
      this.props.saveItem(item);
      this.setState({ modalVisible: false });
  }

  onDeleteItem (id) {
      this.props.deleteItem(id);
  }

  onCompleteItem (id, flag) {
      this.props.completeItem(id, flag);
  }

  onSelectItem (id) {
    this.setState({
      modalItem: this.props.items.find(item => item.id === id),
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
          items={this.props.items}
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

Application.propTypes = {
    items: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.string.isRequired,
        text: PropTypes.string.isRequired,
        completed: PropTypes.bool.isRequired
    })).isRequired,
    saveItem: PropTypes.func.isRequired,
    deleteItem: PropTypes.func.isRequired,
    completeItem: PropTypes.func.isRequired
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    alignItems: 'flex-start',
    backgroundColor: '#F5FCFF',
    marginTop: 22
  }
});

const mapStateToProps = (state, ownProps) => {
    return {
        items: state.tasks
    };
}

const mapDispatchToProps = (dispatch) => {
    return {
        saveItem: (item) => { dispatch(taskActions.saveItem(item)); },
        deleteItem: (id) => { dispatch(taskActions.deleteItem(id)); },
        completeItem: (id, flag) => { dispatch(taskActions.completeItem(id, flag)); }
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Application);
