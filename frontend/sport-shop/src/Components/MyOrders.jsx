import { useState, useEffect } from "react";

export default function MyOrders() {
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [error, setError] = useState(null);

    const token = localStorage.getItem("token");

    const fetchOrders = async () => {
        try {
            const response = await fetch("http://localhost:8080/order", {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Failed to fetch orders");
            const data = await response.json();
            setOrders(data);
        } catch (err) {
            setError(err.message);
        }
    };

    const fetchOrderById = async (orderId) => {
        try {
            const response = await fetch(`http://localhost:8080/order/${orderId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Order not found");
            const data = await response.json();
            setSelectedOrder(data);
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    return (
        <div id="body">
            <h1>My Orders</h1>
            {error && <div style={{ color: "red" }}>{error}</div>}

    <table 
    id="item-table" 
    style={{
        width: "70%",
        borderCollapse: "collapse",
        marginTop: "20px"
    }}
>
    <thead>
        <tr style={{ backgroundColor: "#f0f0f0", textAlign: "left" }}>
            <th colSpan={"2"} style={{ padding: "10px", border: "1px solid #ccc",textAlign:"center" } }>Orders</th>
        </tr>
    </thead>
    <tbody>
        {orders.map(order => (
            <tr key={order.id} style={{ borderBottom: "1px solid #ddd" }}>
                <td>
                    Total: ${order.price.toFixed(2)}
                </td>
                <td>
                    <button 
                        onClick={() => fetchOrderById(order.id)}
                        style={{
                            padding: "5px 10px",
                            backgroundColor: "#4CAF50",
                            color: "white",
                            border: "none",
                            borderRadius: "4px",
                            cursor: "pointer"
                        }}
                    >
                        View
                    </button>
                </td>
            </tr>
        ))}
    </tbody>
    </table>

            {selectedOrder && (
                <div style={{ border: "1px solid gray", padding: "10px", marginTop: "20px" }}>
                    <p>Total: ${selectedOrder.price.toFixed(2)}</p>
                    <ul>
                        {selectedOrder.items.map((item, index) => (
                            <li key={index}>
                                {item.item.name} - {item.quantity} - ${item.price.toFixed(2)}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}