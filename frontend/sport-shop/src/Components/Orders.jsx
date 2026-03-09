import { useState, useEffect } from "react";

export default function Orders() {
    const [orders, setOrders] = useState([]);
    const [selectedOrder, setSelectedOrder] = useState(null);
    const [error, setError] = useState(null);

    const token = localStorage.getItem("token");

    // 1️⃣ Взимаме всички поръчки на потребителя
    const fetchOrders = async () => {
        try {
            const response = await fetch("http://localhost:8080/order", {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Failed to fetch orders");
            const data = await response.json();
            setOrders(data);
        } catch (err) {
            setError(err.message);
        }
    };

    // 2️⃣ Взимане на конкретна поръчка
    const fetchOrderById = async (orderId) => {
        try {
            const response = await fetch(`http://localhost:8080/order/${orderId}`, {
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Order not found");
            const data = await response.json();
            setSelectedOrder(data);
        } catch (err) {
            setError(err.message);
        }
    };

    // 3️⃣ Създаване на поръчка (взимаме кошницата на backend)
    const makeOrder = async () => {
        try {
            const response = await fetch("http://localhost:8080/order", {
                method: "POST",
                headers: { 
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({}) // backend използва текущата кошница
            });
            if (!response.ok) {
                const msg = await response.text();     
                throw new Error(msg || "Failed to create order");
            }
            const data = await response.json();
            setOrders(prev => [...prev, data]);
            alert("Order created successfully!");
        } catch (err) {
            setError(err.message);
        }
    };

    // 4️⃣ Изтриване на поръчка
    const deleteOrder = async (orderId) => {
        try {
            const response = await fetch(`http://localhost:8080/order/${orderId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Failed to delete order");
            setOrders(prev => prev.filter(o => o.id !== orderId));
        } catch (err) {
            setError(err.message);
        }
    };

    // 5️⃣ Актуализиране на поръчка (примерно само адрес/име)
    const updateOrder = async (orderId, updatedOrder) => {
        try {
            const response = await fetch(`http://localhost:8080/order/${orderId}`, {
                method: "PATCH",
                headers: { 
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(updatedOrder)
            });
            if (!response.ok) {
                const msg = await response.text();
                throw new Error(msg || "Failed to update order");
            }
            alert("Order updated successfully!");
            fetchOrders(); // обновяваме списъка
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

    return (
        <div>
            <h1>My Orders</h1>

            {error && <div style={{color:"red", marginBottom:"10px"}}>{error}</div>}

            <button onClick={makeOrder}>Create Order from Cart</button>

            <ul>
                {orders.map(order => (
                    <li key={order.id}>
                        Order #{order.id} - Total: ${order.price.toFixed(2)}
                        <button onClick={() => fetchOrderById(order.id)}>View</button>
                        <button onClick={() => deleteOrder(order.id)}>Delete</button>
                    </li>
                ))}
            </ul>

            {selectedOrder && (
                <div style={{border:"1px solid gray", padding:"10px", marginTop:"20px"}}>
                    <h2>Order #{selectedOrder.id}</h2>
                    <p>Total: ${selectedOrder.price.toFixed(2)}</p>
                    <ul>
                        {selectedOrder.items.map((item, index) => (
                            <li key={index}>
                                {item.item.name} - {item.quantity} pcs - ${item.price.toFixed(2)}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}