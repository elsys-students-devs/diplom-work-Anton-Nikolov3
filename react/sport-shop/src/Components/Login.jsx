import { useState } from "react";
import { useNavigate } from "react-router-dom"; 


export default function Login({ setToken, setRole }) {
     let [user, setUser] = useState(
        {
            username:"",
            password:"",
        }
    );
    let navigate = useNavigate();

    const handleSubmit = async (e) =>{
        e.preventDefault();
        
        try{
            let response = await fetch("http://localhost:8080/auth/login",{
            method: "POST",
            headers:{
            "Content-Type":"application/json",
            },
            body: JSON.stringify(user)
            });
        
            if(!response.ok){
                throw new Error("Login failed");
            }
            const data = await response.json();

            localStorage.setItem("token", data.token);
            localStorage.setItem("isAdmin", data.isAdmin);

            setToken(data.token);
            setRole(data.isAdmin);

            navigate("/shop");
        }
        catch(error)
        {
            alert(error.message);
        }
    }

    const handleChange = (e) => {
        const { name, value } = e.target;

        setUser((prevUser) => ({
            ...prevUser,
            [name]: value,
        }));
    };

    
    
    return(
        <div id="body">
            <h1>Login</h1>

            <form onSubmit={handleSubmit} id="login">
                <label> Username</label>
                    <input
                    type="text"
                    name="username"
                    value={user.username}
                    onChange={handleChange}
                />
                <label> Password</label>
                <input
                    type="password"
                    name="password"
                    value={user.password}
                    onChange={handleChange}
                />

                <button type="submit"> Login </button>
            </form>
        </div>
    )
}
