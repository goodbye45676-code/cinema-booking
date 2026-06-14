// src/components/user/MyBookings.jsx
import { useEffect, useState } from "react";
import api from "../../services/api";

export default function MyBookings() {
    const [bookings, setBookings] = useState([]);

    useEffect(() => {
        fetchBookings();
    }, []);

    const fetchBookings = async () => {
        const res = await api.get("/bookings/my");
        setBookings(res.data);
    };

    const handleCancel = async (id) => {
        if (!window.confirm("هل تريد إلغاء هذا الحجز؟")) return;
        await api.delete(`/bookings/${id}`);
        fetchBookings();
    };

    return (
        <div className="my-bookings">
            <h2>حجوزاتي</h2>
            {bookings.length === 0 && <p>لا توجد حجوزات</p>}
            {bookings.map((b) => (
                <div key={b.id} className="booking-card">
                    <div className="booking-card__info">
                        <h3>{b.movieTitle}</h3>
                        <p><span>المكان</span><span>{b.movieLocation}</span></p>
                        <p><span>المقاعد</span><span>{b.seatNumbers}</span></p>
                        <p><span>الإجمالي</span><span>{b.totalPrice} $</span></p>
                        <span className={`booking-card__status ${b.status}`}>
                            {b.status}
                        </span>
                    </div>

                    <div className="booking-card__side">
                        {b.qrCode && <img src={b.qrCode} alt="QR" />}
                        {b.status !== "CANCELLED" && (
                            <button onClick={() => handleCancel(b.id)}>إلغاء الحجز</button>
                        )}
                    </div>
                </div>
            ))}
        </div>
    );
}