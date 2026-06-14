// src/components/user/SeatSelection.jsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../services/api";

export default function SeatSelection() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [movie, setMovie] = useState(null);
    const [selectedSeats, setSelectedSeats] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchMovie();
    }, [id]);

    const fetchMovie = async () => {
        const res = await api.get(`/movies/${id}`);
        setMovie(res.data);
    };

    if (!movie) return <p>جاري التحميل...</p>;

    // توليد قائمة كل المقاعد (مثال: A1..A10, B1..B10 حسب totalSeats)
    const generateSeatLabels = () => {
        const seats = [];
        const seatsPerRow = 10;
        const rows = Math.ceil(movie.totalSeats / seatsPerRow);
        let count = 0;

        for (let r = 0; r < rows; r++) {
            const rowLetter = String.fromCharCode(65 + r); // A, B, C...
            for (let c = 1; c <= seatsPerRow; c++) {
                if (count >= movie.totalSeats) break;
                seats.push(`${rowLetter}${c}`);
                count++;
            }
        }
        return seats;
    };

    const bookedCount = movie.totalSeats - movie.availableSeats;
    const allSeats = generateSeatLabels();

    const toggleSeat = (seat, index) => {
        // أول bookedCount مقعد نعتبرها محجوزة (محاكاة بسيطة)
        if (index < bookedCount) return;

        if (selectedSeats.includes(seat)) {
            setSelectedSeats(selectedSeats.filter((s) => s !== seat));
        } else {
            setSelectedSeats([...selectedSeats, seat]);
        }
    };

    const handleBooking = async () => {
        if (selectedSeats.length === 0) {
            setError("اختر مقعد واحد على الأقل");
            return;
        }
        setError("");
        setLoading(true);
        try {
            const res = await api.post("/bookings", {
                movieId: movie.id,
                seatNumbers: selectedSeats
            });
            // الانتقال لصفحة التذكرة مع بيانات الحجز
            navigate("/ticket", { state: { booking: res.data } });
        } catch (err) {
            setError(err.response?.data || "حدث خطأ أثناء الحجز");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="seat-selection">
            <div className="movie-header">
                <img src={`http://localhost:8080${movie.imageUrl}`} alt={movie.title} />
                <div>
                    <h2>{movie.title}</h2>
                    <p>{movie.description}</p>
                    <p>📍 {movie.location}</p>
                    <p>🕒 {new Date(movie.showTime).toLocaleString()}</p>
                    <p>💰 السعر للمقعد: {movie.price} $</p>
                </div>
            </div>

            <h3>اختر مقاعدك</h3>
            <div className="seats-grid">
                {allSeats.map((seat, index) => {
                    const isBooked = index < bookedCount;
                    const isSelected = selectedSeats.includes(seat);
                    return (
                        <div
                            key={seat}
                            className={`seat ${isBooked ? "booked" : ""} ${isSelected ? "selected" : ""}`}
                            onClick={() => toggleSeat(seat, index)}
                        >
                            {seat}
                        </div>
                    );
                })}
            </div>

            <div className="legend">
                <span className="seat"></span> متاح
                <span className="seat selected"></span> مختار
                <span className="seat booked"></span> محجوز
            </div>

            <div className="booking-summary">
                <p>المقاعد المختارة: {selectedSeats.join(", ") || "لا يوجد"}</p>
                <p>الإجمالي: {(selectedSeats.length * movie.price).toFixed(2)} $</p>
                {error && <p className="error">{error}</p>}
                <button onClick={handleBooking} disabled={loading}>
                    {loading ? "جاري الحجز..." : "تأكيد الحجز والدفع"}
                </button>
            </div>
        </div>
    );
}