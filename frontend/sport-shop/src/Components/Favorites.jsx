import { useState, useEffect } from "react";
import Rating from "./Rating.jsx";

export default function MyOrders() {
    const [favorites, setFavorites] = useState([]);
    const [error, setError] = useState(null);

    const token = localStorage.getItem("token");

    const removeFavorite = async (itemId) => {
        try{
            const response = await fetch(`http://localhost:8080/item/favorites/${itemId}`, {
                headers: { Authorization: `Bearer ${token}` },
                method: "DELETE"
            });
            if (!response.ok) throw new Error("Failed to delete favorite");
            fetchFavorites()
        }
        catch (err) {
            setError(err.message);
        }


    }

    const fetchFavorites = async () => {
        try {
            const response = await fetch("http://localhost:8080/item/favorites", {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (!response.ok) throw new Error("Failed to fetch favorites");
            const data = await response.json();
            setFavorites(data);
        } catch (err) {
            setError(err.message);
        }
    };

    useEffect(() => {
        fetchFavorites();
    }, []);

    return (
        <div id="body">
            <h1>My Favorites</h1>
            {error && <div style={{ color: "red" }}>{error}</div>}

            <table

                style={{
                    width: "70%",
                    borderCollapse: "collapse",
                    marginTop: "20px"
                }}
            >
                <thead>
                <tr style={{ backgroundColor: "#f0f0f0", textAlign: "left" }}>
                    <th colSpan={"2"} style={{ padding: "10px", border: "1px solid #ccc",textAlign:"center" } }>Favorites</th>
                </tr>
                </thead>
                <tbody>
                {favorites.map(favorite => (
                    <tr style={{
                        borderBottom: "1px solid #ddd",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-between"
                    }}>
                        <img width="100" height="100" src={favorite.image_url}/>
                        <h2>{favorite.name}</h2>
                        <Rating itemId={favorite.id}/>
                        <h2>{favorite.price}€</h2>
                        <button onClick={()=>removeFavorite(favorite.id)}>X</button>

                    </tr>
                ))}
                </tbody>
            </table>


        </div>
    );
}