import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Shop({ setCartCount, categories }) {
    const [items, setItems] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    useEffect(() => {
        if (!token) {
            navigate("/login");
            return;
        }

        fetch("http://localhost:8080/item", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        })
        .then((response) => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return response.json();
        })
        .then((data) => setItems(data))
        .catch((error) => console.error("Error fetching items:", error));

    }, [navigate, token]);

    const AddToCart = async (itemId) => {
        try {
            const response = await fetch("http://localhost:8080/cart", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify({ itemId, quantity: 1 })
            });

            if (!response.ok) throw new Error("Failed to add item to cart");

            setCartCount(prev => prev + 1);

        } catch (error) {
            console.error("Error adding to cart:", error);
        }
    };

    const handleCategoryChange = (category) => {
        setSelectedCategories(prev =>
            prev.includes(category) ? prev.filter(c => c !== category) : [...prev, category]
        );
    };

    const filteredItems =
        selectedCategories.length === 0
            ? items
            : items.filter(item => selectedCategories.includes(item.category));

    return (
        <div id="shop-body">
            <h1 className="shop-title">Shop</h1>
            <div id="in_row">
            {/* Category Filters */}
            <div id="category-filters">
                {categories.map(cat => (
                    <label key={cat} className="category-label">
                        <input
                            type="checkbox"
                            checked={selectedCategories.includes(cat)}
                            onChange={() => handleCategoryChange(cat)}
                        />
                        {cat}
                    </label>
                ))}
            </div>

            {/* Items Grid */}
            <div id="items-grid">
  {filteredItems.map(item => (
    <div className="item-card" key={item.id}>
      <img
        className="item-image"
        src={item.image_url}
      />
      <div className="item-info">
        <h3>{item.name}</h3>
        <p className="item-price">{item.price} €</p>
        <textarea className="item-description" value={item.description} readOnly />
      </div>
      <div className="item-actions">
        <button onClick={() => AddToCart(item.id)}>Add to cart</button>
      </div>
    </div>
  ))}
</div>
            </div>

        </div>
    );
}