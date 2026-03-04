import { useState, useEffect  } from 'react'
import './App.css'

import { BrowserRouter, Routes, Route, Link, NavLink} from "react-router-dom"
import Home from './Components/Home'
import Cart from './Components/Cart'
import Login from './Components/Login'
import Register from './Components/Register'
import Shop from './Components/Shop'
import Admin from './Components/Admin'
import UpdateItem from './Components/UpdateItem'

export  default function App() {
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
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [admin, setRole] = useState(localStorage.getItem("isAdmin"));
  const [cartCount, setCartCount] = useState(0);

  useEffect(() => {
  if (!token) {
    setCartCount(0);
    return;
  }

  const fetchCart = async () => {
    try {
      const response = await fetch("http://localhost:8080/cart", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const data = await response.json();

      const total = data.reduce(
        (sum, item) => sum + item.quantity,
        0
      );

      setCartCount(total);
    } catch (err) {
      console.error("Failed to fetch cart:", err);
    }
    };

  fetchCart();
}, [token]);

  function Logout() {
  localStorage.removeItem("token");
  localStorage.removeItem("isAdmin");
  setToken(null);
  setRole(null);
  }
  return (
      <BrowserRouter>
        <nav id="navbar">
          <ul>
            <div id='left'>
              <li><Link to="/">Home</Link></li>
              <li><Link to="/shop"> Shop</Link></li>   
              <li>
              <Link to="/cart">
                  Cart{cartCount > 0 && `(${cartCount})`}
              </Link>
              </li>
            </div>
            
            {admin? 
            <li><Link to="/admin">Admin</Link></li>
            : ""
            }
            
            
            <div id='right'>
              {token? 
              <li><button onClick={Logout}>Logout</button></li>:
              <li><Link to="/login">Login</Link></li> 
              }
              {token? 
              ""
              :
              <li><Link to="/register">Register</Link></li>}
              
            </div>


            </ul>
        </nav>

        <Routes>
            <Route path='/' element={<Home />}/>
            <Route path='/cart' element={<Cart setCartCount={setCartCount} />} />
            <Route path='/login' element={<Login 
            setToken={setToken} setRole={setRole} />} />
            <Route path='/register' element={<Register />}/>
            <Route path='/shop' element={<Shop 
            setCartCount={setCartCount} categories={categories} />}/>
            <Route path='/admin' element={<Admin categories={categories}/>}></Route>
            <Route path='/update' element={<UpdateItem/>}></Route>
        </Routes>
      </BrowserRouter>
  )
}
