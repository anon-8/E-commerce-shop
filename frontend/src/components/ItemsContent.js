import React, { Component } from 'react';
import axios from 'axios';
import { setAuthHeader } from '../axios_helper';

class ItemsContent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            error: null,
            loading: true,
            showSellOffers: {},
            quantities: {}
        };
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        axios.get('/items')
            .then(response => {
                this.setState({ data: response.data, loading: false });
            })
            .catch(error => {
                if (error.response && error.response.status === 401) {
                    setAuthHeader(null);
                }
                this.setState({ error: error.message, loading: false });
            });
    }

    fetchSellOffers = (itemId) => {
        axios.get(`/item/${itemId}/sell-offers`)
            .then(response => {
                this.setState(prevState => ({
                    showSellOffers: {
                        ...prevState.showSellOffers,
                        [itemId]: response.data
                    }
                }));
            })
            .catch(error => {
                console.error('Error fetching sell offers:', error);
            });
    }

    addToCart = (item, seller, price, quantity) => {
        const cartItem = {
            item: { id: item.id },
            seller: { id: seller.id },
            price: price,
            quantity: quantity
        };

        axios.post('/auth/manipulate-cart', cartItem)
            .then(response => {
                console.log('Item added to cart:', response.data);
            })
            .catch(error => {
                console.error('Error adding item to cart:', error);
            });
    }

    handleQuantityChange = (offerId, quantity) => {
        this.setState(prevState => ({
            quantities: {
                ...prevState.quantities,
                [offerId]: quantity
            }
        }));
    }

    placeOrderFromCart = () => {
        axios.get('/auth/place-order-from-cart')
            .then(response => {
                console.log('Response data:', response.data);
                window.location.href = response.data.paymentUrl;
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    render() {
        const { data, loading, error, showSellOffers, quantities } = this.state;

        return (
            <div className="container">
                <div className="row">
                    <div className="col text-center">
                        <h1>Items</h1>
                    </div>
                </div>
                <div className="row justify-content-center">
                    <div className="col-md-12 text-center">
                        <button className="btn btn-primary" onClick={this.placeOrderFromCart}>Place Order From Cart
                        </button>
                    </div>
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p>Error: {error}</p>
                    ) : (

                        <ul className="item-list">
                            {data.map(item => (
                                <li key={item.id}>
                                    <img src={item.imageUrl} alt={item.title} className="item-image"/>
                                    <div className="item-details">
                                        <h3>{item.title}</h3>
                                        <p>Developer: {item.developer}</p>
                                        <p>Release Year: {item.releaseYear}</p>
                                        <p>Platform: {item.platform}</p>
                                        <p>Tags: {item.tags.join(', ')}</p>
                                        <button className="btn btn-primary"
                                                onClick={() => this.fetchSellOffers(item.id)}>
                                            Show Sell Offers
                                        </button>
                                        {showSellOffers[item.id] && (
                                            <div>
                                                <h6>Sell Offers</h6>
                                                <ul className="sell-offers-list">
                                                    {showSellOffers[item.id].map(offer => (
                                                        <li key={offer.id} className="sell-offer">
                                                            <div className="seller-info">
                                                                Seller: {offer.seller.username}, Price: PLN{offer.price}
                                                            </div>
                                                            <div className="quantity-control">
                                                                <button className="btn btn-decrement"
                                                                        onClick={() => this.handleQuantityChange(offer.id, Math.max(1, quantities[offer.id] - 1))}>
                                                                    -
                                                                </button>
                                                                <input type="text" value={quantities[offer.id] || ''}
                                                                       readOnly/>
                                                                <button className="btn btn-increment"
                                                                        onClick={() => this.handleQuantityChange(offer.id, Math.min(offer.quantity, (quantities[offer.id] || 0) + 1))}>
                                                                    +
                                                                </button>
                                                            </div>
                                                            <button className="btn btn-success"
                                                                    onClick={() => this.addToCart(offer.item, offer.seller, offer.price, quantities[offer.id])}>Add
                                                                to Cart
                                                            </button>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        )}
                                    </div>
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>
        );
    }
}

export default ItemsContent;
