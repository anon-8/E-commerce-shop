import React, { Component } from 'react';
import axios from 'axios';

class CartComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            cartItems: [],
            loading: true,
            error: null
        };
    }

    componentDidMount() {
        this.fetchCartItems();
    }

    fetchCartItems() {
        axios.get('/auth/in-cart')
            .then(response => {
                this.setState({ cartItems: response.data, loading: false });
            })
            .catch(error => {
                console.error('Error fetching cart items:', error);
                this.setState({ error: error.message, loading: false });
            });
    }

    updateCartItem = (itemId, sellerId, price, quantity) => {
        const cartItem = {
            item: { id: itemId },
            seller: { id: sellerId },
            price: price,
            quantity: quantity
        };

        axios.post(`/auth/manipulate-cart`, cartItem )
            .then(response => {
                console.log('Quantity updated:', response.data);
                this.fetchCartItems();
            })
            .catch(error => {
                console.error('Error updating quantity:', error);
            });
    }

    placeOrderFromCart = () => {
        axios.get('/auth/place-order-from-cart')
            .then(response => {
                console.log('Response data:', response.data);
                window.open(response.data.paymentUrl, '_blank');
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    totalOrderValue = () => {
        const { cartItems } = this.state;
        let totalValue = 0;

        cartItems.forEach(item => {
            totalValue += item.quantity * item.price;
        });

        return totalValue;
    }

    render() {
        const { cartItems, loading, error } = this.state;

        return (
            <div className="container">
                <div className="col text-center">
                    <h1>Cart Items</h1>
                </div>
                {this.totalOrderValue() > 0 && (
                    <div className="col-md-12 text-center">
                        <button className="btn btn-primary" onClick={this.placeOrderFromCart}>
                            Place Order PLN{this.totalOrderValue()}
                        </button>
                    </div>
                )}
                {loading ? (
                    <p className="loading-message">Loading...</p>
                ) : error ? (
                    <p className="error-message">Error: {error}</p>
                ): (
                    <ul className="cart-list">
                        {cartItems.map(item => (
                            <li key={item.id} className="cart-item">
                                <div className="item-content">
                                    <div className="item-image-container">
                                        <img src={item.item.imageUrl} alt={item.item.title} className="item-image"/>
                                    </div>
                                    <div className="item-details">
                                        <div className="description">
                                            <h3>{item.item.title}</h3>
                                            <p>Developer: {item.item.developer}</p>
                                            <p>Platform: {item.item.platform}</p>
                                            <p>Quantity: {item.quantity}</p>
                                            <p>Price: {item.quantity * item.price}</p>
                                        </div>
                                        <button className="btn btn-danger btn-cart"
                                                onClick={() => this.updateCartItem(item.item.id, item.seller.id, item.price, -999)}>Remove
                                            from Cart
                                        </button>
                                        <button className="btn btn-secondary btn-cart"
                                                onClick={() => this.updateCartItem(item.item.id, item.seller.id, item.price, -1)}
                                                disabled={item.quantity <= 1}>Decrease Quantity
                                        </button>
                                        <button className="btn btn-secondary btn-cart"
                                                onClick={() => this.updateCartItem(item.item.id, item.seller.id, item.price, 1)}>Increase
                                            Quantity
                                        </button>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        );
    }
}

export default CartComponent;
