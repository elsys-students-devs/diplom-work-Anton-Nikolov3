import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";

export default function Admin({categories}) {
  let navigate = useNavigate();
  const [items, setItems] = useState([]);
  const [currentItem, setCurrentItem] = useState(
    { id: "", name: "", price: "",description:"", image_url: "", category: ""});

  const token = localStorage.getItem("token");
  const headers = {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
  const URL = "http://localhost:8080/item";

  useEffect(() => {
    fetch(URL, { headers })
      .then(res => res.json())
      .then(data => setItems(data))
      .catch(err => console.error(err));
  }, []);

  const handleAdd = async (e) => {
    e.preventDefault();
    const res = await fetch(URL, {
      method: "POST",
      headers,
      body: JSON.stringify({ 
        name: currentItem.name,
        price: Number(currentItem.price),
        image_url: currentItem.image_url,
        category:  currentItem.category}),
    });
    const newItem = await res.json();
    setItems([...items, newItem]);
    setCurrentItem({ id: "", name: "", price: "",description: "", image_url: "", category: "" });
  };

  

  const handleDelete = async (id) => {
    await fetch(`${URL}/${id}`, 
      { method: "DELETE", 
        headers });
      setItems(items.filter(item => item.id !== id));
  };

  const handleEditClick = (item) => {
      navigate("/update", {state: item} )
  };

  const handleChange = (e) => {
  const { name, value } = e.target;

  setCurrentItem(prev => ({
    ...prev,
    [name]: value
  }));
  };

  return (
    <div id="body">
      <h1>Admin Panel</h1>

      <form onSubmit={currentItem.id ? handleUpdate : handleAdd}>
        <h3>{currentItem.id ? "Update Item" : "Add Item"}</h3>
        <input
          type="text"
          name="name"
          placeholder="Name"
          value={currentItem.name}
          onChange={handleChange}
          required
        />

        <input
          type="number"
          name="price"
          placeholder="Price"
          value={currentItem.price}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="description"
          placeholder="Description"
          value={currentItem.description}
          onChange={handleChange}
          required
        />

        <input
          type="text"
          name="image_url"
          placeholder="Image URL"
          value={currentItem.image_url}
          onChange={handleChange}
        />
        <select
          name="category"
          value={currentItem.category || ""}
          onChange={handleChange}>
            <option value="">-- Select Category --</option>
            {categories.map((cat) => (
            <option key={cat} value={cat}>
            {cat}
            </option>
            ))}
            </select>
        
        <button type="submit">{currentItem.id ? "Update" : "Add"}</button>
        {currentItem.id && (
          <button type="button" onClick={() => setCurrentItem({ id: "", name: "", price: "", image_url: "" })}>
            Cancel
          </button>
        )}
      </form>

      <h3>All Items</h3>
      <table id="item-table">
        <tr>
          <th>Име</th>
          <th>Цена</th>
          <th>Описание</th>
          <th>Снимка</th>
          <th>Категория</th>
          <th></th>
          <th></th>
        </tr>
        {items.map(item => (
          <tr key={item.id}>
            <td class="item">{item.name}</td>
            <td class="item"> {item.price}€</td>
            <td class="item"> {item.description}</td>
            <td class="item">{item.image_url && <img src={item.image_url} alt={item.name}/>}</td>
            <td class="item"> {item.category}</td>
            <td><button onClick={() => 
              handleEditClick(item)}>Edit</button></td>
            <td><button onClick={() => 
              handleDelete(item.id)}>Delete</button></td>
          </tr>
        ))}
      </table>
    </div>
  );
}