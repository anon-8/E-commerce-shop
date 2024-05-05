import React, { useState } from 'react';
import axios from 'axios';

const Item = ({ item }) => {
    const [showSellOffers, setShowSellOffers] = useState(false);
    const [quantities, setQuantities] = useState({});
    const [error, setError] = useState(null);

    const fetchSellOffers = (itemId) => {
        axios.get(`/item/${itemId}/sell-offers`)
            .then(response => {
                setShowSellOffers({ ...showSellOffers, [itemId]: response.data });
            })
            .catch(error => {
                console.error('Error fetching sell offers:', error);
                setError('Error fetching sell offers');
            });
    }

    const addToCart = (item, seller, price, quantity) => {
        const cartItem = {
            item: { id: item.id },
            seller: { id: seller.id },
            price: price,
            quantity: quantity
        };

        axios.put('/auth/manipulate-cart', cartItem)
            .then(response => {
                console.log('Item added to cart:', response.data);
            })
            .catch(error => {
                console.error('Error adding item to cart:', error);
                setError('Error adding item to cart');
            });
    }

    const handleQuantityChange = (offerId, quantity) => {
        setQuantities({ ...quantities, [offerId]: quantity });
    }

    const handleItemClick = (item) => {
        this.setState({ selectedItem: item });
    }

    return (

        <div className="bg-gray-100 rounded-md p-5">
            <div className="flex items-center justify-between">
                <div className="flex items-center">
                    <img src={item.imageUrl} alt={item.title} className="w-36 h-50 object-cover rounded-md mr-4"/>
                    <div>
                        <h3 className="text-lg font-semibold mb-2">{item.title}</h3>
                        <p>Developer: {item.developer}</p>
                        <p>Release Year: {item.releaseYear}</p>
                        <p>Platform: {item.platform}</p>
                        <p>Tags: {item.tags.join(', ')}</p>
                    </div>
                </div>
                <button className="btn btn-primary" onClick={() => fetchSellOffers(item.id)}>
                    {showSellOffers[item.id] ? 'Hide Sell Offers' : 'Show Sell Offers'}
                </button>
            </div>
            {showSellOffers[item.id] && (
                <div className="mt-4">
                    <h6>Sell Offers:</h6>
                    <ul>
                        {showSellOffers[item.id].map(offer => {
                            const quantity = quantities[offer.id] || 0;
                            const decrementQuantity = () => handleQuantityChange(offer.id, Math.max(0, quantity - 1));
                            const incrementQuantity = () => handleQuantityChange(offer.id, Math.min(offer.quantity, quantity + 1));

                            return (
                                <li key={offer.id} className="flex items-center">
                                    <div className="mr-2">Seller: {offer.seller.username}, Price: PLN{offer.price}</div>
                                    <button className="btn btn-decrement" onClick={decrementQuantity}>-</button>
                                    <input type="text" value={quantity} readOnly className="mx-2 w-12 text-center"/>
                                    <button className="btn btn-increment" onClick={incrementQuantity}>+</button>
                                    <button className="btn btn-success ml-auto" onClick={() => addToCart(offer.item, offer.seller, offer.price, quantity)}>Add to Cart</button>
                                </li>
                            );
                        })}
                    </ul>
                </div>
            )}
            {error && <p className="text-red-500">{error}</p>}
        </div>
    );
};

export default Item;
