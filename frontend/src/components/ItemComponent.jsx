import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ListKeyForm from '../forms/ListKeyForm'; // Import the ListKeyForm component

const Item = ({ item }) => {
    const [sellOffers, setSellOffers] = useState([]);
    const [quantities, setQuantities] = useState({});
    const [error, setError] = useState(null);
    const [showListKeyForm, setShowListKeyForm] = useState(false); // Add state for toggling the form visibility

    useEffect(() => {
        fetchSellOffers(item.id);
    }, [item.id]);

    const fetchSellOffers = (itemId) => {
        axios.get(`/item/${itemId}/sell-offers`)
            .then(response => {
                setSellOffers(response.data);
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

    const toggleListKeyForm = () => {
        setShowListKeyForm(prevState => !prevState);
    };

    return (
        <div className="bg-gray-100 rounded-md p-5">
            {showListKeyForm && <ListKeyForm />} {/* Render ListKeyForm if showListKeyForm is true */}
            <div className="flex items-center justify-between">
                <div className="flex items-center">
                    <img src={item.imageUrl} alt={item.title} className="w-57 h-80 object-cover rounded-md mr-4"/>
                    <div>
                        <h3 className="text-lg font-semibold mb-2">{item.title}</h3>
                        <p>Developer: {item.developer}</p>
                        <p>Release Year: {item.releaseYear}</p>
                        <p>Platform: {item.platform}</p>
                        <p>Tags: {item.tags.join(', ')}</p>
                    </div>
                </div>
            </div>
            <div className="mt-4">
                <h6 className="mb-2">Sell Offers:</h6>
                {sellOffers.length > 0 ? (
                    sellOffers.map(offer => {
                        const quantity = quantities[offer.id] || 1;
                        const decrementQuantity = () => handleQuantityChange(offer.id, Math.max(0, quantity - 1));
                        const incrementQuantity = () => handleQuantityChange(offer.id, Math.min(offer.quantity, quantity + 1));

                        return (
                            <div key={offer.id} className="bg-white rounded-md p-3 mb-2">
                                <div>Seller: {offer.seller.username}</div>
                                <div>Price: PLN{offer.price}</div>
                                <div className="flex items-center">
                                    <button className="btn btn-decrement" onClick={decrementQuantity}>-</button>
                                    <input type="text" value={quantity} readOnly className="mx-2 w-12 text-center"/>
                                    <button className="btn btn-increment" onClick={incrementQuantity}>+</button>
                                    <button className="btn btn-success ml-auto" onClick={() => addToCart(offer.item, offer.seller, offer.price, quantity)}>Add to Cart</button>
                                </div>
                            </div>
                        );
                    })
                ) : (
                    <p>No sell offers available.</p>
                )}
            </div>
            <div className="flex justify-center mt-4">
                <button className="btn btn-primary" onClick={toggleListKeyForm}>
                    {showListKeyForm ? 'Hide Form' : 'Show Form'}
                </button>
            </div>
            {error && <p className="text-red-500">{error}</p>}
        </div>
    );
};

export default Item;
