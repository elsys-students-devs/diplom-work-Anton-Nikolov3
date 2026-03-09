import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login({ setToken, setRole }) {

    const [error, setError] = useState(null);

    const [user, setUser] = useState({
        username: "",
        password: "",
    });

    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(user)
            });

            if (!response.ok) {

            if (response.status === 400) {
                throw new Error("Invalid input data");
            }

            if (response.status === 401) {
                throw new Error("Wrong username or password");
            }

            if (response.status === 403) {
                throw new Error("You do not have permission");
            }

            if (response.status === 500) {
                    throw new Error("Server error. Try again later.");
                }

                throw new Error("Unexpected error");
            }   

            const data = await response.json();

            localStorage.setItem("token", data.token);
            localStorage.setItem("isAdmin", data.isAdmin);

            setToken(data.token);
            setRole(data.isAdmin);

            navigate("/shop");

        } catch (err) {
            setError(err.message);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;

        setUser(prev => ({
            ...prev,
            [name]: value
        }));
    };

    return (
        <div id="body">
            <h1>Login</h1>

            <form onSubmit={handleSubmit} id="login">

                <label>Username</label>
                <input
                    type="text"
                    name="username"
                    value={user.username}
                    onChange={handleChange}
                />

                <label>Password</label>
                <input
                    type="password"
                    name="password"
                    value={user.password}
                    onChange={handleChange}
                />

                {error && (
                    <textarea
                        value={error}
                        readOnly
                        rows={4}
                    style={{ color: "red", width: "100%",resize: "none" }}
                    />
                )}

                <button type="submit">Login</button>

            </form>
        </div>
    );
}