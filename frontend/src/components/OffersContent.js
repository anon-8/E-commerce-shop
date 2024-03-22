import React, { Component } from 'react';
import axios from 'axios';

class OffersContent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            sellOffers: [],
            loading: true,
            error: null
        };
    }
    componentDidMount() {
        // Fetch sell offers from the backend
        this.fetchSellOffers();
    }

    fetchSellOffers() {
        axios.get(`/item/${this.props.itemId}/sell-offers`)
            .then(response => {
                this.setState({
                    sellOffers: response.data,
                    loading: false
                });
            })
            .catch(error => {
                this.setState({
                    error: 'Error fetching sell offers',
                    loading: false
                });
            });
    }

    addToCart = (item, seller, price) => {
        const cartItem = {
            item: { id: item.id },
            seller: { id: seller.id },
            price: price,
            quantity: 1
        };

        axios.post('/manipulate-cart', cartItem)
            .then(response => {
                console.log('Item added to cart:', response.data);
            })
            .catch(error => {
                console.error('Error adding item to cart:', error);
            });
    }

    render() {
        const { sellOffers, loading, error } = this.state;

        return (
            <div className="container">
                <h2>Sell Offers</h2>
                {loading ? (
                    <p>Loading...</p>
                ) : error ? (
                    <p>{error}</p>
                ) : (
                    <div>
                        {sellOffers.map(offer => (
                            <div key={offer.id} className="card">
                                <div className="card-body">
                                    <h5 className="card-title">{offer.item.title}</h5>
                                    <p className="card-text">Seller: {offer.seller.username}</p>
                                    <p className="card-text">Price: PLN{offer.price}</p>
                                    <p className="card-text">Quantity: {offer.quantity}</p>
                                    <button className="btn btn-success"
                                            onClick={() => this.addToCart(offer.item, offer.seller, offer.price)}>Add to Cart</button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        );
    }
}

export default OffersContent;
