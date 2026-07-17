import {useEffect, useState} from "react";

export default function Rating({ itemId }){
    const [rating, setRating] = useState(0);
    const [averageRating, setAverageRating] = useState(0);

    useEffect(() => {
        const fetchAverageRating = async () => {
            try {
                const token = localStorage.getItem("token");

                const response = await fetch(
                    `http://localhost:8080/ratings/${itemId}`,
                    {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`,
                        },
                    }
                );

                if (!response.ok) {
                    throw new Error("Failed to fetch rating");
                }

                const data = await response.json();
                setAverageRating(data); // or data.averageRating
            } catch (error) {
                console.error(error);
            }
        };

        const fetchUserRating = async () => {
            try {
                const token = localStorage.getItem("token");

                const response = await fetch(
                    `http://localhost:8080/ratings/user`,
                    {
                        headers: {
                            "Content-Type": "application/json",
                            Authorization: `Bearer ${token}`,
                        },
                        body: JSON.stringify({
                           "itemId": itemId
                        }),
                        method: "POST"
                    }
                );

                if (!response.ok) {
                    throw new Error("Failed to fetch rating");
                }

                const data = await response.json();
                setRating(data);
            } catch {
                setRating(0);
            }
        };

        fetchUserRating();
        fetchAverageRating();
    }, [itemId,rating]);

    const deleteRating = async (itemId) => {
        try {
            const token = localStorage.getItem("token");

            const response = await fetch(
                `http://localhost:8080/ratings/${itemId}`,
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    method:"DELETE"
                }
            );

            if (!response.ok) {
                throw new Error("Failed to delete rating");
            }

            setRating(0);
        } catch (error) {
            console.error(error);
        }

    }


    const rateItem = async (stars) => {
        try {
            const token = localStorage.getItem("token");

            if(stars === 1 && rating === 1){
                await deleteRating(itemId);
                return;
            }

            const response = await fetch(
                `http://localhost:8080/ratings`,
                {
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({
                        "itemId": itemId,
                        "stars": stars
                    }),
                    method:"POST"

                }
            );

            if (!response.ok) {
                throw new Error("Failed to post rating");
            }


            setRating(stars);
        } catch (error) {
            console.error(error);
        }

    }

    return (
        <div>
            {[1,2,3,4,5].map((star)=>(
                <span
                    key={star}
                    onClick={() => rateItem(star)}
                    style={{
                        cursor: "pointer",
                        fontSize: "2rem",
                        color: star <= rating ? "gold" : "gray",
                    }}
                >
                    ★
                </span>
            ))}
            <p>Rating: {averageRating.toFixed(1)}/5</p>

        </div>

    )
}