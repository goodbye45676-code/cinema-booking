// src/components/user/MovieCard.jsx
import { Link } from "react-router-dom";

export default function MovieCard({ movie }) {
    if (!movie || !movie.id) return null; // << protection

    return (
        <div className="movie-card">
            <img
                src={`http://localhost:8080${movie.imageUrl}`}
                alt={movie.title}
                className="movie-image"
                onError={(e) => { e.target.src = '/placeholder.jpg'; }} // << fallback
            />
            <div className="movie-info">
                <h3>{movie.title}</h3>
                <p>📍 {movie.location}</p>
                <p>🕒 {new Date(movie.showTime).toLocaleString()}</p>
                <p>💰 {movie.price} $</p>
                <p>🎟️ متاح: {movie.availableSeats} / {movie.totalSeats}</p>
                <Link to={`/movies/${movie.id}`} className="btn">
                    عرض التفاصيل
                </Link>
            </div>
        </div>
    );
}