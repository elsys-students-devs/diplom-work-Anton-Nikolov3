
import { useNavigate } from "react-router-dom"



export default function Home(){
    const navigate = useNavigate()
    return(
       <div id="body">
  <h1>Добре дошли в SportShop!</h1>
  <p>Открий любимите си спортни артикули.</p>
  <button onClick={() => navigate("/shop")}
    style={{
    backgroundColor: "#646cff",
    color: "#fff",
    border: "none",
    borderRadius: "12px",

  }}>Shop Now</button>
 </div>
    )
}
