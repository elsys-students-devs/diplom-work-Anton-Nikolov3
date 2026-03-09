import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import CreateOrder from "./CreateOrder";

export default function Cart({ setCartCount }) {
    const [cartItems, setCartItems] = useState([]);
    const token = localStorage.getItem("token");
    const navigate = useNavigate();

    useEffect(() => {
        if (!token) {
            navigate("/login");
        }
    }, [token, navigate]);

    const API_URL = "http://localhost:8080/cart";

    const fetchCart = async () => {
        if (!token) return;
        try {
            const response = await fetch(API_URL, {
                headers: { "Authorization": `Bearer ${token}` },
            });
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            const data = await response.json();
            setCartItems(data);
            const total = data.reduce((sum, order) => sum + order.quantity, 0);
            setCartCount(total); // initial count sync
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchCart();
    }, []);

    const updateCartCount = (items) => {
        const total = items.reduce((sum, order) => sum + order.quantity, 0);
        setCartCount(total);
    };

    // Edit item quantity
    const editItem = async (itemId, quantity) => {
        if (quantity < 1) return; // prevent negative
        try {
            const response = await fetch(`${API_URL}/edit`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                body: JSON.stringify({ itemId, quantity }),
            });
            if (!response.ok) throw new Error("Failed to edit item");

            const data = await response.json();
            setCartItems(data);
            updateCartCount(data); // update App's cart count immediately
        } catch (err) {
            console.error(err);
        }
    };

    // Delete item
    const deleteItem = async (itemId) => {
        try {
            const response = await fetch(`${API_URL}/${itemId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` },
            });
            if (!response.ok) throw new Error("Failed to delete item");

            const data = await response.json();
            setCartItems(data);
            updateCartCount(data); // update App's cart count immediately
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div id="body">
            <h1>My Cart</h1>
            {cartItems.length === 0 && <p>Your cart is empty</p>}

            <table id="item-table">
                <tbody>
                    <tr>
                        <th></th>
                        <th>Име</th>
                        <th>Брой</th>
                        <th>Цена</th>
                    </tr>
                    {cartItems.map((order) => (
                        <tr key={order.id}>
                            <td className="item"><img src={order.item.image_url} alt="" /> </td>
                            <td className="item">{order.item.name}</td>
                            <td className="item">{order.quantity}</td>
                            <td className="item">{order.item.price}€</td>
                            <td>
                                <button onClick={() => editItem(order.item.id, order.quantity + 1)}>+</button>
                                <button
                                    onClick={() => editItem(order.item.id, order.quantity - 1)}
                                    disabled={order.quantity <= 1}
                                >-</button>
                            </td>
                            <td><button onClick={() => deleteItem(order.item.id)}>Remove</button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <CreateOrder/>
        </div>
    );
}