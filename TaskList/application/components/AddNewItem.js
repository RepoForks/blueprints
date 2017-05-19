import React, { Component, PropTypes } from 'react';
import { Button, StyleSheet, Switch, Text, TextInput, View } from 'react-native';
import * as uuid from 'uuid';

const styles = StyleSheet.create({
    container: {
        marginTop: 30,
        flexDirection: 'column',
        justifyContent: 'flex-start'
    },
    textinput: {
        height: 40,
        borderWidth: 1,
        borderColor: '#CCCCCC',
        margin: 2,
        paddingLeft: 4,
        paddingRight: 4,
        paddingTop: 0,
        paddingBottom: 0
    },
    completed: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
    completedtext: {
        fontSize: 16
    },
    buttons: {
        flexDirection: 'row',
        justifyContent: 'space-around'
    },
    title: {
        fontSize: 24
    }
});

class AddNewItem extends Component {
    constructor (props) {
        super(props);

        this.state = {
            text: '',
            completed: false
        };
    }

    onNewItem (e) {
        e.preventDefault();
        if (this.props.onNewItem !== null) {
            this.props.onNewItem({ id: uuid.v4(), text: this.state.text, completed: this.state.completed });
        }
    }

    onCancel (e) {
        e.preventDefault();
        if (this.props.onCancel !== null) {
            this.props.onCancel();
        }
    }

    render () {
        return (
            <View style={styles.container}>
                <Text style={styles.title}>Add New Item</Text>
                <TextInput
                    onChangeText={(text) => this.setState({ text: text })}
                    placeholder="Enter Task"
                    autoCapitalize="sentences"
                    autoCorrect={true}
                    autoFocus={true}
                    keyboardType="default"
                    maxLength={200}
                    value={this.state.text}
                    style={styles.textinput} />
                <View style={styles.completed}>
                    <Text style={styles.completedtext}>Completed?</Text>
                    <Switch
                        onTintColor='green'
                        onValueChange={(value) => this.setState({ completed: value })}
                        value={this.state.completed} />
                </View>
                <View style={styles.buttons}>
                    <Button
                        title="OK"
                        onPress={(e) => this.onNewItem(e)}/>
                    <Button
                        color='#888888'
                        title="Cancel"
                        onPress={(e) => this.onCancel(e)}/>
                </View>
            </View>
        );
    }
}

AddNewItem.propTypes = {
    onNewItem: PropTypes.func,
    onCancel: PropTypes.func
};

AddNewItem.defaultProps = {
    onNewItem: null,
    onCancel: null
};

export default AddNewItem;
