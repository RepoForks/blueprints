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

class ViewItem extends Component {
    constructor (props) {
        super(props);

        this.state = {
            id: this.props.item.id,
            text: this.props.item.text || '',
            completed: this.props.item.completed || false
        };
    }

    onSaveItem (e) {
        e.preventDefault();
        if (this.props.onSaveItem !== null) {
            this.props.onSaveItem({ id: this.state.id || uuid.v4(), text: this.state.text, completed: this.state.completed });
        }
    }

    onCancel (e) {
        e.preventDefault();
        if (this.props.onCancel !== null) {
            this.props.onCancel();
        }
    }

    render () {
        const title = !this.state.id ? 'Add New Item' : 'Show Item';
        return (
            <View style={styles.container}>
                <Text style={styles.title}>{title}</Text>
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
                        onPress={(e) => this.onSaveItem(e)}/>
                    <Button
                        color='#888888'
                        title="Cancel"
                        onPress={(e) => this.onCancel(e)}/>
                </View>
            </View>
        );
    }
}

ViewItem.propTypes = {
    item: PropTypes.shape({
        id: PropTypes.string,
        text: PropTypes.string,
        completed: PropTypes.bool
    }),
    onSaveItem: PropTypes.func,
    onCancel: PropTypes.func
};

ViewItem.defaultProps = {
    item: {},
    onSaveItem: null,
    onCancel: null
};

export default ViewItem;
