import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Register(){
    const navigate = useNavigate()
    const [token, setToken] = useState(localStorage.getItem("token"));
    let [user, setUser] = useState({
        username:"",
        email:"",
        password:"",
    });

    if(token){
        navigate("\shop")
    }

    const handleSubmit = async (e) =>{
        e.preventDefault();

        const response = await fetch("http://localhost:8080/auth/register",
        {
            method: "POST",
            headers:{
                "Content-Type":"application/json",
            },
            body: JSON.stringify(user)
        })

        console.log(response)

        if(response.ok){
            console.log("Registered")
        }else{
            throw new Error("Register failed");
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
            <h1>Register</h1>

            <form onSubmit={handleSubmit} id="register">
                <label>username</label>
                <input
                    type="text"
                    name="username"
                    value={user.username}
                    onChange={handleChange}
                />
                <label>email</label>
                <input
                    type="text"
                    name="email"
                    value={user.email}
                    onChange={handleChange}
                />
                <label>password</label>
                <input
                    type="password"
                    name="password"
                    value={user.password}
                    onChange={handleChange}
                />

                <button type="submit"> Register </button>
            </form>
        </div>
    )
}