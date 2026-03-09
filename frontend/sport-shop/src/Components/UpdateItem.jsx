import { useNavigate,useLocation } from "react-router-dom";
import { useState } from "react";

export default function UpdateItem(){
    const categories = [
  "Football",
  "Basketball",
  "Tennis",
  "Running",
  "Gym",
  "Swimming",
  "Cycling",
  "Boxing"
];
    const location = useLocation();
    const navigate = useNavigate();
    const [item, setItem] = useState(
    location.state || { id: "", name: "", description: "", price: "", image_url: "", category: "" });
    const token = localStorage.getItem("token");
    const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,};

    console.log(item);
    // Update item
  const handleUpdate = async (e) => {
    e.preventDefault();
    const res = await fetch(`http://localhost:8080/item/${item.id}`, {
      method: "PUT",
      headers,
      body: JSON.stringify(item),
    });
    navigate("/admin")
  };

  
    const handleChange = (e) => {
        const { name, value } = e.target;

        setItem((prevItem) => ({
            ...prevItem,
            [name]: value,
        }));
    };


  return(
    <div>
        <form onSubmit={handleUpdate}>
            <label> Име </label>
            <input
                type="text"
                name="name"
                value={item.name || ""}
                onChange={handleChange}
            />
            <label> Описание </label>
            <input
                type="text"
                name="description"
                value={item.description || ""}
                onChange={handleChange}
            />
            <label> Цена</label>
            <input
                type="number"
                name="price"
                value={item.price || 0}
                onChange={handleChange}
            />
            <label> Снимка</label>
            <input
                type="text"
                name="image_url"
                value={item.image_url || ""}
                onChange={handleChange}
            />
            <label> Категория</label>
            <select
                name="category"
                value={item.category || ""}
                onChange={handleChange}>
                <option value="">-- Select Category --</option>
                    {categories.map((cat) => (
                <option key={cat} value={cat}>
                {cat}
                </option>
                ))}
            </select>

            <button type="submit"> Save </button>
        </form>
    </div>
  )
    
}