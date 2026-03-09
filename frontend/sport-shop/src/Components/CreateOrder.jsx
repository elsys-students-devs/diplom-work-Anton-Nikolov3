import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function CreateOrder() {
  const navigate = useNavigate();

  const [order, setOrder] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    address: "",
  });

  const [error, setError] = useState(null);

  const token = localStorage.getItem("token");

  const handleChange = (e) => {
    setOrder({ ...order, [e.target.name]: e.target.value });
  };

  const makeOrder = async (e) => {
    e.preventDefault(); // 🚨 prevent reload
    try {
      const response = await fetch("http://localhost:8080/order", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(order),
      });

      if (!response.ok) {
        const msg = await response.text();
        throw new Error(msg || "Failed to create order");
      }

      const data = await response.json();
      alert("Order created successfully!");
      navigate("/orders"); // optional redirect
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div >
      <h1>Create Order</h1>

      {error && <p style={{ color: "red" }}>{error}</p>}

      <form id="form" onSubmit={makeOrder}>
        <input
          name="firstName"
          placeholder="First Name"
          value={order.firstName}
          onChange={handleChange}
        />
        <input
          name="lastName"
          placeholder="Last Name"
          value={order.lastName}
          onChange={handleChange}
        />
        <input
          name="phoneNumber"
          placeholder="Phone"
          value={order.phoneNumber}
          onChange={handleChange}
        />
        <input
          name="address"
          placeholder="Address"
          value={order.address}
          onChange={handleChange}
        />

        <button type="submit">Place Order</button>
      </form>
    </div>
  );
}