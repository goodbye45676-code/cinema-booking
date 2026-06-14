// src/components/user/MovieList.jsx
import { useEffect, useState } from "react";
import api from "../../services/api";
import MovieCard from "./MovieCard";

export default function MovieList() {
    const [movies, setMovies] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchMovies();
    }, []);

    // في MovieList.jsx بعد fetchMovies
const fetchMovies = async () => {
    try {
        const res = await api.get("/movies");
        console.log("MOVIES DATA:", res.data); // << أضف هذا
        setMovies(res.data);
    } catch (err) {
        console.error(err);
    } finally {
        setLoading(false);
    }
};


    if (loading) return <p>جاري التحميل...</p>;

    return (
        <div className="movie-list">
            <h2>الأفلام المتاحة</h2>
            <div className="movie-grid">
                {movies.map((movie) => (
                    <MovieCard key={movie.id} movie={movie} />
                ))}
            </div>
        </div>
    );
}