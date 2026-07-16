import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Rating from "./Rating.jsx";

export default function Shop({ setCartCount, categories }) {
    const [items, setItems] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);
    const [favorites, setFavorites] = useState([]);

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
        .then((data) => setItems(data.sort((a, b) => a.name.localeCompare(b.name))))
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

    const fetchFavorites = async () => {
        try {
            const response = await fetch("http://localhost:8080/item/favorites", {
                headers: {Authorization: `Bearer ${token}`}
            });
            if (!response.ok) throw new Error("Failed to fetch favorites");
            const data = await response.json();
            setFavorites(data);

        } catch {
            console.error("Cannot fetch favorites")
        }
    };


    const AddToFavorites = async (itemId) => {
        try{
            const response = await fetch(`http://localhost:8080/item/favorites/${itemId}`, {
                headers: { Authorization: `Bearer ${token}` },
                method: "POST"
            });
            if (!response.ok) throw new Error("Failed to delete favorite");
            fetchFavorites()
        }
        catch {
            console.error("Cannot add favorite")
        }


    }

    const RemoveFromFavorites = async (itemId) => {
        try{
            const response = await fetch(`http://localhost:8080/item/favorites/${itemId}`, {
                headers: { Authorization: `Bearer ${token}` },
                method: "DELETE"
            });
            if (!response.ok) throw new Error("Failed to delete favorite");
            fetchFavorites()
        }
        catch {
            console.error("Cannot add favorite")
        }


    }

    useEffect(()=>{
        fetchFavorites();
    },[]);


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
        <textarea className="item-description" value={item.description} readOnly disabled />
        <Rating itemId={item.id}/>
        <button className="favorite-btn" style={{
            color: favorites.some(fav => fav.id === item.id) ? "red" : "white"
        }} onClick={()=>{
            if(favorites.map(fav => fav.id).includes(item.id)) {
                RemoveFromFavorites(item.id);
            }
            else {
                AddToFavorites(item.id);
            }
        }}>❤</button>
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