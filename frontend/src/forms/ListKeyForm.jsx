import React, { Component } from 'react';
import classNames from 'classnames';
import axios from 'axios';

class ListKeyForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            copy_key: "",
            price: ""
        };
    }

    onChangeHandler = (event) => {
        let name = event.target.name;
        let value = event.target.value;
        this.setState({[name]: value});
    };

    onSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/list-copy-key', this.state);
            console.log("Response:", response.data);
        } catch (error) {
            console.error("Error:", error);
        }
    };

    render() {
        return (
            <div className="col-4">
                <form onSubmit={this.onSubmit}>
                    <div className="form-outline mb-4">
                        <input type="text" id="copy_key" name="copy_key" className="form-control" onChange={this.onChangeHandler} />
                        <label className="form-label" htmlFor="copy_key">Copy Key</label>
                    </div>
                    <div className="form-outline mb-4">
                        <input type="number" id="price" name="price" className="form-control" onChange={this.onChangeHandler} />
                        <label className="form-label" htmlFor="price">Price</label>
                    </div>
                    <button type="submit" className="btn btn-primary btn-block mb-4">Submit</button>
                </form>
            </div>
        );
    }
}

export default ListKeyForm;
