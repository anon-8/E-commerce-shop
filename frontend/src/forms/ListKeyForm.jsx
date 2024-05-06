import React, { Component } from 'react';
import axios from 'axios';

class ListKeyForm extends Component {
    constructor(props) {
        super(props);
    }

    onChangeHandler = (event) => {
        let name = event.target.name;
        let value = event.target.value;
        this.listCopyKey(value);
    };

    listCopyKey = (price, copyKey) => {
        const itemCopy = {
            price: price,
            copyKey: copyKey
        };

        axios.put('/auth/list-copy-key', itemCopy)
            .then(response => {
                console.log('Item added to cart:', response.data);
            })
            .catch(error => {
                console.error('Error adding item to cart:', error);
            });
    }

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
