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
            quantities: {},
            currentPage: 0,
            pageSize: 10
        };
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        const { currentPage, pageSize } = this.state;
        axios.get(`/items?page=${currentPage}&size=${pageSize}`)
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
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p>Error: {error}</p>
                    ) : (
                        <ul className="item-list">
                            {data.map((item, index) => (
                                <li key={item.id} className={`item-container ${index % 2 === 0 ? 'even' : 'odd'}`}>
                                    <div className="item-content">
                                        <div className="item-image-container">
                                            <img src={item.imageUrl} alt={item.title} className="item-image"/>
                                        </div>
                                        <div className="item-details">
                                            <div className="description">
                                                <h3>{item.title}</h3>
                                                <p>Developer: {item.developer}</p>
                                                <p>Release Year: {item.releaseYear}</p>
                                                <p>Platform: {item.platform}</p>
                                                <p>Tags: {item.tags.join(', ')}</p>
                                                <button className="btn btn-primary"
                                                        onClick={() => this.fetchSellOffers(item.id)}>Show Sell Offers
                                                </button>
                                            </div>
                                            {showSellOffers[item.id] && (
                                                <div className="sell-offers">
                                                    <h6>Sell Offers:</h6>
                                                    <ul className="sell-offers-list">
                                                        {showSellOffers[item.id].map(offer => {
                                                            const quantity = quantities[offer.id] || 0;
                                                            const decrementQuantity = () => this.handleQuantityChange(offer.id, Math.max(0, quantity - 1));
                                                            const incrementQuantity = () => this.handleQuantityChange(offer.id, Math.min(offer.quantity, quantity + 1));

                                                            return (
                                                                <li key={offer.id} className="sell-offer">
                                                                    <div className="seller-info">
                                                                        Seller: {offer.seller.username}, Price:
                                                                        PLN{offer.price}
                                                                        <button className="btn btn-decrement"
                                                                                onClick={decrementQuantity}>-
                                                                        </button>
                                                                        <input type="text" value={quantity} readOnly
                                                                               style={{
                                                                                   width: '30px',
                                                                                   margin: '0 5px'
                                                                               }}/>
                                                                        <button className="btn btn-increment"
                                                                                onClick={incrementQuantity}>+
                                                                        </button>
                                                                        <button className="btn btn-success"
                                                                                style={{margin: '0 6px'}}
                                                                                onClick={() => this.addToCart(offer.item, offer.seller, offer.price, quantity)}>Add
                                                                            to Cart
                                                                        </button>
                                                                    </div>
                                                                </li>
                                                            );
                                                        })}
                                                    </ul>
                                                </div>
                                            )}

                                        </div>
                                    </div>
                                </li>
                            ))}

                        </ul>

                    )}
                </div>
                <div className="row justify-content-center">
                    <button
                        onClick={() => this.setState(prevState => ({currentPage: Math.max(prevState.currentPage - 1, 0)}))}>Previous
                    </button>
                    <button
                        onClick={() => this.setState(prevState => ({currentPage: prevState.currentPage + 1}))}>Next
                    </button>
                </div>
            </div>
        );
    }
}

export default ItemsContent;
