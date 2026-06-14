// src/components/user/Ticket.jsx
import { useLocation, Link } from "react-router-dom";

export default function Ticket() {
    const location = useLocation();
    const booking = location.state?.booking;

    if (!booking) {
        return (
            <div className="ticket-container">
                <p>لا توجد بيانات حجز</p>
                <Link to="/my-bookings">عرض حجوزاتي</Link>
            </div>
        );
    }

    return (
        <div className="ticket-container">
            <div className="ticket">
                <h2>🎬 بطاقة الحجز</h2>
                <hr />
                <p><strong>الفيلم:</strong> {booking.movieTitle}</p>
                <p><strong>المكان:</strong> {booking.movieLocation}</p>
                <p><strong>الوقت:</strong> {new Date(booking.showTime).toLocaleString()}</p>
                <p><strong>المقاعد:</strong> {booking.seatNumbers}</p>
                <p><strong>الإجمالي:</strong> {booking.totalPrice} $</p>
                <p><strong>الحالة:</strong> {booking.status}</p>
                <hr />
                {booking.qrCode && (
                    <img src={booking.qrCode} alt="QR Code" className="qr-code" />
                )}
                <p className="ticket-id">رقم الحجز: #{booking.id}</p>
            </div>
            <Link to="/" className="btn">العودة للأفلام</Link>
        </div>
    );
}