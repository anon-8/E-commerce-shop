import React, { useState, useEffect } from 'react';
import axios from 'axios';

function CartComponent() {
    const [cartItems, setCartItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchCartItems();
    }, []);

    const fetchCartItems = () => {
        axios.get('/auth/in-cart')
            .then(response => {
                setCartItems(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error('Error fetching cart items:', error);
                setError(error.message);
                setLoading(false);
            });
    }

    const updateCartItem = (itemId, sellerId, price, quantity) => {
        const cartItem = {
            item: { id: itemId },
            seller: { id: sellerId },
            price: price,
            quantity: quantity
        };

        axios.put(`/auth/manipulate-cart`, cartItem)
            .then(response => {
                console.log('Quantity updated:', response.data);
                fetchCartItems();
            })
            .catch(error => {
                console.error('Error updating quantity:', error);
            });
    }

    const placeOrderFromCart = () => {
        axios.post('/auth/place-order-from-cart')
            .then(response => {
                console.log('Response data:', response.data);
                window.open(response.data.paymentUrl, '_blank');
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    const totalOrderValue = () => {
        let totalValue = 0;

        cartItems.forEach(item => {
            totalValue += item.quantity * item.price;
        });

        return totalValue;
    }

    return (
        <div className="container mx-auto">
            <div className="text-center">
                <h1 className="text-3xl font-bold">Cart Items</h1>
            </div>
            {totalOrderValue() > 0 && (
                <div className="text-center">
                    <button className="btn btn-primary" onClick={placeOrderFromCart}>
                        Place Order PLN{totalOrderValue()}
                    </button>
                </div>
            )}
            {loading ? (
                <p className="text-center">Loading...</p>
            ) : error ? (
                <p className="text-center text-red-500">Error: {error}</p>
            ) : (
                <ul className="cart-list">
                    {cartItems.map(item => (
                        <li key={item.id} className="cart-item">
                            <div className="flex items-center">
                                <div className="item-image-container">
                                    <img src={item.item.imageUrl} alt={item.item.title} className="w-34 h-44" />
                                </div>
                                <div className="ml-4">
                                    <h3 className="font-bold">{item.item.title}</h3>
                                    <p>Developer: {item.item.developer}</p>
                                    <p>Platform: {item.item.platform}</p>
                                    <p>Quantity: {item.quantity}</p>
                                    <p>Price: {item.quantity * item.price}</p>
                                </div>
                            </div>
                            <div className="mt-2">
                                <button className="btn btn-danger btn-cart mr-2" onClick={() => updateCartItem(item.item.id, item.seller.id, item.price, -999)}>Remove from Cart</button>
                                <button className="btn btn-secondary btn-cart mr-2" onClick={() => updateCartItem(item.item.id, item.seller.id, item.price, -1)} disabled={item.quantity <= 1}>Decrease Quantity</button>
                                <button className="btn btn-secondary btn-cart" onClick={() => updateCartItem(item.item.id, item.seller.id, item.price, 1)}>Increase Quantity</button>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
}

export default CartComponent;
